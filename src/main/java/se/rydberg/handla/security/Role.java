package se.rydberg.handla.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
@NoArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
}
