package pl.edu.pw.ee.rutynar.auctionsystem.services;

import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.Game;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.dtos.user.NewUserDTO;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> createUserFromForm(NewUserDTO userDTO);

    Mono<Game> getUserGamesFromLibrary(User user);

    Mono<User> getCurrentUser();
}
