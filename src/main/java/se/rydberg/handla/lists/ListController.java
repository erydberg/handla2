package se.rydberg.handla.lists;

import jakarta.validation.Valid;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("lists")
public class ListController {
    private final ShopListService shopListService;
    private final CategoryService categoryService;

    public ListController(ShopListService shopListService, CategoryService categoryService) {
        this.shopListService = shopListService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String start(Model model) {
        model.addAttribute("shoplists", shopListService.getAllLists());
        return "lists/list-start";
    }

    @PostMapping("/save")
    public String save(@Valid ShopListDTO shopListDTO, BindingResult bindingResult, Model model,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error_message", "Har du fyllt i allt du behöver?");
            model.addAttribute("shoplist", shopListDTO);
            return "lists/list-edit";
        } else {
            Integer savedListId;
            if (shopListDTO.getId() != null) {
                ShopListDTO backendList = shopListService.getShopListById(shopListDTO.getId());
                backendList.setTitle(shopListDTO.getTitle());
                backendList.setUseCategory(shopListDTO.isUseCategory());
                ShopList updatedShopList = shopListService.save(backendList);
                savedListId = updatedShopList.getId();
                redirectAttributes.addFlashAttribute("message", "Listan är uppdaterad");
            } else {
                ShopList newShopList = shopListService.save(shopListDTO);
                savedListId = newShopList.getId();
                redirectAttributes.addFlashAttribute("message", "Listan är skapad");
            }
            if (savedListId > 0) {
                return "redirect:/lists/view/" + savedListId;
            } else {
                return "redirect:/lists/";
            }
        }
    }

    @GetMapping("/new")
    public String createNew(Model model) {
        ShopList shopList = new ShopList();
        model.addAttribute("shoplist", shopList);
        return "lists/list-edit";
    }

    @GetMapping("/edit/{id}")
    public String editList(Model model, @PathVariable String id) {
        ShopListDTO shopList = shopListService.getShopListById(Integer.parseInt(id));
        model.addAttribute("shoplist", shopList);
        return "lists/list-edit";
    }

    @GetMapping("/view/{id}")
    public String viewShoplist(Model model, @PathVariable String id) {
        ShopList shopList = shopListService.getShopListWithArticlesSortedByCategory(Integer.parseInt(id));

        if (shopList == null) {
            model.addAttribute("error_message", "Listan finns inte i systemet");
            return "error/general_error";
        }

        model.addAttribute("shoplist", shopList);
        ArticleDTO article;
        ArticleDTO articleToCategorize = (ArticleDTO) model.getAttribute("articleToCategorize");
        article = Objects.requireNonNullElseGet(articleToCategorize, ArticleDTO::new);

        model.addAttribute("article", article);

        if (shopList.isUseCategory()) {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
        }
        return "lists/shoplist";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, @RequestParam(required = false) String returnview) {
        shopListService.delete(Integer.parseInt(id));
        String returnViewEncoded = Encode.forHtml(returnview);
        if (ReturnViewValidator.validate(returnViewEncoded)) {
            return "redirect:" + returnViewEncoded;
        } else {
            return "redirect:/lists";
        }
    }

    @RequestMapping(value = "/deleteboughtarticlesonlist/{id}", method = RequestMethod.GET)
    public String deleteBoughtArticlesFromListWithId(@PathVariable String id) {
        int listId = Integer.parseInt(Encode.forJava(id));
        shopListService.deleteBoughtArticlesFromShopListWithId(listId);
        return "redirect:/lists/view/" + listId;
    }
}
