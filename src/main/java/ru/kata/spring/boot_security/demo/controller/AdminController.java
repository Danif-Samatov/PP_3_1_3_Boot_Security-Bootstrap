package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping({"", "/"})
    public String adminPage(Model model) {
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("allRoles", roleService.listRoles());
        return "admin";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user,
                          @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        userService.add(user, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        userService.update(user, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
