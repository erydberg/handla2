package se.rydberg.handla.menu;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("")
    public String start() {
        return "menu-start";
    }

    @GetMapping("/planned")
    public String plannedMenu(Model model) {
        // Test

        //model.addAttribute("menus", menuService.getAllByDate());
        model.addAttribute("menus", menuService.getAll());
        return "menu-list";
    }


    @GetMapping("/detail/{id}")
    public String viewDetail(Model model, @PathVariable String id) {
        Menu menu = menuService.getMenu(Integer.parseInt(id));
        model.addAttribute("menu", menu);
        return "menu-detail";
    }

    @GetMapping("/edit/{id}")
    public String editMenu(Model model, @PathVariable String id) {
        Menu menu = menuService.getMenu(Integer.parseInt(id));
        model.addAttribute("menu", menu);
        return "menu-edit";
    }

    @GetMapping("/new")
    public String createNew(Model model){
        Menu menu = new Menu();
        model.addAttribute("menu", menu);
        return "menu-edit";
    }

    //TODO dto-convertering https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
    @PostMapping("/save")
    public String save(@Valid Menu menu, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error_message","Har du skrivit in allt du behöver?");
            model.addAttribute("menu", menu);
            return "menu-edit";
        } else {
            redirectAttributes.addFlashAttribute("message","Maträtten är sparad");
            menuService.save(menu);
        }
        return "redirect:/menu/detail/" + menu.getId();
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable String id) {
        menuService.delete(Integer.parseInt(id));
        return "redirect:/menu/list";
    }
}
