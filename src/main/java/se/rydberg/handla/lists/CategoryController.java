package se.rydberg.handla.lists;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String start(Model model){
        model.addAttribute("categories", categoryService.getAllCategories());
        return "lists/category-start";
    }

    @GetMapping("/new")
    public String createNew(Model model){
        CategoryDTO category = new CategoryDTO();
        model.addAttribute("category", category);
        return "lists/category-edit";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable String id){
        CategoryDTO category = categoryService.getCategoryById(Integer.parseInt(id));
        model.addAttribute("category", category);
        return "lists/category-edit";
    }

    @PostMapping("/save")
    public String save(@Valid CategoryDTO categoryDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            model.addAttribute("error_message","Det verkar finnas något konstigt i formuläret");
            model.addAttribute("category", categoryDTO);
            return "lists/category-edit";
        }else{
            categoryService.save(categoryDTO);
            model.addAttribute("message", "Kategorin sparad");
            return "redirect:/category";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id){
        //TODO fundera på relationen här,
        categoryService.delete(Integer.parseInt(id));
        return "redirect:/category";
    }
}
