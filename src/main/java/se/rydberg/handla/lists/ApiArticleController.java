package se.rydberg.handla.lists;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.owasp.encoder.Encode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * JSON-API för genvägar (Siri/Apple Watch). Skyddas av API-nyckel, se SecurityConfiguration.
 */
@RestController
@RequestMapping("/api")
public class ApiArticleController {
    private final ShopListService shopListService;
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final CategoryHintService categoryHintService;

    public ApiArticleController(ShopListService shopListService, ArticleService articleService,
                                CategoryService categoryService, CategoryHintService categoryHintService) {
        this.shopListService = shopListService;
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.categoryHintService = categoryHintService;
    }

    public record ListInfo(Integer id, String title) {
    }

    public record CategoryInfo(Integer id, String title) {
    }

    public record AddArticleRequest(@NotBlank String text) {
    }

    public record SetCategoryRequest(@NotNull Integer categoryId) {
    }

    public record ArticleResponse(Integer id, String title, String list, String category,
                                  boolean needsCategory, List<CategoryInfo> categories) {
    }

    @GetMapping("/lists")
    public List<ListInfo> lists() {
        return shopListService.getAllLists().stream()
                .map(list -> new ListInfo(list.getId(), list.getTitle()))
                .toList();
    }

    @PostMapping("/lists/{listId}/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse addArticle(@PathVariable("listId") Integer listId, @Valid @RequestBody AddArticleRequest request) {
        String title = Sanitize.setCapitalFirstLetter(Encode.forHtml(VoiceText.toArticleTitle(request.text())));
        if (title.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Säg något att handla");
        }
        ShopList shopList = shopListService.findById(listId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kan inte hitta listan"));

        Category category = null;
        if (shopList.isUseCategory()) {
            category = categoryHintService.findCategoryFor(title).orElse(null);
        }
        // sparas alltid, även utan kategori, så att inget tappas bort om man inte väljer kategori
        Article saved = articleService.save(ArticleDTO.builder().title(title).shopList(shopList).category(category).build());
        return toResponse(saved, shopList);
    }

    @PutMapping("/articles/{id}/category")
    public ArticleResponse setCategory(@PathVariable("id") Integer id, @Valid @RequestBody SetCategoryRequest request) {
        Article article = articleService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kan inte hitta artikeln"));
        Category category = categoryService.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kan inte hitta kategorin"));
        article.setCategory(category);
        categoryHintService.findOrCreateHint(article.getTitle(), category);
        Article saved = articleService.saveEntity(article);
        return toResponse(saved, saved.getShopList());
    }

    private ArticleResponse toResponse(Article article, ShopList shopList) {
        boolean needsCategory = shopList.isUseCategory() && article.getCategory() == null;
        List<CategoryInfo> categories = needsCategory
                ? categoryService.getAllCategories().stream().map(c -> new CategoryInfo(c.getId(), c.getTitle())).toList()
                : List.of();
        return new ArticleResponse(article.getId(), article.getTitle(), shopList.getTitle(),
                article.getCategory() == null ? null : article.getCategory().getTitle(),
                needsCategory, categories);
    }
}
