package org.oem.pinggo.dto;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max=50)
    private String name;


    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
  

}
