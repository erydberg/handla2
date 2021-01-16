package se.rydberg.handla.lists;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Iterator;

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
        if (shoplist == null) {
            // TODO felmeddelande tillbaka till listan
        }
        article.setShopList(shoplist);
        articleService.save(article);
        return "redirect:/lists/view/" + id;
    }

    // markera köpt/oköpt via restanrop
    @PutMapping(value = "/markbought/{id}/{bought}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markBoughtStatus(@PathVariable String id, @PathVariable String bought) {
        try {
            Article article = articleService.getArticleById(Integer.parseInt(id));
            article.setBought(Boolean.parseBoolean(bought));
            articleService.save(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/delete/{id}/from/{shoplistid}")
    public String deleteArticle(@PathVariable String id, @PathVariable String shoplistid) {
        ShopList shopList = null;
        // hitta rätt artikel i shoplist
        try {
            shopList = shopListService.getShopListById(Integer.parseInt(shoplistid));
            Iterator<Article> itt = shopList.getArticles().iterator();
            while (itt.hasNext()) {
                Article a = itt.next();
                if (a.getId().equals(Integer.parseInt(id))) {
                    itt.remove();
                    shopListService.save(shopList);
                    break;
                }
            }
            String redirectUrl = "/lists/view/" + shoplistid;
            return "redirect:" + redirectUrl;

        } catch (Exception e) {
            // Lägga på felmeddelande
            System.out.println("verkar hamna fel när vi tar bort en artikel från en lista");
            return "redirect:/";
        }
    }
}
