package org.oem.pinggo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.dto.OrderingRequest;
import org.oem.pinggo.dto.OrderingResponse;
import org.oem.pinggo.service.OrderingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderingService orderingService;


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderingResponse> getOneOrder(@PathVariable long orderId) {
        return orderingService.getOneOrder(orderId);
    }


    @GetMapping("/myorders")
    public ResponseEntity<?> getMyOrders() {
        return orderingService.getMyOrders();

    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderingRequest orderRequest) {
        return orderingService.createOrder(orderRequest);
    }


    @DeleteMapping("/{orderId}/reject")
    public ResponseEntity<?> rejectOrder(@PathVariable Long orderId) {
        return orderingService.rejectOrder(orderId);

    }


    @GetMapping("/get-delivered-latest-day")
    public ResponseEntity<?> getDeliveredLatestDay() {
        log.info("fetch all delivered latest day controller");
        return orderingService.getDeliveredLatestDay();
    }

    @GetMapping("/{orderId}/accept")
    public ResponseEntity<?> acceptOrder(@PathVariable Long orderId) {
        return orderingService.acceptOrder(orderId);

    }


    @DeleteMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('USER')&&@orderingService.currentUserisOwnerOfOrder(#orderId)")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        return orderingService.cancelOrder(orderId);
    }


}
