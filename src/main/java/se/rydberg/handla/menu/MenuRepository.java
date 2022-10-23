package se.rydberg.handla.menu;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("SELECT m from Menu m WHERE m.dayToEat >=:date order by m.dayToEat asc")
    List<Menu> getAllPlanned(@Param("date")LocalDate date);

    @Query("SELECT m from Menu m WHERE m.dayToEat is null")
    List<Menu> getAllUnplanned();

    @Query("SELECT m from Menu m WHERE m.grade = 'EAT_AGAIN' or m.grade = 'FANTASTIC' order by m.title")
    List<Menu> getAllFavorites();

    @Query("SELECT m from Menu m WHERE m.grade = 'NEVER_EAT_AGAIN'")
    List<Menu> getAllNeverAgain(Sort title);

    @Query("SELECT m from Menu m WHERE m.dayToEat is not null order by m.dayToEat desc")
    List<Menu> getAllHistory();

    @Query("SELECT m from Menu m WHERE m.title LIKE %:searchTerm%")
    List<Menu> searchTitleAndDescription(@Param("searchTerm") String searchTerm);
}
