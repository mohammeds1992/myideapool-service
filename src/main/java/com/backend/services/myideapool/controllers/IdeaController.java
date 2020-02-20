package com.backend.services.myideapool.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.services.myideapool.entities.Idea;
import com.backend.services.myideapool.exceptions.IdeaNotFoundException;
import com.backend.services.myideapool.repositories.IdeaRepository;
import com.backend.services.myideapool.request.models.CreateOrUpdateIdeaRequest;
import com.backend.services.myideapool.uitls.JwtUtil;

@RestController
@RequestMapping(value = "/ideas", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(maxAge = 3600, origins = "*")
public class IdeaController {

	@Autowired
	private IdeaRepository ideaRepository;

	@Autowired
	private JwtUtil jwtUtil;

    @RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Idea> createIdea(@RequestHeader(value = "X-Access-Token", required = false) String token,
			@RequestBody @Valid CreateOrUpdateIdeaRequest request) {

		Date current_date = new Date();

		Idea idea = ideaRepository.save(Idea.builder()
					.created_at(current_date)
					.last_modified_at(current_date)
					.content(request.getContent())
					.impact(request.getImpact())
					.ease(request.getEase())
					.confidence(request.getConfidence())
					.user_id(jwtUtil.extractUserId(token))
					.build());
		
		return new ResponseEntity<>(idea, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Idea> updateIdea(@RequestHeader(value = "X-Access-Token", required = false) String token,
			@PathVariable("id") Integer id, @RequestBody @Valid CreateOrUpdateIdeaRequest request) {

		Idea idea = ideaRepository.findById(id).orElseThrow(() -> new IdeaNotFoundException(id));

		idea.setContent(request.getContent());
		idea.setConfidence(request.getConfidence());
		idea.setEase(request.getEase());
		idea.setImpact(request.getImpact());

		ideaRepository.save(idea);

		return new ResponseEntity<>(idea, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Idea> getIdea(@RequestHeader(value = "X-Access-Token", required = false) String token,
			@PathVariable("id") Integer id) {
		Idea idea = ideaRepository.findById(id).orElseThrow(() -> new IdeaNotFoundException(id));
		return new ResponseEntity<>(idea, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteIdea(@RequestHeader(value = "X-Access-Token", required = false) String token,
			@PathVariable("id") Integer id) {
		Idea idea =  ideaRepository.findById(id).orElseThrow(() -> new IdeaNotFoundException(id));
		ideaRepository.delete(idea);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Iterable<Idea>> getIdeas(
			@RequestHeader(value = "X-Access-Token", required = false) String token,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") Integer page_size,
			@RequestParam(value = "page_number", required = false, defaultValue = "1") Integer page_number) {
		int offset = (page_number - 1) * page_size;
		return new ResponseEntity<>(ideaRepository.findIdeas(jwtUtil.extractUserId(token), page_size, offset),
				HttpStatus.OK);
	}
}