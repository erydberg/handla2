package se.rydberg.handla.lists;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public Category saveEntity(Category category){
        category.setTitle(Sanitize.setCapitalFirstLetter(category.getTitle()));
        return categoryRepository.save(category);
    }

    public Category save(CategoryDTO categoryDto) {
        Category categoryEntity = toEntity(categoryDto);
        return saveEntity(categoryEntity);
    }

    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDTO getCategoryById(Integer id) {
        Category category = categoryRepository.getById(id);
        return toDto(category);
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"))
                .stream()
                .map((s -> modelMapper.map(s, CategoryDTO.class)))
                .toList();

    }

    public int getNextSortOrder() {
        List<CategoryDTO> categories = getAllCategories();
        if (categories == null || categories.isEmpty()) {
            return 0;
        } else {
            CategoryDTO lastCategory = categories.get(categories.size() - 1);
            if (lastCategory != null) {
                return lastCategory.getSortOrder() + 1;
            } else {
                return 0;
            }
        }
    }

    private Category toEntity(CategoryDTO categoryDto) {
        if (categoryDto != null) {
            return modelMapper.map(categoryDto, Category.class);
        } else {
            return null;
        }
    }

    private CategoryDTO toDto(Category category) {
        if (category != null) {
            return modelMapper.map(category, CategoryDTO.class);
        } else {
            return null;
        }
    }
}
