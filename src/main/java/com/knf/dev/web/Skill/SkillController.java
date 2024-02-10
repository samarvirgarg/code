package com.knf.dev.web.Skill;

import com.knf.dev.model.Skill.Skill;
import com.knf.dev.service.Service.Skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/skill")
public class SkillController {

    @Autowired
    private SkillService skillService;


    @GetMapping("/")
    public String getAllSkills(Model model) {
        List<Skill> skillList = skillService.getAllSkills();
        model.addAttribute("skills", skillList);
        return "skill_index";
    }

    @GetMapping("/{id}")
    public Optional<Skill> getSkillById(@PathVariable Long id) {

        return skillService.getSkillById(id);

    }

    @GetMapping("addskill")
    public String addSkill(Model model){
        Skill skill = new Skill();
        model.addAttribute("skills", skill);
        return "skill_form";
    }


//    @PostMapping("/saveSkill")
//    public String saveSkills(@ModelAttribute("skills") Skill skill){
//        System.out.println("error duplicate: " +skill.toString());
//        skillService.saveSkill(skill);
//        return "redirect:/skill/";
//    }

    @PostMapping("/saveSkill")
    public String saveSkills(@ModelAttribute("skills") Skill skill, Model model) {
        try {
            skillService.saveSkill(skill);
            return "redirect:/skill/"; // Redirect to the skill listing page
        } catch (DataIntegrityViolationException ex) {
            // Handle the duplicate skill error
            System.out.println("duplicate skill: " +ex);
            model.addAttribute("error", "Duplicate entry for skill: " + skill.getName());
            return "skill_form"; // Return back to the skill form page with error message
        }
    }


    @PostMapping("/add")
    public Skill createOrUpdateSkill(@RequestBody Skill userskill) {
        return skillService.createOrUpdateSkill(userskill);
    }

    @DeleteMapping("/{id}")
    public void deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
    }


    @GetMapping("/search")
    public String searchSkills(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("skills", skillService.getSkillByKeyword(search));
        } else {
            // If no search query provided, display all users
            model.addAttribute("skills", skillService.getAllSkills());
        }
        return "skill_index"; // Return the name of your Thymeleaf template to display the search results
    }

}
