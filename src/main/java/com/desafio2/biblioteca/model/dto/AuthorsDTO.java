package com.desafio2.biblioteca.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthorsDTO {
	
	private String name;
	private Integer birthYear;
    private Integer deathYear;
    private String title;
    
    public AuthorsDTO(String name, Integer birthYear, Integer deathYear, String title) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.title = title;
    }


}
