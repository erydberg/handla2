package se.rydberg.handla.listor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopListRepository extends JpaRepository<ShopList, Integer> {

    @Query("SELECT s FROM ShopList s left JOIN FETCH s.articles WHERE s.id = (:id)")
    ShopList getShopListWithArticles(@Param("id") Integer id);
}
