package se.rydberg.handla.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    @NotEmpty(message = "Fyll i användarnamn")
    private String username;
    @NotEmpty(message = "Fyll i ett lösenord")
    private String password;
    private boolean enabled;
}
