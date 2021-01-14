package se.rydberg.handla.lists;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class ShopListService {
    private final ShopListRepository shopListRepository;

    public ShopListService(ShopListRepository shopListRepository) {
        this.shopListRepository = shopListRepository;
    }

    public List<ShopList> getAllLists() {
        return shopListRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
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
}
