package se.rydberg.handla;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.rydberg.handla.listor.ArticleService;
import se.rydberg.handla.listor.ShopListService;
import se.rydberg.handla.menu.MenuService;
import se.rydberg.handla.menu.TestLoader;
import se.rydberg.handla.security.Role;
import se.rydberg.handla.security.RoleRepository;
import se.rydberg.handla.security.User;
import se.rydberg.handla.security.UserRepository;

@Controller
@RequestMapping("/")
public class StartController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    //TODO ta bort när vi inte genererar något testdata
    private final MenuService menuService;
    private final ArticleService articleService;
    private final ShopListService shopListService;

    public StartController(UserRepository userRepository, RoleRepository roleRepository, MenuService menuService,
            ArticleService articleService, ShopListService shopListService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.menuService = menuService;
        this.articleService = articleService;
        this.shopListService = shopListService;
    }

    @GetMapping("")
    public String start(){
        return "start";
    }

    //För utveckling
    @GetMapping("/generateusers")
    public String generateUsers(){

        TestLoader testLoader = new TestLoader(menuService, articleService, shopListService);
        testLoader.loadMenus();
        testLoader.loadLists();



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
