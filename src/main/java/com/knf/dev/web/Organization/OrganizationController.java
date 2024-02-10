package com.knf.dev.web.Organization;

import com.knf.dev.dto.OrganizationDTO;
import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Skill.Skill;
import com.knf.dev.service.Service.Organization.OrganizationService;
import com.knf.dev.service.Service.Skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;


    @Autowired
    private SkillService skillService;


    @GetMapping("/")
    public String OrganizationHome(Model model) {
        model.addAttribute("listOrganization", organizationService.getAllOrganization());
        return "org_home";
    }


    @GetMapping("/showNewOrganizationForm")
    public String createOrganization(Model model) {
        List<Skill> skills = skillService.getAllSkills(); // Assuming you have a SkillService

        System.out.println("all skills..............................." +skills.toString());
        model.addAttribute("organization", new Organization());
        model.addAttribute("allSkills", skillService.getAllSkills());
        return "org_form";
    }

    @PostMapping("/saveOrganization")
    public String saveOrganization(@ModelAttribute("organization") Organization organization, Model model) {
        try {
            organizationService.saveOrganization(organization);
            return "redirect:/organization/";
        }catch (DataIntegrityViolationException exception){
            model.addAttribute("error", "Duplicate entry for skill: " + organization.getName());
            return "org_form";
        }

    }


    @GetMapping("/showFormForUpdate/{OrgID}")
    public String updateOrganization(@PathVariable(value = "OrgID") long OrgID,
                                     @RequestParam(name = "skilId", required = false) Long skilId,
                                     Model model) {
        Organization organization = organizationService.getOrganizationById(OrgID);
        List<Skill> selectedSkills = skillService.getSelectedSkillsForOrganization(organization);
        System.out.println("selected skills..............................." +selectedSkills.toString());
        List<Skill> skills = skillService.getAllSkills();// Assuming you have a SkillService
        System.out.println("all skills..............................." +skills.toString());
        model.addAttribute("organization", organization);
        model.addAttribute("skills", skills);
        model.addAttribute("selectedSkillIds", selectedSkills);
        model.addAttribute("skilId", skilId); // Add skilId to the model
        return "org_update";
    }



    @GetMapping("/deleteOrganization/{OrgID}")
    public String deleteOrganization(@PathVariable(value = "OrgID") long OrgID) {
        this.organizationService.deleteOrganizationById(OrgID);
        return "redirect:/organization/";
    }



//    Search By Organization OR Skills
@RequestMapping(path = {"/Search-org"})
public String searchByOrganizationOrSkills(@ModelAttribute("organizationSearch") Organization organization,
                           Model model,
                           Authentication authentication) {
    String skillsSearch = organization.getSkillsSearch();
    if (skillsSearch != null) {
        model.addAttribute("listOrganization", organizationService.getByOrganizationOrSkills(skillsSearch, Arrays.asList(skillsSearch.split(","))));
    } else {
        model.addAttribute("userlist", organizationService.getAllOrganization());
    }

    if (authentication != null && authentication.isAuthenticated()) {
        // Check if the user has the ADMIN role
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            return "org_home"; // Admin view
        }
    }
    // Default view for non-admin users
    return "user_home";
}


}
