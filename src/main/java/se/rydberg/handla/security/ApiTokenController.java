package se.rydberg.handla.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users/tokens")
public class ApiTokenController {
    private final ApiTokenService apiTokenService;

    public ApiTokenController(ApiTokenService apiTokenService) {
        this.apiTokenService = apiTokenService;
    }

    @GetMapping("")
    public String start(@AuthenticationPrincipal HandlaUser user, Model model) {
        model.addAttribute("tokens", apiTokenService.getTokensFor(user));
        return "users/tokens";
    }

    @PostMapping("/new")
    public String create(@AuthenticationPrincipal HandlaUser user, @RequestParam(value = "name", required = false) String name,
                         RedirectAttributes redirectAttributes) {
        String plain = apiTokenService.create(user, name);
        redirectAttributes.addFlashAttribute("newToken", plain);
        return "redirect:/users/tokens";
    }

    @PostMapping("/revoke/{id}")
    public String revoke(@AuthenticationPrincipal HandlaUser user, @PathVariable("id") Long id,
                         RedirectAttributes redirectAttributes) {
        apiTokenService.revoke(id, user);
        redirectAttributes.addFlashAttribute("message", "API-nyckeln är borttagen och fungerar inte längre.");
        return "redirect:/users/tokens";
    }
}
