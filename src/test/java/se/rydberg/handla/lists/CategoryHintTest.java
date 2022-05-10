package se.rydberg.handla.lists;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import se.rydberg.handla.Handla2Application;

import java.util.Optional;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Handla2Application.class)
class CategoryHintTest {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryHintService categoryHintService;

    private Category categoryFruit;
    private Category categoryBread;


    @BeforeAll
    void setup() {
        createTestCategories();
        createTestHints();
    }

    @Test
    void shouldMapInMemoryCategoryWithExistingMapping() {
        Article banana = Article.builder().title("bananer").build();
        Optional<Category> foundCategory = categoryHintService.findCategoryFor(banana.getTitle());
        assertThat(foundCategory.isPresent());
        assertThat(foundCategory.get().getTitle()).isEqualTo("Frukt");
    }

    private void createTestCategories() {
        categoryFruit = Category.builder().title("frukt").build();
        categoryBread = Category.builder().title("bröd").build();
        categoryService.saveEntity(categoryFruit);
        categoryService.saveEntity(categoryBread);
    }

    private void createTestHints() {
        CategoryHint categoryHintBanana = CategoryHint.builder().name("bananer").category(categoryFruit).build();
        categoryHintService.save(categoryHintBanana);
    }
}
