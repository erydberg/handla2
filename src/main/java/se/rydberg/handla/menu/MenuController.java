package se.rydberg.handla.menu;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.rydberg.handla.image.ImageService;
import se.rydberg.handla.image.MenuImage;
import se.rydberg.handla.lists.ReturnViewValidator;

import javax.validation.Valid;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Controller
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;
    private final ImageService imageService;

    public MenuController(MenuService menuService, ImageService imageService) {
        this.menuService = menuService;
        this.imageService = imageService;
    }

    @GetMapping("")
    public String start() {
        return "menu/menu-start";
    }

    @GetMapping("/search/")
    public String searchMenuGET(Model model, @RequestParam String searchWord) {
        String search = Encode.forJava(searchWord);
        model.addAttribute("menus", menuService.search(search));
        model.addAttribute("searchWord", search);
        return "menu/menu-history";
    }

    @PostMapping("/search")
    public String searchMenu(@RequestParam("searchWord") String searchWord, Model model) {
        String search = Encode.forJava(searchWord);
        model.addAttribute("menus", menuService.search(search));
        model.addAttribute("searchWord", searchWord);
        return "menu/menu-history";


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
        MenuDTO menu = menuService.getMenu(Integer.parseInt(id));
        System.out.println("meny " + menu);

        // if(ArrayUtils.isNotEmpty(menu.getImage())){
        // System.out.println("bild: " + menu.getImage().length);
        // menu.setImageBase64(Base64.getEncoder().encodeToString(menu.getImage()));
        // }
        model.addAttribute("menu", menu);
        return "menu/menu-detail";
    }

    @GetMapping("/edit/{id}")
    public String editMenu(Model model, @PathVariable String id) {
        MenuDTO menu = menuService.getMenu(Integer.parseInt(id));
        model.addAttribute("menu", menu);
        return "menu/menu-edit";
    }

    @GetMapping("/new")
    public String createNew(Model model) {
        MenuDTO menu = new MenuDTO();
        model.addAttribute("menu", menu);
        return "menu/menu-edit";
    }

    @PostMapping("/save")
    public String save(@Valid MenuDTO menuDto, BindingResult bindingResult, @RequestParam("imageupload") MultipartFile multipartFile,
             Model model, RedirectAttributes redirectAttributes) {
        String id;
        if (bindingResult.hasErrors()) {
            model.addAttribute("error_message", "Har du skrivit in allt du behöver?");
            model.addAttribute("menu", menuDto);
            return "menu/menu-edit";
        } else {
            if (!multipartFile.isEmpty()) {
                try {
                    // hitta ett bättre sätt för bildhanteringen
                    if (isNotEmpty(menuDto.getImageId())) {
                        imageService.delete(menuDto.getImageId());
                    }
                    MenuImage menuImage = imageService.save(multipartFile);
                    menuDto.setImageId(menuImage.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            redirectAttributes.addFlashAttribute("message", "Maträtten är sparad");
            Menu savedMenu = menuService.save(menuDto);
            id = String.valueOf(savedMenu.getId());

        }
        return "redirect:/menu/detail/" + id;
    }

    private void handleFileUpload(MenuDTO menuDto) {

    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable String id, @RequestParam(required = false) String returnview) {
        menuService.delete(Integer.parseInt(id));
        String returnViewEncoded = Encode.forHtml(returnview);
        if (ReturnViewValidator.validate(returnViewEncoded)) {
            return "redirect:" + returnViewEncoded;
        } else {
            return "redirect:/menu";
        }
    }
}
