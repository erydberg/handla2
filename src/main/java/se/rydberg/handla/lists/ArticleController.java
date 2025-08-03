package se.rydberg.handla.lists;

import jakarta.validation.Valid;
import org.owasp.encoder.Encode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/article")
public class ArticleController {
    public static final String ERROR_MESSAGE = "error_message";
    public static final String REDIRECT_LISTS_VIEW = "redirect:/lists/view/";
    private final ShopListService shopListService;
    private final ArticleService articleService;
    private final CategoryHintService categoryHintService;

    public ArticleController(ShopListService shopListService, ArticleService articleService, CategoryHintService categoryHintService) {
        this.shopListService = shopListService;
        this.articleService = articleService;
        this.categoryHintService = categoryHintService;
    }


    @PostMapping("/addtolist/{id}")
    public String addToShoplist(@Valid ArticleDTO articleDto, BindingResult bindingResult, @PathVariable("id") String id,
                                Model model, RedirectAttributes redirectAttributes) {
        int listId = Integer.parseInt(Encode.forJava(id));
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Skriv in något att handla");
            return REDIRECT_LISTS_VIEW + listId;
        }
        articleDto.setTitle(Encode.forHtml(articleDto.getTitle()));
        ShopList shopEntity = shopListService.getShopEntity(listId);
        if (shopEntity == null) {
            redirectAttributes.addAttribute(ERROR_MESSAGE, "Kan inte hitta listan i systemet");
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
                    redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Sortera mera - välj en kategori!");
                    return REDIRECT_LISTS_VIEW + listId;
                }
            }
        }

        if (articleDto.getId() != null && shopEntity.isUseCategory() && articleDto.getCategory() == null) {
            ArticleDTO backendArticle = articleService.getArticleById(articleDto.getId());
            articleDto.setCategory(backendArticle.getCategory());
        }

        articleDto.setShopList(shopEntity);
        articleService.save(articleDto);

        return REDIRECT_LISTS_VIEW + listId;
    }

    // markera köpt/oköpt via restanrop
    @PutMapping(value = "/markbought/{id}/{bought}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markBoughtStatus(@PathVariable("id") String id, @PathVariable("bought") String bought) {
        try {
            ArticleDTO article = articleService.getArticleById(Integer.parseInt(id));
            article.setBought(Boolean.parseBoolean(bought));
            articleService.save(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/delete/{id}/from/{shoplistid}")
    public String deleteArticle(@PathVariable("id") String id, @PathVariable("shoplistid") String shoplistid) {
        ShopListDTO shopListDTO;
        // hitta rätt artikel i shoplist
        int listId = Integer.parseInt(Encode.forJava(shoplistid));
        int articleId = Integer.parseInt(Encode.forJava(id));
        try {
            shopListDTO = shopListService.getShopListById(listId);
            shopListDTO.getArticles().removeIf(article -> article.getId().equals(articleId));
            shopListService.save(shopListDTO);

            /* OLD version, new using lambda
            Iterator<Article> itt = shopListDTO.getArticles().iterator();
            while (itt.hasNext()) {
                Article a = itt.next();
                if (a.getId().equals(articleId)) {
                    itt.remove();
                    shopListService.save(shopListDTO);
                    break;
                }
            }

             */
            return REDIRECT_LISTS_VIEW + listId;

        } catch (Exception e) {
            // Lägga på felmeddelande
            System.out.println("verkar hamna fel när vi tar bort en artikel från en lista");
            return "redirect:/";
        }
    }
}
