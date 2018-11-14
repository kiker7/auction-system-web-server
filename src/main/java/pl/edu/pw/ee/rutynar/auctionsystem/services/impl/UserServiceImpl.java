package pl.edu.pw.ee.rutynar.auctionsystem.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.rutynar.auctionsystem.config.security.CustomPasswordEncoder;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Library;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Role;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.LibraryRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.dtos.user.NewUserDTO;
import pl.edu.pw.ee.rutynar.auctionsystem.services.UserService;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    @Override
    public Mono<User> createUserFromForm(NewUserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(customPasswordEncoder.encode(userDTO.getPassword()));
        user.setRoles(Collections.singletonList(Role.USER));
        user.setEnabled(true);

        return userRepository.save(user)
                .zipWith(libraryRepository.save(new Library()), (u, lib) -> {
                    u.setLibrary(lib);
                    return u;
                }).flatMap(u -> userRepository.save(u));
    }
}
