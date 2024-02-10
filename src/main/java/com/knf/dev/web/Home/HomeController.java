package com.knf.dev.web.Home;

import com.knf.dev.model.Skill.Skill;
import com.knf.dev.model.User.User;
import com.knf.dev.repository.UserRepo.UserRepository;
import com.knf.dev.service.Service.Skill.SkillService;
import com.knf.dev.service.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    SkillService skillService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<String> getAllSkills() {
        List<Skill> skillList = skillService.getAllSkills();
        return skillList.stream().map(Skill::getName).collect(Collectors.toList());
    }

    @ResponseBody
    @GetMapping("/profile")
    public ResponseEntity<User> showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // Load additional user details from your database
        User user = userRepository.findNameByEmail(username);
        System.out.println("user email: " + user);
        // Return the user information as JSON with status OK (200)
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Check if the user has the ADMIN role
            if (authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/organization/";
            }
        }

        // Default redirect for non-admin users
        return "user_home";
    }


    @GetMapping("/home")
    public String userhome() {
        return "home";
    }


    @GetMapping("/users")
    public String userHome(Model model) {
        model.addAttribute("listusers", userService.getAll());
        return "user-index";
    }
}
