package com.aquinas.servicegroups.repository;

import com.aquinas.servicegroups.repository.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<Group, Long> {
    Group findByTitle(String title);
}
