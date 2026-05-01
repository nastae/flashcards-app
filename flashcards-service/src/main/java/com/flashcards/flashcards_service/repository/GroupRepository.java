package com.flashcards.flashcards_service.repository;

import com.flashcards.flashcards_service.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByDeletedFalse();
}
