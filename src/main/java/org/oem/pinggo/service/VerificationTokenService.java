package org.oem.pinggo.service;

import lombok.RequiredArgsConstructor;
import org.oem.pinggo.entity.VerificationToken;
import org.oem.pinggo.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

private final VerificationTokenRepository verificationTokenRepository;

    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public VerificationToken save(VerificationToken vToken) {
        return verificationTokenRepository.save(vToken);
    }
}
