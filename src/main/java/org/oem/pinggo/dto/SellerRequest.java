package org.oem.pinggo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class SellerRequest {
    @NotBlank
    private String businessName;

    @NotBlank
    private String businessAddress;


}
