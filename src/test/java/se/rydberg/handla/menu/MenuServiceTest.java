package se.rydberg.handla.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.rydberg.handla.Handla2Application;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Handla2Application.class)
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @AfterEach
    public void clearEntries() {
        menuService.deleteAll();
    }

    @Test
    public void shouldSaveMenu() {
        MenuDTO menu = MenuDTO.builder().title("Matförslag").description("Detta är en maträtt").build();
        menuService.save(menu);

        List<Menu> menyer = menuService.getAll();
        assertThat(menyer).hasSize(1);
    }


    @Test
    public void shouldSearchInTitleField() {
        MenuDTO menu = MenuDTO.builder().title("Detta är en god måltid med kyckling och potatis").description("gott").build();
        menuService.save(menu);

        MenuDTO menu2 = MenuDTO.builder().title("Matförslag").description("Detta är en maträtt").build();
        menuService.save(menu2);

        List<Menu> menyer = menuService.getAll();
        assertThat(menyer).hasSize(2);

        List<MenuDTO> potatisMenyer = menuService.search("potatis");
        System.out.println(potatisMenyer);
        assertThat(potatisMenyer).hasSize(1);

        List<MenuDTO> kycklingMenyer = menuService.search("kyckling");
        System.out.println(kycklingMenyer);
        assertThat(kycklingMenyer).hasSize(1);
        assertThat(kycklingMenyer.get(0).getTitle()).contains("kyckling");
    }

    @Test
    public void shouldFindInDescription() {
        MenuDTO menu = MenuDTO.builder().title("Detta är en god måltid med kyckling och potatis").description("Börja med att skala gurkan, ta sedan en tomat.").build();
        menuService.save(menu);

        MenuDTO menu2 = MenuDTO.builder().title("Matförslag").description("Detta är en maträtt").build();
        menuService.save(menu2);

        List<MenuDTO> menues = menuService.search("gurkan");
        assertThat(menues).hasSize(1);
        assertThat(menues.get(0).getDescription()).contains("gurkan");
    }

}
