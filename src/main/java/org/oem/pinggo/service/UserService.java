package org.oem.pinggo.service;

import lombok.RequiredArgsConstructor;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.entity.Seller;
import org.oem.pinggo.entity.User;
import org.oem.pinggo.exception.UserNotFoundWithIdException;
import org.oem.pinggo.repository.SellerRepository;
import org.oem.pinggo.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final Translator translator;

    public Seller getCurrentSeller() {
//        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        //from contextholder: no required check
//        Seller currentSeller = sellerRepository.getSeller(ud.getId()).get();
//
//        return currentSeller;

        return getCurrentUser().getSelleraccount();
    }

    public User getCurrentUser() {
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //from contextholder: no required check
        User currentUser = userRepository.getReferenceById(ud.getId());
        return currentUser;
    }


    public User getRef(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            String errorMessage = translator.toLocale("user.not.found.with.id.exception", userId);
            return new UserNotFoundWithIdException(errorMessage);
        });
        return user;

    }

    public Long getCurrentUserId() {
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //from contextholder: no required check
        return ud.getId();
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String mail) {
        return userRepository.findByEmail(mail);
    }

    public void saveSeller(Seller newSeller) {
        userRepository.save(newSeller.getUser());

        sellerRepository.save(newSeller);
    }
}
