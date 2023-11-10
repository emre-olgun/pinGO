package org.oem.pinggo.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.dto.*;
import org.oem.pinggo.entity.Role;
import org.oem.pinggo.entity.Seller;
import org.oem.pinggo.entity.User;
import org.oem.pinggo.entity.VerificationToken;
import org.oem.pinggo.enums.ERole;
import org.oem.pinggo.exception.InvalidVerificationTokenException;
import org.oem.pinggo.exception.TimeoutVerificationTokenException;
import org.oem.pinggo.exception.UserNotFoundWithEmailException;
import org.oem.pinggo.repository.RoleRepository;
import org.oem.pinggo.repository.SellerRepository;
import org.oem.pinggo.repository.UserRepository;
import org.oem.pinggo.repository.VerificationTokenRepository;
import org.oem.pinggo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final Translator translator;


    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final VerificationTokenService verificationTokenService;
    private final JwtUtils jwtUtils;


    @Value("${server.port}")
    private String port;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(new UserInfoResponse(jwtCookie.toString(),

                userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));

    }

    public ResponseEntity<?> registerUser(HttpServletRequest request, SignupRequest signUpRequest) {
String message;
        if (userService.existsByUsername(signUpRequest.getUsername())) {

         message   = translator.toLocale("error.username.is.already.taken", signUpRequest.getUsername());
            log.info("try dublicate username : {}",message);


            return ResponseEntity.badRequest().body(new MessageResponse(message));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {


            message   = translator.toLocale("error.email.is.already.in.use", signUpRequest.getUsername());
            log.info("try dublicate email : {}",message);
            return ResponseEntity.badRequest().body(new MessageResponse(message));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),signUpRequest.getName(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));


        Set<Role> roles = new HashSet<>();

            Role userRole = roleService.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);



        user.setRoles(roles);


        userService.save(user);
        String contextPath = getAppUrl(request);

        mailService.sendMail(constructEmailMessage(contextPath, user, user.getVerificationToken().getToken()));

        message   = translator.toLocale("user.registered.successfully.with", signUpRequest.getUsername());
        log.info(message);

        return ResponseEntity.ok(new MessageResponse(message+"http:localhost:" + port + "/api/auth/verify/" + user.getVerificationToken().getToken()));
    }

    private SimpleMailMessage constructEmailMessage(final String contextPath, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = translator.toLocale("registration.confirmation");
        final String confirmationUrl = contextPath + "/api/auth/verify/" + token;
        final String message = translator.toLocale("authRegSuccessLink");
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom("${support.email");
        return email;
    }


    public ResponseEntity<?> logoutUser() {

        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

         String message = translator.toLocale("you.have.been.signed.out");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse(message));
    }

    public ResponseEntity<?> verify(String token) {


        VerificationToken vt = verificationTokenService.findByToken(token).orElseThrow(

                () -> {

                    final String errorMessage = translator.toLocale("invalid.token", token);
                    log.error("token validation error : {}",errorMessage);
                    throw new InvalidVerificationTokenException(errorMessage);
                }

        );
        User user = vt.getUser();
        Calendar cal = Calendar.getInstance();
        if ((vt.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {

           final String errorMessage = translator.toLocale("timeout.token", token,vt.getExpiryDate().getTime());
log.error("errorMessage");
            throw new TimeoutVerificationTokenException(errorMessage);
        }


        user.setEnabled(true);
        userService.save(user);
        final String message = translator.toLocale("validated.token");
        return ResponseEntity.ok(new MessageResponse(message));

    }

    // User activation - verification
    public ResponseEntity<?> resendRegistrationToken(final HttpServletRequest request, final String existingToken) {
        final VerificationToken newToken = generateNewVerificationToken(existingToken);
        final User user = newToken.getUser();

        mailService.sendMail((constructResendVerificationTokenEmail(getAppUrl(request), newToken, user)));
        return ResponseEntity.ok(translator.toLocale("message.resendToken"));
    }

    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = verificationTokenService.findByToken(existingVerificationToken).get();
        vToken.setToken(UUID.randomUUID().toString());

        vToken = verificationTokenService.save(vToken);
        return vToken;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


    private SimpleMailMessage constructResendVerificationTokenEmail(String contextPath, VerificationToken newToken, User user) {
        String confirmationUrl = contextPath + "/api/auth/verify/" + newToken.getToken();

        String title = translator.toLocale("auth.resendTokenTitle");
        String message = translator.toLocale("auth.resendToken");


        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(title);
        email.setText(message + " rn" + confirmationUrl);
        email.setFrom("${support.email");
        email.setTo(user.getEmail());
        return email;
    }

    private SimpleMailMessage RandomlyCreatedPasswordEmail(String password, User user) {

        String title = translator.toLocale("auth.randomlyCreatedPasswordSubject");
        String message = translator.toLocale("auth.randomlyCreatedPasswordBody");


        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(title);
        email.setText(message + " rn" + password);
        email.setFrom("${support.email");
        email.setTo(user.getEmail());
        return email;
    }


    public ResponseEntity<?> forgetPassword(String mail) {
        User user = userService.findByEmail(mail).orElseThrow(() -> 
{
            
           final String errorMessage = translator.toLocale("no.user.with.mail.address",mail);
log.error("errorMessage");

        return    new UserNotFoundWithEmailException(errorMessage);
}

        );

        String password = encoder.encode(UUID.randomUUID().toString().substring(0, 10));
        user.setPassword(password);
        userService.save(user);
        return ResponseEntity.ok("Randomly created password is sended via mail");
    }

    public ResponseEntity<?> becomeSeller(SellerRequest sellerRequest) {

        User currentUser = userService.getCurrentUser();
        Set<Role> roles =  currentUser.getRoles();
        Role sellerRole = roleService.findByName(ERole.ROLE_SELLER).get();
        roles.add(sellerRole);
        currentUser.setRoles(roles);
//
Seller newSeller=new Seller(currentUser, sellerRequest.getBusinessAddress(), sellerRequest.getBusinessName());
userService.saveSeller(newSeller);
//sellerRepository.save(newSeller);
//currentUser.setSelleraccount(newSeller);
//userService.save(currentUser);
//       // currentUser.setBusinessAddress(businessAdress);
//return ResponseEntity.ok(currentUser);
return null;
    }
}
