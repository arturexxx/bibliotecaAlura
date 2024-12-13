package com.desafio2.biblioteca.pincipal;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.desafio2.biblioteca.model.Author;
import com.desafio2.biblioteca.model.Books;
import com.desafio2.biblioteca.model.dto.AuthorsDTO;
import com.desafio2.biblioteca.model.dto.BookDTO;
import com.desafio2.biblioteca.repository.BooksRepository;
import com.desafio2.biblioteca.service.ConsumoAPI;
import com.desafio2.biblioteca.service.ConvierteDatos;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class Principal {
	
	@Autowired
	private BooksRepository booksRepository;
	
	private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private List<Books> books = new ArrayList<>();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
	
	public void muestraElMenu() {
		var opcion = -1;
		while (opcion != 0) {
			try {
	        var menu = """
	                1 - Buscar libro por titulo 
	                2 - Listar libros Registrados
	                3 - Listar autores Registrados
	                4 - Listar autores vivos en un determinado año
	                5 - Listar libros por idioma
	                              
	                0 - Salir
	                Seleccione una opción, ingresando un numero del menu:
	                """;
	        System.out.println(menu);
	        opcion = teclado.nextInt();
	        teclado.nextLine();
	        switch (opcion) {
	            case 1:
	            	findAndSaveBook();
	            	break;
	            case 2:
	            	listAllBooks();
	            	break;
	            case 3:
	            	listAllAuthors();
	            	break;
	            case 0:
	            	System.out.println("Saliendo...");
	            	break;
	            default:
	            	System.out.println("Opción inválida.");
	        	}		
		} catch (InputMismatchException e) {
	        System.out.println("Error: Debes ingresar un número entero.");
	        teclado.nextLine();  // Limpiar el buffer para que el programa no quede atrapado
	    }
	 }
}

	private void findAndSaveBook() {
	    System.out.println("Ingrese el nombre del libro que desea buscar:");
	    var nombreLibro = teclado.nextLine();
	    var url = URL_BASE + nombreLibro.replace(" ", "%20");
	    
	    var json = consumoApi.obtenerDatos(url);
	    ConvierteDatos convierteDatos = new ConvierteDatos();
	    var rootNode = convierteDatos.obtenerDatos(json, JsonNode.class);
	    var results = rootNode.get("results");

	    if (results.isArray() && results.size() > 0) {
	        JsonNode firstResult = results.get(0);

	        Books book = createBookFromJson(firstResult);
	        booksRepository.save(book);

	        printBookDetails(book);

	        pauseExecution(5000); // Pausa de 3 segundos
	    } else {
	        System.out.println("No se encontraron libros para ese título.");
	    }
	}

	
	private Books createBookFromJson(JsonNode bookNode) {
	    Books book = new Books();
	    book.setId(bookNode.get("id").asLong());
	    book.setExternalId(bookNode.get("id").asLong());
	    book.setTitle(bookNode.get("title").asText());

	    book.setAuthors(processAuthors(bookNode.get("authors")));
	    book.setLanguages(processLanguages(bookNode.get("languages")));
	    book.setDownloadCount(bookNode.get("download_count").asLong());

	    return book;
	}

	private List<Author> processAuthors(JsonNode authorsNode) {
	    List<Author> authors = new ArrayList<>();
	    if (authorsNode != null && authorsNode.isArray()) {
	        for (JsonNode authorNode : authorsNode) {
	            Author author = new Author();
	            author.setName(authorNode.get("name").asText());
	            if (authorNode.has("birth_year")) {
	                author.setBirthYear(authorNode.get("birth_year").asInt());
	            }
	            if (authorNode.has("death_year")) {
	                author.setDeathYear(authorNode.get("death_year").asInt());
	            }
	            authors.add(author);
	        }
	    }
	    return authors;
	}

	private List<String> processLanguages(JsonNode languagesNode) {
	    List<String> languages = new ArrayList<>();
	    if (languagesNode != null && languagesNode.isArray()) {
	        for (JsonNode languageNode : languagesNode) {
	            languages.add(languageNode.asText());
	        }
	    }
	    return languages;
	}

	private void printBookDetails(Books book) {
	    System.out.println("------- LIBRO -------");
	    System.out.println("Título: " + book.getTitle());
	    if (!book.getAuthors().isEmpty()) {
	        System.out.println("Autor: " + book.getAuthors().get(0).getName());
	    }
	    if (!book.getLanguages().isEmpty()) {
	        System.out.println("Idioma: " + book.getLanguages().get(0));
	    }
	    System.out.println("Número de Descargas: " + book.getDownloadCount());
	    System.out.println("----------------------");
	}

	private void pauseExecution(int millis) {
	    try {
	        Thread.sleep(millis);
	        System.out.println();
	        System.out.println("---------------------------------");
	    } catch (InterruptedException e) {
	        System.err.println("La pausa fue interrumpida: " + e.getMessage());
	    }
	}

	private void listAllBooks() {
	    List<BookDTO> books = booksRepository.findBookDetails();

	    if (books.isEmpty()) {
	        System.out.println("No hay libros registrados en la base de datos.");
	        return;
	    }

	    System.out.println("------- LISTA DE LIBROS -------");
	    for (BookDTO book : books) {
	        System.out.println("Título: " + book.getTitle());
	        System.out.println("Autores: " + String.join(", ", book.getAuthors()));
	        System.out.println("Idiomas: " + String.join(", ", book.getLanguages()));
	        System.out.println("Número de Descargas: " + book.getDownloadCount());
	        System.out.println("---------------------------------");
	        pauseExecution(2000);
	    }
	}
	
	private void listAllAuthors() {
		System.out.println("Entro a ListAllAuthors");
		List<AuthorsDTO> authors = booksRepository.findAllAuthorsWithBooks();
		System.out.println("1");
		if (books.isEmpty()) {
	        System.out.println("No hay libros registrados en la base de datos.");
	        return;
	    }

	    System.out.println("------- LISTA DE LIBROS -------");
	    for (AuthorsDTO author : authors) {
	        System.out.println("Autor: " + author.getName());
	        System.out.println("Fecha de nacimiento: " + author.getBirthYear());
	        System.out.println("Fecha de fallecimiento: " + author.getDeathYear());
	        System.out.println("Libros: " + author.getTitle());
	        System.out.println("---------------------------------");
	        pauseExecution(2000);
	    }
	}
	
}
