package se.rydberg.handla.menu;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
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
        return "menu/menu-start";
    }

    @GetMapping("/planned")
    public String plannedMenu(Model model) {
        model.addAttribute("menus", menuService.getAllPlanned());
        return "menu/menu-planned";
    }

    @GetMapping("/unplanned")
    public String unplannedMenu(Model model) {
        model.addAttribute("menus", menuService.getAllUnplanned());
        return "menu/menu-unplanned";
    }

    @GetMapping("/favorites")
    public String favorites(Model model) {
        model.addAttribute("menus", menuService.getAllFavorites());
        return "menu/menu-favorites";
    }

    @GetMapping("/neveragain")
    public String neverAgain(Model model) {
        model.addAttribute("menus", menuService.getAllNeverAgain());
        return "menu/menu-neveragain";
    }

    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("menus", menuService.getAllHistory());
        return "menu/menu-history";
    }

    @GetMapping("/detail/{id}")
    public String viewDetail(Model model, @PathVariable String id) {
        Menu menu = menuService.getMenu(Integer.parseInt(id));
        model.addAttribute("menu", menu);
        return "menu/menu-detail";
    }

    @GetMapping("/edit/{id}")
    public String editMenu(Model model, @PathVariable String id) {
        Menu menu = menuService.getMenu(Integer.parseInt(id));
        model.addAttribute("menu", menu);
        return "menu/menu-edit";
    }

    @GetMapping("/new")
    public String createNew(Model model) {
        Menu menu = new Menu();
        model.addAttribute("menu", menu);
        return "menu/menu-edit";
    }

    // TODO dto-convertering https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
    @PostMapping("/save")
    public String save(@Valid Menu menu, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error_message", "Har du skrivit in allt du behöver?");
            model.addAttribute("menu", menu);
            return "menu/menu-edit";
        } else {
            redirectAttributes.addFlashAttribute("message", "Maträtten är sparad");
            menuService.save(menu);
        }
        return "redirect:/menu/detail/" + menu.getId();
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable String id, @RequestParam(required = false) String returnview) {
        menuService.delete(Integer.parseInt(id));
        if (isNotEmpty(returnview)){
            return "redirect:" + returnview;
        }else{
            return "redirect:/menu";
        }
    }
}
