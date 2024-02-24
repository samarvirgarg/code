package com.knf.dev.service.ServiceImpl.SkillImpl;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Skill.Skill;
import com.knf.dev.repository.SkillRepo.SkillRepository;
import com.knf.dev.service.Service.Skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SkillServiceImpl implements SkillService {


    @Autowired
    private SkillRepository skillRepository;

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.getskill();
    }

    @Override
    public List<Skill> getSelectedSkillsForOrganization(Organization organization) {
        return skillRepository.findAllAndSelected(organization);
    }

    @Override
    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    @Override
    public void saveSkill(Skill skill) {
        this.skillRepository.save(skill);
    }

    @Override
    public Skill createOrUpdateSkill(Skill user_skill) {
        return skillRepository.save(user_skill);
    }

    @Override
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }

    @Override
    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name);
    }

    @Override
    public List<Skill> getSkillByKeyword(String search) {
        return skillRepository.findByKeyword(search);
    }
}
