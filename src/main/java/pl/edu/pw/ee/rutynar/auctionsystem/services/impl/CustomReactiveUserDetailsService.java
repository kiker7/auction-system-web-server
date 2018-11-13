package pl.edu.pw.ee.rutynar.auctionsystem.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;
import reactor.core.publisher.Mono;

@Service
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        Mono<UserDetails> data = userRepository.findByUsername(s);
        return data;
    }
}
