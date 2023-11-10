package org.oem.pinggo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.oem.pinggo.dto.LoginRequest;
import org.oem.pinggo.dto.SellerRequest;
import org.oem.pinggo.dto.SignupRequest;
import org.oem.pinggo.service.AuthService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "User", description = "User management APIs")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final ApplicationEventPublisher eventPublisher;
    private final AuthService authService;


    @Operation(summary = "For Singin", description = "username:admin , password:123456 ")
    @PostMapping("/signin")
    @CrossOrigin(origins = "http://localhost:4200")

    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/become-seller")
    @PreAuthorize("hasRole('USER')")

    public ResponseEntity<?> becomeSeller(@RequestBody SellerRequest sellerRequest) {
        return authService.becomeSeller(sellerRequest);
    }

    @Operation(summary = "For Singup", description = "username:randomly , password:randomly, email:valid randomly, role:[admin, mod ..etc] ")
    @ApiResponse(description = "data of user")
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(HttpServletRequest request, @Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(request, signUpRequest);
    }


    @GetMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestParam @Email String mail) {
        authService.forgetPassword(mail);
        return ResponseEntity.ok(mail);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        return authService.logoutUser();

    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verify(@Parameter(description = "Validation code") @PathVariable String token) {
        return authService.verify(token);
    }


    @GetMapping("/resendRegistrationToken")

    public ResponseEntity<?> resendRegistrationToken(HttpServletRequest request, @RequestParam("token") String existingToken) {
        return authService.resendRegistrationToken(request, existingToken);
    }


}
