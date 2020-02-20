package com.backend.services.myideapool.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.backend.services.myideapool.entities.Idea;

public interface IdeaRepository extends CrudRepository<Idea, Integer> {

	@Query(value = "select * from Idea where user_id = ?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
	List<Idea> findIdeas(Integer userId, Integer pageSize, Integer pageNumber);
}