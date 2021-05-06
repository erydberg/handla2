package se.rydberg.handla.menu;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.rydberg.handla.image.ImageRepository;

import java.time.LocalDate;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    public MenuService(MenuRepository menuRepository, ModelMapper modelMapper, ImageRepository imageRepository) {
        this.menuRepository = menuRepository;
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
    }

    public Menu save(MenuDTO menu) {
        Menu menuEntity = toEntity(menu);
        return menuRepository.save(menuEntity);
    }

    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Menu> getAllByDate() {
        return menuRepository.findAll(Sort.by(Sort.Direction.ASC, "dayToEat"));
    }

    public MenuDTO getMenu(Integer id) {
        Menu menu = menuRepository.getOne(id);
        return toDto(menu);
    }

    public void delete(Integer id) {
        MenuDTO menuToDelete = getMenu(Integer.valueOf(id));
        if(isNotEmpty(menuToDelete.getImageId())){
            imageRepository.deleteById(menuToDelete.getImageId());
        }
        menuRepository.deleteById(id);
    }

    public List<Menu> getAllPlanned() {
        return menuRepository.getAllPlanned(LocalDate.now());
    }

    public List<Menu> getAllUnplanned() {
        return menuRepository.getAllUnplanned();
    }

    public List<Menu> getAllFavorites() {
        return menuRepository.getAllFavorites();
    }

    public List<Menu> getAllNeverAgain() {
        return menuRepository.getAllNeverAgain(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Menu> getAllHistory() {
        // return menuRepository.findAll(Sort.by(Sort.Direction.DESC, "dayToEat"));
        return menuRepository.getAllHistory();
    }

    private Menu toEntity(MenuDTO menuDto) {
        if (menuDto != null) {
            return modelMapper.map(menuDto, Menu.class);
        } else {
            return null;
        }
    }

    private MenuDTO toDto(Menu menu) {
        if (menu != null) {
            return modelMapper.map(menu, MenuDTO.class);
        } else {
            return null;
        }
    }

    public Menu saveEntity(Menu menu) {
        return menuRepository.save(menu);
    }
}
