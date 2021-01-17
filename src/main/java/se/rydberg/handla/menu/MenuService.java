package se.rydberg.handla.menu;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;


    public MenuService(MenuRepository menuRepository, ModelMapper modelMapper) {
        this.menuRepository = menuRepository;
        this.modelMapper = modelMapper;
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

    public Menu getMenu(Integer id) {
        return menuRepository.getOne(id);
    }

    public void delete(Integer id) {
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
        return menuRepository.getAllNeverAgain(Sort.by(Sort.Direction.ASC,"title"));
    }

    public List<Menu> getAllHistory() {
        return menuRepository.findAll(Sort.by(Sort.Direction.DESC, "dayToEat"));
    }

    private Menu toEntity(MenuDTO menuDto){
        if(menuDto!=null){
            return modelMapper.map(menuDto, Menu.class);
        }else{
            return null;
        }
    }

    public Menu saveEntity(Menu menu) {
        return menuRepository.save(menu);
    }
}
