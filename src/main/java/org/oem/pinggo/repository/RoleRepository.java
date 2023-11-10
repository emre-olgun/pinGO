package org.oem.pinggo.repository;

import java.util.Optional;

import org.oem.pinggo.enums.ERole;
import org.oem.pinggo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
