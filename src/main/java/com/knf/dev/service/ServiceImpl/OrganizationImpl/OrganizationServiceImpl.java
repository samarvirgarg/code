package com.knf.dev.service.ServiceImpl.OrganizationImpl;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Skill.Skill;
import com.knf.dev.repository.OrganizationRepo.OrganizationRepository;
import com.knf.dev.repository.SkillRepo.SkillRepository;
import com.knf.dev.service.Service.Organization.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private SkillRepository skillRepository;


    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public List<Organization> getAllOrganization() {
        return organizationRepository.findAll();
    }


    @Override
    public void saveOrganization(Organization organization) {
        this.organizationRepository.save(organization);
    }

    @Override
    public Organization getOrganizationById(long OrgID) {

        Optional<Organization> org_optional = organizationRepository.findById(OrgID);
        Organization organization = null;
        if (org_optional.isPresent()) {
            organization = org_optional.get();
        } else {
            throw new RuntimeException("Organization not found for id :: " + OrgID);
        }
        return organization;
    }

    @Override
    @Transactional
    public void deleteOrganizationById(long id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Organization not found with id: " + id));
        organization.getSkills().clear();
        this.organizationRepository.deleteById(id);
    }

//    public List<Organization> getByKeywordAndSkills(List<String> skills) {
//        System.out.println("search is: list of skills is ........" + skills.toString());
//        return this.organizationRepository.findByKeywordAndSkills(skills);
//    }

    @Override
    public List<Organization> getByOrganizationOrSkills(String search, List<String> skills) {
        System.out.println("search is: " + search + " and list of skills is ........" + skills.toString());
        return organizationRepository.findByOrganizationORSkills(search, skills);
    }

    public List<Organization> getByKeyword(String search) {
        System.out.println("input........." + search);
        return organizationRepository.findByKeyword(search);
    }

}
