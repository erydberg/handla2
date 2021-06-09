package se.rydberg.handla.lists;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
    public String save(@Valid ShopList shopList, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error_message", "Har du fyllt i allt du behöver?");
            model.addAttribute("shoplist", shopList);
            return "lists/list-edit";
        } else {
            redirectAttributes.addFlashAttribute("message", "Listan är skapad");
            ShopList savedShoplist = shopListService.save(shopList);
            return "redirect:/lists/view/" + savedShoplist.getId();
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
        ShopList shopList = shopListService.getShopList(Integer.parseInt(id));
        model.addAttribute("shoplist", shopList);
        return "lists/list-edit";
    }

    @GetMapping("/view/{id}")
    public String viewShoplist(Model model, @PathVariable String id) {
        ShopList shopList = shopListService.getShopListWithArticlesSortedByCategory(Integer.parseInt(id));
        model.addAttribute("shoplist", shopList);
        Article article = new Article();
        model.addAttribute("article", article);

        if(shopList.isUseCategory()) {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
        }
        return "lists/shoplist";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, @RequestParam(required = false) String returnview) {
        shopListService.delete(Integer.parseInt(id));
        if (isNotEmpty(returnview)) {
            return "redirect:" + returnview;
        } else {
            return "redirect:/lists";
        }
    }

    @RequestMapping(value = "/deleteboughtarticlesonlist/{id}", method = RequestMethod.GET)
    public String deleteBoughtArticlesFromListWithId(@PathVariable String id) {
        shopListService.deleteBoughtArticlesFromShopListWithId(Integer.parseInt(id));
        return "redirect:/lists/view/" + id;
    }
}
