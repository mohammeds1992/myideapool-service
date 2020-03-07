package com.backend.services.myideapool.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backend.services.myideapool.entities.Idea;

@DataJpaTest
public class IdeaRepositoryTest {

	@Autowired
	private IdeaRepository repository;

	@Test
	public void testSizeOfIdeasPage1() {
		Iterable<Idea> ideas = repository.findIdeas(100, 10, 0);
		assertEquals(10, ((Collection<?>) ideas).size());
	}
	
	@Test
	public void testSizeOfIdeasPage2() {
		Iterable<Idea> ideas = repository.findIdeas(100, 10, 10);
		assertEquals(5, ((Collection<?>) ideas).size());
	}
	
	@Test
	public void testSizeOfIdeasForInvalidUser() {
		Iterable<Idea> ideas = repository.findIdeas(-1, 10, 1);
		assertEquals(0, ((Collection<?>) ideas).size());
	}
}