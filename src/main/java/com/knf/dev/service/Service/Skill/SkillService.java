package com.knf.dev.service.Service.Skill;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Skill.Skill;
import com.knf.dev.model.User.User;
import com.knf.dev.repository.SkillRepo.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public interface SkillService {

    List<Skill> getAllSkills();

    public List<Skill> getSelectedSkillsForOrganization(Organization organization);

    Optional<Skill> getSkillById(Long id);

    void saveSkill(Skill skill);

    Skill createOrUpdateSkill(Skill user_skill);

    void deleteSkill(Long id);

    public Optional<Skill> findByName(String name);


    List<Skill> getSkillByKeyword(String search);



}
