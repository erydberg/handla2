package se.rydberg.handla.listor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopListRepository extends JpaRepository<ShopList, Integer> {
}
