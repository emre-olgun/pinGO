package org.oem.pinggo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.config.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.oem.pinggo.repository.UserRepository;
import org.oem.pinggo.entity.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
private final   UserRepository userRepository;
private final Translator translator;
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> {


                 String errorMessage = translator.toLocale("user.not.found.with.username.exception", username);
log.error(" searching failed with {}  : {}",username,errorMessage);

         return new UsernameNotFoundException(errorMessage);



}

      );

    return UserDetailsImpl.build(user);
  }

}
