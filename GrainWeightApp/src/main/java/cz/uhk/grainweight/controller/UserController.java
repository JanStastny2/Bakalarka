package cz.uhk.grainweight.controller;

import cz.uhk.grainweight.model.User;
import cz.uhk.grainweight.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = { "", "/" })
    public String list(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users_list";
    }

    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable long id) {
        model.addAttribute("user", userService.getUser(id));
        return "users_detail";
    }

    @GetMapping("/delete/{id}")
    public String delete( @PathVariable long id) {
       userService.deleteUser(id);
        return "redirect:/users/";
    }

    @GetMapping("/add")
    public String add(Model model) {
        User user = new User();
        user.setRole("USER");
        model.addAttribute("user", user);
        return "users_add";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable long id) {
        model.addAttribute("user", userService.getUser(id));
        return "users_add";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users/";
    }

}
