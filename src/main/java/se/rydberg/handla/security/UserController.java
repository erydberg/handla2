package se.rydberg.handla.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String start(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "users/users-start";
    }

    @GetMapping("/new")
    public String newuser(Model model){
        UserDTO user = new UserDTO();
        model.addAttribute("user",user);
        return "users/user-edit";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") String id, Model model) {
        UserDTO user = userService.getUserBy(Long.parseLong(id));
        model.addAttribute("user",user);
        return "users/user-edit";
    }

    @PostMapping("/save")
    public String save(@Valid UserDTO userDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            model.addAttribute("error_message", "Inte allt klart för att spara användaren. Fyll i rätt uppgifter");
            model.addAttribute("user", userDto);
            return "users/user-edit";
        }else{
            HandlaUser savedHandlaUser = userService.savenew(userDto);
            redirectAttributes.addFlashAttribute("message", "Användare " + savedHandlaUser.getUsername() + " skapad.");
            return "redirect:/users";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id){
        userService.delete(Long.parseLong(id));
        return "redirect:/users";
    }
}
