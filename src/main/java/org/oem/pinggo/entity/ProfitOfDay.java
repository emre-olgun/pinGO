package org.oem.pinggo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_profitrecord_date_seller", columnNames = {"date", "seller_id"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfitOfDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    private long totalProfitfortheday;
}