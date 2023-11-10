package org.oem.pinggo.dto;

import lombok.Data;

@Data
public class ProductUpdateRequest {
    private String name;

    private String description;

    private Integer quantity;

}
