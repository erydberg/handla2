package se.rydberg.handla;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.rydberg.handla.menu.MenuService;
import se.rydberg.handla.menu.TestLoader;
import se.rydberg.handla.security.Role;
import se.rydberg.handla.security.RoleRepository;
import se.rydberg.handla.security.User;
import se.rydberg.handla.security.UserRepository;

@Controller
@RequestMapping("/")
public class StartController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private MenuService menuService;

    public StartController(UserRepository userRepository, RoleRepository roleRepository, MenuService menuService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.menuService = menuService;
    }

    @GetMapping("")
    public String start(){
        return "start";
    }

    @GetMapping("/generateusers")
    public String generateUsers(){

        TestLoader testLoader = new TestLoader(menuService);
        testLoader.loadMenus();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String losen = "{bcrypt}" + bCryptPasswordEncoder.encode("losen");
        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);

        User user = User.builder().username("erik").password(losen).enabled(true).build();
        user.addRole(userRole);
        userRepository.save(user);


        return "klart";
    }
}
