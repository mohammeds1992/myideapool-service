package com.backend.services.myideapool.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backend.services.myideapool.entities.User;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository repository;

	@Test
	public void testFindUserByEmail() {
		assertNotNull(repository.findUserByEmail("shoaib@gmail.com"));
	}
	
	@Test
	public void testFindUserByInvalidEmail() {
		assertNull(repository.findUserByEmail("xyz@gmail.com"));
	}
	
	@Test
	public void testFindUserByRefreshToken() {
		User user = repository.findUserByRefreshToken("f7ca2721-345e-4b27-aaad-8e19f28cf2b9");
		assertNotNull(user);
		assertEquals("shoaib@gmail.com", user.getEmail());
	}
	
	@Test
	public void testFindUserByInvalidRefreshToken() {
		User user = repository.findUserByRefreshToken("blaa");
		assertNull(user);
	}
}