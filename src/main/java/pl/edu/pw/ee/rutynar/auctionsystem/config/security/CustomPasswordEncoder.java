package pl.edu.pw.ee.rutynar.auctionsystem.config.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;

public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);

        return BCrypt.hashpw(charSequence.toString(), BCrypt.gensalt(14, random));
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {

        String decodedString = new String(Base64.getDecoder().decode(charSequence.toString()));
        return BCrypt.checkpw(decodedString, s);
    }
}
