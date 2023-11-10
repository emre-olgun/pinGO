package org.oem.pinggo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class Product extends BaseEntity {


    @OneToMany(mappedBy = "product")
    Set<Ordering> orderings;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Size(message = "max 60 char", min = 0, max = 60)
    @NotEmpty(message = "Please write anything for name")
    @NotNull(message = "Must be filled for name")
    private String name;
    @NotEmpty(message = "Please write anything for desc.")
    @NotNull(message = "Must be filled desc.")
    @Column(name = "description", nullable = false)

    private String description;
    @JoinColumn(
            name = "seller_id")
    @ManyToOne
    private Seller seller;
    private int quantity;

}