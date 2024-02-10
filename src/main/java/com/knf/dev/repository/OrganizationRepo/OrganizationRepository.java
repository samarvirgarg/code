package com.knf.dev.repository.OrganizationRepo;

import com.knf.dev.model.Organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {


    @Query("SELECT DISTINCT o FROM Organization o " +
            "LEFT JOIN o.skills s " +
            "WHERE o.name LIKE %:search% " +
            "    OR o.email LIKE %:search% " +
            "    OR s.name LIKE %:search%")
    List<Organization> findByKeyword(@Param("search") String search);

//    @Query("SELECT DISTINCT o FROM Organization o LEFT JOIN FETCH o.skills s WHERE  (COALESCE(:skills) IS NULL OR s.name IN :skills)")
//    List<Organization> findByKeywordAndSkills(@Param("skills") List<String> skills);

    @Query("SELECT DISTINCT o FROM Organization o " +
            "LEFT JOIN FETCH o.skills s " +
            "WHERE (:search IS NULL OR " +
            "       o.name LIKE %:search% OR " +
            "       o.email LIKE %:search% OR " +
            "       o.address LIKE %:search% OR " +
            "       o.city LIKE %:search% OR " +
            "       o.state LIKE %:search% OR " +
            "       (COALESCE(:skills) IS NULL OR s.name IN :skills))")
    List<Organization> findByOrganizationORSkills(@Param("search") String search, @Param("skills") List<String> skills);

}
