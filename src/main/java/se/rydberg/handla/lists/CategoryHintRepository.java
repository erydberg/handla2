package se.rydberg.handla.lists;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryHintRepository extends JpaRepository<CategoryHint, Integer> {
    CategoryHint findByName(String name);
}
