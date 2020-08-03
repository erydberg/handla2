package se.rydberg.handla.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("SELECT m from Menu m WHERE m.dayToEat is null")
    List<Menu> getAllUnplanned();

    @Query("SELECT m from Menu m WHERE m.grade = 'EAT_AGAIN' or m.grade = 'FANTASTIC'")
    List<Menu> getAllFavorites();
}
