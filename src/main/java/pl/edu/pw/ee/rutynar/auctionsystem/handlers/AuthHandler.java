package pl.edu.pw.ee.rutynar.auctionsystem.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.config.security.JwtAuthenticationRequest;
import pl.edu.pw.ee.rutynar.auctionsystem.config.security.JwtAuthenticationResponse;
import pl.edu.pw.ee.rutynar.auctionsystem.config.security.JwtTokenUtil;
import pl.edu.pw.ee.rutynar.auctionsystem.data.domain.User;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;
import pl.edu.pw.ee.rutynar.auctionsystem.dtos.user.NewUserDTO;
import pl.edu.pw.ee.rutynar.auctionsystem.services.UserService;
import pl.edu.pw.ee.rutynar.auctionsystem.services.impl.UserServiceImpl;
import reactor.core.publisher.Mono;

@Component
public class AuthHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Mono<ServerResponse> token(ServerRequest request) {

        return request.bodyToMono(JwtAuthenticationRequest.class)
                .flatMap(requestWrapper -> userRepository.findByUsername(requestWrapper.getUsername()))
                .flatMap(user ->
                        ServerResponse.ok()
                        .contentType(APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(new JwtAuthenticationResponse(jwtTokenUtil.generateToken(user), user.getUsername())))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> register(ServerRequest request){

        Mono<NewUserDTO> userMono = request.bodyToMono(NewUserDTO.class);

        return userMono.flatMap(user ->
                ServerResponse.status(HttpStatus.CREATED)
                .contentType(APPLICATION_JSON)
                .body(userService.createUserFromForm(user), User.class));
    }
}
