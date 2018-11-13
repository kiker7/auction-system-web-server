package pl.edu.pw.ee.rutynar.auctionsystem.dtos.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import pl.edu.pw.ee.rutynar.auctionsystem.validators.NonExistingLogin;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class NewUserDTO {

    @EqualsAndHashCode.Include
    @NotBlank
    @NonExistingLogin
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Length(min = 6)
    private String password;
}
