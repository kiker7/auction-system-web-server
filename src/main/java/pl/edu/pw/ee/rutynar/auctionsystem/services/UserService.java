package pl.edu.pw.ee.rutynar.auctionsystem.services;

import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Game;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Library;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.dtos.user.NewUserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> createUserFromForm(NewUserDTO userDTO);

    Mono<User> getCurrentUser();

    User getCurrentUserObject();

    Mono<Void> deleteUser(User user);
}
