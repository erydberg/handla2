package se.rydberg.handla.lists;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ShopListService {
    private final ShopListRepository shopListRepository;
    private final ModelMapper modelMapper;

    public ShopListService(ShopListRepository shopListRepository, ModelMapper modelMapper) {
        this.shopListRepository = shopListRepository;
        this.modelMapper = modelMapper;
    }

    public List<ShopListDTO> getAllLists() {
        return shopListRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
                .stream()
                .map((s -> modelMapper.map(s, ShopListDTO.class)))
                .collect(toList());
    }

    public ShopList save(ShopList shopList) {
        return shopListRepository.save(shopList);
    }

    public ShopList getShopList(Integer id) {
        return shopListRepository.getOne(id);
    }

    public ShopList getShopListWithArticles(Integer id) {
        return shopListRepository.getShopListWithArticles(id);
    }

    public ShopList getShopListWithArticlesSortedByCategory(Integer id) {
        ShopList shopList = shopListRepository.getShopListWithArticles(id);
        return sortByCategory(shopList);
    }

    public void delete(Integer id) {
        shopListRepository.deleteById(id);
    }

    public ShopList getShopListById(Integer id) {
        return shopListRepository.getOne(id);
    }

    public void deleteBoughtArticlesFromShopListWithId(Integer id) {
        ShopList shoplist;
        shoplist = getShopListById(id);
        Iterator<Article> itt = shoplist.getArticles().iterator();
        while (itt.hasNext()) {
            Article article = itt.next();
            if (article.isBought()) {
                itt.remove();
            }
        }
        save(shoplist);
    }

    ShopList sortByCategory(ShopList shopList) {
        Set<Article> toSort = shopList.getArticles();
        Comparator<Article> compareArticleCategory = Comparator
                .comparing(Article::getCategory, Comparator.nullsLast(Comparator.comparingInt(Category::getSortOrder)));
        Comparator<Article> boughtLast = Comparator.comparing(Article::isBought);
        shopList.setArticles(
                toSort.stream()
                        .sorted(boughtLast.thenComparing(compareArticleCategory))
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
        return shopList;
    }
}
