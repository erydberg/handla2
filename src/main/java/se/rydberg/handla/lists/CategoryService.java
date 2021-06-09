package se.rydberg.handla.lists;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


import static java.util.stream.Collectors.*;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper){
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public Category save(CategoryDTO categoryDto){
        categoryDto.setTitle(Sanitize.setCapitalFirstLetter(categoryDto.getTitle()));
        Category categoryEntity = toEntity(categoryDto);
        return categoryRepository.save(categoryEntity);
    }


    public void delete(Integer id){
        categoryRepository.deleteById(id);
    }

    public CategoryDTO getCategoryById(Integer id){
        Category category = categoryRepository.getOne(id);
        return toDto(category);
    }

    private CategoryDTO toDto(Category category) {
        if(category!=null){
            return modelMapper.map(category, CategoryDTO.class);
        }else{
            return null;
        }

    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"))
                .stream()
                .map((s->modelMapper.map(s, CategoryDTO.class)))
                .collect(toList());
    }

    private Category toEntity(CategoryDTO categoryDto) {
        if(categoryDto!=null){
            return modelMapper.map(categoryDto, Category.class);
        }else{
            return null;
        }
    }
}
