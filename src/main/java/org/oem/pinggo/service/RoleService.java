package org.oem.pinggo.service;

import lombok.RequiredArgsConstructor;
import org.oem.pinggo.entity.Role;
import org.oem.pinggo.enums.ERole;
import org.oem.pinggo.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<Role> findByName(ERole eRole) {
        return roleRepository.findByName(eRole);
    }
}
