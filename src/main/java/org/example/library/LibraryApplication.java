package org.example.library;

//import org.example.library.entities.Role;
//import org.example.library.repositories.RoleRepository;
import org.example.library.entities.Role;
import org.example.library.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class LibraryApplication  implements CommandLineRunner {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Autowired
    private RoleRepository roleRepository;
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {

            Role role = new Role();
            role.setId(1);
            role.setName("ROLE_ADMIN");

            Role role1 = new Role();
            role1.setId(2);
            role1.setName("ROLE_NORMAL");

            List<Role> roles = List.of(role, role1);


            List<Role> result = this.roleRepository.saveAll(roles);

            result.forEach(r -> {
                System.out.println(r.getName());
            });

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
