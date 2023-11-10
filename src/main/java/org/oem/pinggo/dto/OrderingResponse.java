package org.oem.pinggo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.oem.pinggo.entity.Ordering;
import org.oem.pinggo.enums.OrderStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.oem.pinggo.entity.Ordering}
 */
@Value
public class OrderingResponse implements Serializable {
    int quantity;
    OrderStatus orderStatus;
    Long customerId;
    Long productId;
    @NotNull
    LocalDateTime timeOfOrder;


    public OrderingResponse(Ordering ordering){

        this.quantity=ordering.getQuantity();
        this.orderStatus=ordering.getOrderStatus();
        this.customerId=ordering.getCustomer().getId();
        this.productId=ordering.getProduct().getId();
        this.timeOfOrder=ordering.getTimeOfOrder();
    }
}