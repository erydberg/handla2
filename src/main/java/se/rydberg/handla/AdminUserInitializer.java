package se.rydberg.handla;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.rydberg.handla.security.UserDTO;
import se.rydberg.handla.security.UserService;


@Component
@RequiredArgsConstructor
public class AdminUserInitializer {

    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void ensureAdminUserExists() {
        System.out.println("Number of users in the system " + userService.count());
        if (userService.count() == 0) {
            System.out.println("No users found, creating a start user");
            UserDTO user = new UserDTO();
            user.setEnabled(true);
            user.setPassword("admin");
            user.setUsername("admin");
            userService.savenew(user);
            System.out.println("Default admin user created: username=admin, password=admin");
        } else {
            System.out.println("One or more users already in place, no default admin user created");
        }
    }
}

