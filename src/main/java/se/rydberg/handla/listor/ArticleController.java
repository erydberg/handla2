package se.rydberg.handla.listor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/article")
public class ArticleController {
    private final ShopListService shopListService;
    private final ArticleService articleService;

    public ArticleController(ShopListService shopListService, ArticleService articleService) {
        this.shopListService = shopListService;
        this.articleService = articleService;
    }

    @PostMapping("/addtolist/{id}")
    public String addToShoplist(@Valid Article article, @PathVariable String id, BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {
        ShopList shoplist = shopListService.getShopList(Integer.parseInt(id));
        if(shoplist==null){
            //TODO felmeddelande tillbaka till listan
        }
        article.setShopList(shoplist);
        articleService.save(article);
        return "redirect:/lists/view/" + id;
    }
}
