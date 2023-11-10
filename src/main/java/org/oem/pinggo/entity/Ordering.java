package org.oem.pinggo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.oem.pinggo.enums.OrderStatus;

import java.time.LocalDateTime;


@Entity
@Table(name = "orders")
@Getter
@Setter
public class Ordering extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.NEW;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @Column(name = "time_of_order", nullable = false)
    private LocalDateTime timeOfOrder = LocalDateTime.now();

}