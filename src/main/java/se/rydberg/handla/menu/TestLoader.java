package se.rydberg.handla.menu;


import se.rydberg.handla.lists.Article;
import se.rydberg.handla.lists.ArticleService;
import se.rydberg.handla.lists.ShopList;
import se.rydberg.handla.lists.ShopListService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestLoader {

    private final MenuService menuService;
    private final ArticleService articleService;
    private final ShopListService shopListService;

    public TestLoader(MenuService menuService, ArticleService articleService, ShopListService shopListService){
        this.menuService = menuService;
        this.articleService = articleService;
        this.shopListService = shopListService;
    }

    public void loadMenus() {

        List<Menu> menus = new ArrayList<>();
        Menu menu1 = Menu.builder().title("Spagetti och köttfärssås").dayToEat(LocalDate.parse("2020-05-05")).grade(Grade.EAT_AGAIN).build();
        Menu menu2 = Menu.builder()
                .title("Stekt lax med dillstuvad potatis")
                .dayToEat(LocalDate.parse("2020-05-06"))
                .grade(Grade.EAT_AGAIN)
                .build();
        Menu menu3 = Menu.builder().title("Fiskbullar och potatis").dayToEat(LocalDate.parse("2020-05-07"))
                .grade(Grade.FANTASTIC).build();
        Menu menu4 = Menu.builder().title("Grillat").dayToEat(LocalDate.parse("2020-05-08")).grade(Grade.UN_GRADED).build();
        Menu menu5 = Menu.builder().title("Tonfisksallad").dayToEat(LocalDate.parse("2020-05-09")).grade(Grade.EAT_AGAIN).build();
        Menu menu6 = Menu.builder()
                .title("Sous vide-hanterad kyckling med bulgursallad")
                .dayToEat(LocalDate.parse("2020-05-10"))
                .grade(Grade.NEVER_EAT_AGAIN)
                .build();
        menus.add(menu1);
        menus.add(menu2);
        menus.add(menu3);
        menus.add(menu4);
        menus.add(menu5);
        menus.add(menu6);

        for(Menu menu:menus){
            menuService.saveEntity(menu);
        }
    }
}
