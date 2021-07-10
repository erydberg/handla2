package se.rydberg.handla.lists;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public ShopList save(ShopListDTO shopListDTO) {
        var shopListEntity = toEntity(shopListDTO);
        return shopListRepository.save(shopListEntity);
    }

    private ShopList saveEntity(ShopList shopList){
        return shopListRepository.save(shopList);
    }


    public ShopList getShopEntity(Integer id) {
        return shopListRepository.getOne(id);
    }


    public ShopList getShopListWithArticles(Integer id) {
        return shopListRepository.getShopListWithArticles(id);
    }

    public ShopList getShopListWithArticlesSortedByCategory(Integer id) {
        var shopList = shopListRepository.getShopListWithArticles(id);
        if(shopList!=null) {
            return sortByCategory(shopList);
        }else{
            return null;
        }
    }

    public void delete(Integer id) {
        shopListRepository.deleteById(id);
    }

    public ShopListDTO getShopListById(Integer id) {
        ShopList shopListEntity = shopListRepository.getOne(id);
        return toDto(shopListEntity);
    }

    private ShopList getShopListEntityById(Integer id){
        return shopListRepository.getOne(id);
    }

    public void deleteBoughtArticlesFromShopListWithId(Integer id) {
        ShopList shoplist;
        shoplist = getShopListEntityById(id);
        Iterator<Article> itt = shoplist.getArticles().iterator();
        while (itt.hasNext()) {
            var article = itt.next();
            if (article.isBought()) {
                itt.remove();
            }
        }
        saveEntity(shoplist);
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

    private ShopList toEntity(ShopListDTO shopListDTO) {
        if(shopListDTO !=null){
            return modelMapper.map(shopListDTO, ShopList.class);
        }else{
            return null;
        }
    }

    private ShopListDTO toDto(ShopList shopList){
        if(shopList != null){
            return modelMapper.map(shopList, ShopListDTO.class);
        }else{
            return null;
        }
    }
}
