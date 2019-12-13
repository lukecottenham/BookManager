package com.bae.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bae.manager.exception.InvalidEntryException;
import com.bae.manager.persistence.domain.Author;
import com.bae.manager.persistence.repo.AuthorRepo;

@Service
public class AuthorService {
	private AuthorRepo repo;
	
	@Autowired
	public AuthorService(AuthorRepo repo) {
		super();
		this.repo = repo;
	}

	public Author createAuthor(Author author) throws InvalidEntryException{
		if ((author.getFirstName() + " " + author.getLastName()).length() > 250)
		{
			throw new InvalidEntryException();
		}
		return this.repo.save(author);
	}



}
