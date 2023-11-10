package org.oem.pinggo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.oem.pinggo.enums.OrderStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class OrderingRequest implements Serializable {
    int quantity;
    Long productID;
}