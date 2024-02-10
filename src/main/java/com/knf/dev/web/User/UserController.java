package com.knf.dev.web.User;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Role.UserRole;
import com.knf.dev.model.Skill.Skill;
import com.knf.dev.model.User.User;
import org.dom4j.rule.Mode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.knf.dev.dto.UserRegistrationDto;
import com.knf.dev.service.Service.User.UserService;

import java.util.List;

@Controller
@RequestMapping("/registration")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    //user update
//    @GetMapping("/update/{userId}")
//    public String updateUser(@PathVariable(value = "userId") long userId,
//                             Model model){
//        User user = userService.getUserById(userId);
//        model.addAttribute("userlist",user);
//        return "user_update";
//    }


    @GetMapping("/update/{userId}")
    public String updateUser(@PathVariable(value = "userId") long userId, Model model) {
        User user = userService.getUserById(userId);
        System.out.println("user:.........." +user);
        model.addAttribute("user", user);
        return "user_update";
    }

    @PostMapping("/saveUser")
    public String saveOrganization(@ModelAttribute("user") UserRegistrationDto userDto) {
        System.out.println("insde dddddddddd");
        userService.saveUser(userDto);
        return "redirect:/users";
    }


    @GetMapping("/delete/{userId}")
    public String deleteOrganization(@PathVariable(value = "userId") long userId){
        this.userService.deleteUserById(userId);
        return "redirect:/users";
    }


    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, UserRole role) {
        userService.save(registrationDto, role);
        return "redirect:/users";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("listusers", userService.getByKeyword(search));
        } else {
            // If no search query provided, display all users
            model.addAttribute("listusers", userService.getAll());
        }
        return "user-index"; // Return the name of your Thymeleaf template to display the search results
    }




}
