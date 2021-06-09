package se.rydberg.handla.lists;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.LinkedHashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ShopListServiceTest {
    @Mock
    ShopListRepository shopListRepository;

    @Mock
    ModelMapper modelMapper;

    ShopListService shopListService;



    @Test
    public void shouldSort(){
        shopListService = new ShopListService(shopListRepository, modelMapper);
        ShopList shopList = createShopList();
        ShopList newShopList = shopListService.sortByCategory(shopList);
        newShopList.getArticles().stream().forEach(article -> System.out.println(article.getTitle() + article.isBought()));

    }

    private ShopList createShopList() {
        Category cat1 = Category.builder().title("Frukt").sortOrder(1).build();
        Category cat2 = Category.builder().title("Annat").sortOrder(2).build();
        Article article1 = Article.builder().title("Banan Frukt").category(cat1).bought(true).build();
        Article article2 = Article.builder().title("Banan Annat").category(cat2).build();
        Article article3 = Article.builder().title("Äpple Frukt").category(cat1).build();
        Article article4 = Article.builder().title("Äpple Annat").category(cat2).build();
        Article article5 = Article.builder().title("Övrigt").bought(true).build();
        Article article6 = Article.builder().title("Övrigt 2").build();
        Article article7 = Article.builder().title("skrutt Frukt").category(cat1).build();
        Set<Article> articles = new LinkedHashSet<>();
        articles.add(article1);
        articles.add(article2);
        articles.add(article3);
        articles.add(article4);
        articles.add(article5);
        articles.add(article6);
        articles.add(article7);

        return ShopList.builder().title("Testlista").useCategory(true).articles(articles).build();
    }
}
