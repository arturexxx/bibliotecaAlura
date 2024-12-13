package com.desafio2.biblioteca.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDTO {
    private String title;
    private String authors;
    private String languages;
    private Long downloadCount;

}
