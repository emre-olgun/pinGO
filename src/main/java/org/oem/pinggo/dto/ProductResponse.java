package org.oem.pinggo.dto;

import lombok.Data;
import org.oem.pinggo.entity.Product;

@Data
public class ProductResponse {
    Long id;
    String name;
    String description;
    int quantity;
    Long ownerId;


    public ProductResponse(Product entity) {
        this.id = entity.getId();
        this.ownerId = entity.getSeller().getId();
        this.description = entity.getDescription();
        this.name = entity.getName();
        this.quantity = entity.getQuantity();
    }


    //for not found implementation

}
