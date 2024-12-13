package com.desafio2.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.desafio2.biblioteca.model.Books;
import com.desafio2.biblioteca.model.dto.AuthorsDTO;
import com.desafio2.biblioteca.model.dto.BookDTO;

@Repository
public interface  BooksRepository extends JpaRepository<Books, Long> {
	
	@Query("SELECT DISTINCT new com.desafio2.biblioteca.model.dto.BookDTO(b.title, a.name, bl, b.downloadCount) " +
		       "FROM Books b JOIN b.authors a JOIN b.languages bl")
		List<BookDTO> findBookDetails();
	
	@Query("""
	        SELECT new com.desafio2.biblioteca.model.dto.AuthorsDTO(
	            a.name, 
	            a.birthYear, 
	            a.deathYear, 
	            STRING_AGG(b.title, ', ')
	        )
	        FROM Author a
	        JOIN a.book b  -- Aquí usas 'book', ya que es la propiedad en la entidad Author
	        GROUP BY a.name, a.birthYear, a.deathYear
	    """)
	List<AuthorsDTO> findAllAuthorsWithBooks();



}
