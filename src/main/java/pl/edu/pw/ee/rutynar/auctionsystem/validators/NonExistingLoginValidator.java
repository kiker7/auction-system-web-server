package pl.edu.pw.ee.rutynar.auctionsystem.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pw.ee.rutynar.auctionsystem.data.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NonExistingLoginValidator implements ConstraintValidator<NonExistingLogin, String> {

    @NotNull
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {

        return !userRepository.findByLogin(login).blockOptional().isPresent();
    }

    @Override
    public void initialize(NonExistingLogin constraintAnnotation) {
    }
}
