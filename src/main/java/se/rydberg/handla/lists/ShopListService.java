package se.rydberg.handla.lists;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

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
