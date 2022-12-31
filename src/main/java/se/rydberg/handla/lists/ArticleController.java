package se.rydberg.handla.lists;

import org.owasp.encoder.Encode;
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

import jakarta.validation.Valid;
import java.util.Iterator;
import java.util.Optional;

@Controller
@RequestMapping("/article")
public class ArticleController {
    private final ShopListService shopListService;
    private final ArticleService articleService;
    private final CategoryHintService categoryHintService;

    public ArticleController(ShopListService shopListService, ArticleService articleService, CategoryHintService categoryHintService) {
        this.shopListService = shopListService;
        this.articleService = articleService;
        this.categoryHintService = categoryHintService;
    }


    @PostMapping("/addtolist/{id}")
    public String addToShoplist(@Valid ArticleDTO articleDto, BindingResult bindingResult, @PathVariable String id,
                                Model model, RedirectAttributes redirectAttributes) {
        int listId = Integer.parseInt(Encode.forJava(id));
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("error_message", "Skriv in något att handla");
            return "redirect:/lists/view/" + listId;
        }
        articleDto.setTitle(Encode.forHtml(articleDto.getTitle()));
        ShopList shopEntity = shopListService.getShopEntity(listId);
        if (shopEntity == null) {
            redirectAttributes.addAttribute("error_message", "Kan inte hitta listan i systemet");
            return "error/general_error";
        }
        //testar om artikeln är ny och det ska sättas en kategori
        if (articleDto.getId() == null && shopEntity.isUseCategory()) {
            if(articleDto.getCategory()!=null){
                categoryHintService.findOrCreateHint(articleDto.getTitle(), articleDto.getCategory());
            }
            if(articleDto.getCategory()==null){
                //leta efter en kategori
                Optional<Category> foundCategory = categoryHintService.findCategoryFor(articleDto.getTitle());
                if(foundCategory.isPresent()){
                    articleDto.setCategory(foundCategory.get());
                }else{
                    System.out.println("Ingen kategori hittad för " + articleDto);
                    redirectAttributes.addFlashAttribute("articleToCategorize", articleDto);
                    redirectAttributes.addFlashAttribute("error_message", "Sortera mera - välj en kategori!");
                    return "redirect:/lists/view/" + listId;
                }
            }
        }

        if (articleDto.getId() != null && shopEntity.isUseCategory() && articleDto.getCategory() == null) {
            ArticleDTO backendArticle = articleService.getArticleById(articleDto.getId());
            articleDto.setCategory(backendArticle.getCategory());
        }

        articleDto.setShopList(shopEntity);
        articleService.save(articleDto);

        return "redirect:/lists/view/" + listId;
    }

    // markera köpt/oköpt via restanrop
    @PutMapping(value = "/markbought/{id}/{bought}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markBoughtStatus(@PathVariable String id, @PathVariable String bought) {
        try {
            ArticleDTO article = articleService.getArticleById(Integer.parseInt(id));
            article.setBought(Boolean.parseBoolean(bought));
            articleService.save(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/delete/{id}/from/{shoplistid}")
    public String deleteArticle(@PathVariable String id, @PathVariable String shoplistid) {
        ShopListDTO shopListDTO;
        // hitta rätt artikel i shoplist
        int listId = Integer.parseInt(Encode.forJava(shoplistid));
        int articleId = Integer.parseInt(Encode.forJava(id));
        try {
            shopListDTO = shopListService.getShopListById(listId);
            Iterator<Article> itt = shopListDTO.getArticles().iterator();
            while (itt.hasNext()) {
                Article a = itt.next();
                if (a.getId().equals(articleId)) {
                    itt.remove();
                    shopListService.save(shopListDTO);
                    break;
                }
            }
            return "redirect:/lists/view/" + listId;

        } catch (Exception e) {
            // Lägga på felmeddelande
            System.out.println("verkar hamna fel när vi tar bort en artikel från en lista");
            return "redirect:/";
        }
    }
}
