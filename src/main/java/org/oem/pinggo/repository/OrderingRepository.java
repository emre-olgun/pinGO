package org.oem.pinggo.repository;

import org.oem.pinggo.entity.Ordering;
import org.oem.pinggo.entity.User;
import org.oem.pinggo.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderingRepository extends JpaRepository<Ordering, Long> {
    List<Ordering> findByCustomer(User customer);


    @Query("select (count(o) > 0) from Ordering o where o.id = :oID and  o.customer.id = :cID")
    boolean existsOrderCustomerwithIDs(@Param("oID") Long oID, @Param("cID") Long cID);


    @Query("select o from Ordering o where o.orderStatus = :orderStatus and o.timeOfOrder > :timeOfOrder")
    List<Ordering> findByOrderStatusAndTimeOfOrderAfter(@Param("orderStatus") OrderStatus orderStatus, @Param("timeOfOrder") LocalDateTime timeOfOrder);


List<Ordering> findAllByOrderStatus(OrderStatus orderStatus);

}