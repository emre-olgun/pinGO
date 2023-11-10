package org.oem.pinggo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.dto.OrderingRequest;
import org.oem.pinggo.dto.OrderingResponse;
import org.oem.pinggo.entity.*;
import org.oem.pinggo.enums.OrderStatus;
import org.oem.pinggo.exception.InsufficientResourceException;
import org.oem.pinggo.exception.ItemOwnerException;
import org.oem.pinggo.exception.NoSuchElementFoundException;
import org.oem.pinggo.exception.OrderStatusNoEligibleForEditException;
import org.oem.pinggo.repository.OrderingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderingService {

    private final OrderingRepository orderingRepository;
    private final ProductService productService;
    private final Translator translator;
    private final UserService userService;
    private final ProfitOfDayService profitOfDayService;

    public ResponseEntity<OrderingResponse> getOneOrder(long orderId) {

        Ordering ordering = orderingRepository.findById(orderId).orElseThrow(() -> {
            String errorMessage = translator.toLocale("order.not.found.with.id.exception", orderId);
            log.error(errorMessage);
            return new NoSuchElementFoundException(errorMessage);
        });
        return ResponseEntity.ok(new OrderingResponse(ordering));

    }

    public ResponseEntity<?> getDeliveredLatestDay() {
        log.info("fetch all delivered latest day");

        List<Ordering> orderingList = orderingRepository.findByOrderStatusAndTimeOfOrderAfter(OrderStatus.DELIVERED, LocalDateTime.now().minusDays(1));

        final Map<Seller, List<Ordering>> collect = orderingList.stream().collect(groupingBy(o -> o.getProduct().getSeller(), toList()));
        final Map<Seller, Long> sellerrevenue = new HashMap<>();
        collect.entrySet().stream().forEach(entry -> {
            Seller seller = entry.getKey();
            final long summed = entry.getValue().stream().mapToLong(a -> a.getQuantity()).sum();
            System.out.println("u: %s summed %s".formatted(seller, summed));
            sellerrevenue.put(seller, summed);
        });

        return ResponseEntity.ok(sellerrevenue);
    }


    @Transactional
    //job
    @Scheduled(cron = "1 0 0 * * ?")
    public void getDeliveredLatestDaytoProfitOfDay() {
        log.info("fetch all delivered latest day");

        List<Ordering> orderingList = orderingRepository.findByOrderStatusAndTimeOfOrderAfter(OrderStatus.DELIVERED, LocalDateTime.now().minusDays(1));

        final Map<Seller, List<Ordering>> collect = orderingList.stream().collect(groupingBy(o -> o.getProduct().getSeller(), toList()));
        collect.entrySet().stream().forEach(entry -> {
            Seller seller = entry.getKey();
            final long summed = entry.getValue().stream().mapToLong(a -> a.getQuantity()).sum();
            System.out.println("u: %s summed %s".formatted(seller, summed));
            ProfitOfDay profitOfDay = new ProfitOfDay();
            profitOfDay.setTotalProfitfortheday(summed);
            profitOfDay.setSeller(seller);
            profitOfDayService.save(profitOfDay);
        });
    }



    @Transactional
    //job
    @Scheduled(cron = "0/40 * * * * ?")
    public void acceptedToDelivered() {
        log.info("checking for accepted");
        final List<Ordering> allAccepted = orderingRepository.findAllByOrderStatus(OrderStatus.ACCEPTED);
        final List<Ordering> willSave = new ArrayList<Ordering>();

        //15 dakikadan fazla zaman geçti
        allAccepted.stream().filter(o -> o.getTimeOfOrder().plusMinutes(15).isBefore(LocalDateTime.now())).forEach(o -> {
            o.setOrderStatus(OrderStatus.DELIVERED);
            willSave.add(o);
        });
        allAccepted.stream().filter(o -> o.getTimeOfOrder().plusMinutes(2).isBefore(LocalDateTime.now())).forEach(o -> {
            if (Math.random() < 0.5) {
                o.setOrderStatus(OrderStatus.DELIVERED);
                willSave.add(o);
            }
        });
        orderingRepository.saveAll(willSave);

    }


    public ResponseEntity<?> getMyOrders() {

        User currentUser = userService.getCurrentUser();

        log.info(currentUser.getName());
        final List<Ordering> orderingOfUser = orderingRepository.findByCustomer(currentUser);
        final List<OrderingResponse> orderingResponseList = orderingOfUser.stream().map(OrderingResponse::new).toList();

        return ResponseEntity.ok(orderingResponseList);

    }

    @Transactional
    public ResponseEntity<?> acceptOrder(Long orderId) {
        Ordering order = findorthrowbyId(orderId);
        productService.currentSellerisOwnerofProduct(order.getProduct().getId());
        final String message;
        if (order.getOrderStatus().equals(OrderStatus.NEW)) {
            order.setOrderStatus(OrderStatus.ACCEPTED);
            order.setTimeOfOrder(LocalDateTime.now());
            orderingRepository.save(order);
            message = translator.toLocale("order.with.id.accepted", orderId);
//task
            // markasDelivered(order);
        } else {

            message = translator.toLocale("only.newly.created.can.be.accepted.with.id.not.new", orderId);
            log.error("status.not.chanced : {}", message);
            throw new OrderStatusNoEligibleForEditException(message);
        }

        return ResponseEntity.ok(message);
    }


    public boolean currentUserisOwnerOfOrder(Long orderId) {
        Long currentUserId = userService.getCurrentUserId();

        if (!orderingRepository.existsOrderCustomerwithIDs(orderId, currentUserId)) {

            final String errorMessage = translator.toLocale("with.id.order.is.from.different.user", orderId);
            log.error("{} user is trying to edit {} : {} ", currentUserId, orderId, errorMessage);
            throw new ItemOwnerException(errorMessage);


        }
        return true;
    }


    public ResponseEntity<?> rejectOrder(Long orderId) {
        Ordering order = findorthrowbyId(orderId);
        productService.currentSellerisOwnerofProduct(order.getProduct().getId());
        final String message;
        if (order.getOrderStatus().equals(OrderStatus.NEW)) {
            order.setOrderStatus(OrderStatus.REJECTED);
            orderingRepository.save(order);
            message = translator.toLocale("order.with.id.rejected", orderId);
//iade
            productService.increaseQuantityforReturn(order.getProduct(), order.getQuantity());
        } else {

            message = translator.toLocale("only.newly.created.can.be.rejected.with.id.not.new", orderId);

            log.error("{} status is not chanced : {}", orderId, message);
            throw new OrderStatusNoEligibleForEditException(message);
        }

        return ResponseEntity.ok(message);
    }

    public Ordering findorthrowbyId(Long orderId) {

        Ordering order = orderingRepository.findById(orderId).orElseThrow(() -> {
            final String errorMessage = translator.toLocale("order.not.found.exception.with.id", orderId);
            log.error("not found element : {}", errorMessage);
            throw new NoSuchElementFoundException(errorMessage);
        });
        return order;
    }


    public ResponseEntity<?> cancelOrder(Long orderId) {
        Ordering order = findorthrowbyId(orderId);
        //  productService.currentSellerisOwnerofProduct(order.getProduct().getId());
        final String message;
        if (order.getOrderStatus().equals(OrderStatus.NEW)) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderingRepository.save(order);
            message = translator.toLocale("order.with.id.cancelled", orderId);
//iade
            productService.increaseQuantityforReturn(order.getProduct(), order.getQuantity());

        } else {

            message = translator.toLocale("only.newly.created.can.be.cancelled.with.id.not.new", orderId);

            log.error("{} order status is not chanced : {}", orderId, message);
            throw new OrderStatusNoEligibleForEditException(message);
        }

        return ResponseEntity.ok(message);
    }


    public ResponseEntity<?> createOrder(OrderingRequest orderRequest) {
        User currentUser = userService.getCurrentUser();
        Long productId = orderRequest.getProductID();
        Product product = productService.getRef(productId);
        int orderQuantity = orderRequest.getQuantity();
        Ordering newOrder = new Ordering();

        if (orderQuantity <= product.getQuantity()) {
            newOrder.setProduct(product);
            newOrder.setQuantity(orderQuantity);
            newOrder.setCustomer(currentUser);
            productService.decreaseQuantity(product, orderQuantity);

            orderingRepository.save(newOrder);


        } else {
            String errorMessage = translator.toLocale("product.insufficient.with.id.quantity.exception", productId, orderQuantity);
            log.error("order is not created : {}", errorMessage);
            throw new InsufficientResourceException(errorMessage);
        }

        return ResponseEntity.ok(new OrderingResponse(newOrder));
    }
}
