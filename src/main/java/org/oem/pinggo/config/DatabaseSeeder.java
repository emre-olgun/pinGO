package org.oem.pinggo.config;

import org.oem.pinggo.enums.ERole;
import org.oem.pinggo.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class         DatabaseSeeder {

    //   @Autowired
    private JdbcTemplate jdbcTemplate;

    private UserRepository userRepository;
//private MailService mailService;

    public DatabaseSeeder(JdbcTemplate jdbcTemplate, UserRepository userRepository) {

        this.jdbcTemplate = jdbcTemplate;


        try {


           // jdbcTemplate.execute("delete from roles");
            String sql = "INSERT IGNORE INTO roles(id,name) VALUES(?,?)";
            Arrays.stream(ERole.values()).forEach(
                    key -> {

                        jdbcTemplate.update(sql, key.ordinal() + 1, key.toString());
                    }

            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }


}

