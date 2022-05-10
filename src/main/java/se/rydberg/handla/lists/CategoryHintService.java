package se.rydberg.handla.lists;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryHintService {
    private final CategoryHintRepository categoryHintRepository;

    public CategoryHintService(CategoryHintRepository categoryHintRepository) {
        this.categoryHintRepository = categoryHintRepository;
    }

    public CategoryHint save(CategoryHint categoryHint) {
        return categoryHintRepository.save(categoryHint);
    }

    public CategoryHint find(String name) {
        return categoryHintRepository.findByName(name);
    }

    public Optional<Category> findCategoryFor(String name) {
        CategoryHint hint = find(name);
        if (hint != null) {
            return Optional.ofNullable(hint.getCategory());
        } else {
            return Optional.empty();
        }
    }

    public void findOrCreateHint(String title, Category category) {
        Optional<Category> firstHint = findCategoryFor(title);
        if (firstHint.isPresent() && !firstHint.get().getTitle().equalsIgnoreCase(category.getTitle())) {
            //knepigt, har en artikel som anses vara en annan kategori? Vad göra
            System.out.println(String.format("En artikel med titel %s som enligt databasen är mappad " +
                    "till kategorin %s men är markerad i guit med kategori %s Vad göra?", title, firstHint.get().getTitle(), category.getTitle()));
        }
        if (firstHint.isEmpty()) {
            CategoryHint newHint = CategoryHint.builder().name(title).category(category).build();
            categoryHintRepository.save(newHint);
        }
    }
}
