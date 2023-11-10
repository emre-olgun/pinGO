package org.oem.pinggo.repository;

import java.util.List;
import java.util.Optional;

import org.oem.pinggo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {


  @NonNull
  Optional<User> findByEmail(String email);
  Optional<User> findByUsername(String username);
List<User> findUserByIdLessThan(Long id);
  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  @Transactional
  @Modifying
  @Query("update User u set u.name = ?1   where u.id = ?2")
  int updateNameById(String name, Long id);


}
