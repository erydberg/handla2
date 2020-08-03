package se.rydberg.handla.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu save(Menu menu) {
        return menuRepository.save(menu);
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
}
