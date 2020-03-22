package se.rydberg.handla.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Integer> {


    //här kan jag lägga till @Query för att göra specialgrejer som inte finns i standard, kan köra nativeQuery=true också

    //@Query()

}
