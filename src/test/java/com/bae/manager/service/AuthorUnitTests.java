package com.bae.manager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.bae.manager.exception.DuplicateValueException;
import com.bae.manager.exception.EntryNotFoundException;
import com.bae.manager.exception.InvalidEntryException;
import com.bae.manager.persistence.domain.Author;
import com.bae.manager.persistence.repo.AuthorRepo;

@RunWith(SpringRunner.class)
public class AuthorUnitTests {

	@InjectMocks
	private AuthorService service;

	@Mock
	private AuthorRepo repo;

	private List<Author> authorList;

	private Author testAuthor;

	private Author testAuthorWIthId;

	private Author testAuthorFail;

	private Author testAuthorFailWithId;

	private long invalidId;

	final long id = 1L;
	private String length251 = "PebvtPIUTFWcyFFtucstLjqIhztILbbWHnoMZpfMupJsQjdqxDcpFfDtrJcdajvmqqocwlbzjROsLYcgZgWyboQPzxCdhVrvXnXJEXOhkzSGoEyeWFlkvHIkiDJIjsWRqZcVbpwZoRqsgdRVxDjWQvMPuIeYQnqxCDpdTkvaFnCdoPSYKWjPKIyOGbRJCurpbkoBgTmmc"
			+ "XhAcsWAgQPahSNCcaHuvsHNruwYTgtDynDOswCtEuHRCfAxpAh";

	@Before
	public void init() {
		this.authorList = new ArrayList<>();
		this.testAuthor = new Author("Terry Pratchett");
		this.testAuthorWIthId = new Author(testAuthor.getPenName());
		this.testAuthorWIthId.setId(id);
		this.authorList.add(testAuthor);
		this.authorList.add(testAuthor);
		this.testAuthorFail = new Author(this.length251);
		this.testAuthorFail.setId(id);
		this.testAuthorFailWithId = new Author(testAuthorFail.getPenName());
		this.testAuthorFailWithId.setId(id);
		this.invalidId = 2L;

	}

	@Test
	public void createAuthorTest() {
		when(this.repo.save(this.testAuthor)).thenReturn(this.testAuthorWIthId);
		assertEquals(this.testAuthorWIthId, this.service.createAuthor(this.testAuthor));
		verify(this.repo, times(1)).save(this.testAuthor);
	}

	@Test
	public void createAuthorDuplicateEntryTest() {

		when(this.repo.findAll()).thenReturn(this.authorList);
		assertThrows(DuplicateValueException.class, () -> {
			this.service.createAuthor(this.testAuthor);
			verify(this.repo, times(1)).findAll();

		});
	}

	@Test
	public void createAuthorNameTooLargeTest() {
		assertThrows(InvalidEntryException.class, () -> {
			this.service.createAuthor(this.testAuthorFail);
		});
	}

	@Test
	public void findDuplicateAuthorTest() {
		when(this.repo.findAll()).thenReturn(this.authorList);
		assertTrue(this.service.findRepeatedAuthor(this.testAuthor));
		assertFalse(this.service.findRepeatedAuthor(this.testAuthorFail));
		verify(this.repo, times(2)).findAll();
	}

	@Test
	public void getAllAuthorsTest() {
		when(this.repo.findAll()).thenReturn(this.authorList);
		assertEquals(this.authorList, this.service.getAllAuthors());
		verify(this.repo, times(1)).findAll();
	}

	@Test
	public void findAuthorByIdTest() {
		when(this.repo.findById(id)).thenReturn(Optional.of(this.authorList.get(0)));
		assertEquals(testAuthor, this.service.findAuthorById(id));
		verify(this.repo, times(1)).findById(id);
	}

	@Test
	public void findAuthorByIdMissingIdTest() {
		when(this.repo.findById(invalidId)).thenThrow(new EntryNotFoundException());
		assertThrows(EntryNotFoundException.class, () -> {
			this.service.findAuthorById(invalidId);
		});
	}

}