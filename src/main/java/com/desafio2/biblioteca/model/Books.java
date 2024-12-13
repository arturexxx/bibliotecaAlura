package com.desafio2.biblioteca.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
public class Books {
	
    @Id
    private Long id;

    @Column(name = "external_id")
    private Long externalId;

    @Column(nullable = false)
    private String title;
  
    
    private List<Author> authors = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "language")
    private List<String> languages = new ArrayList<>();

    @Column(name = "download_count")
    private Long downloadCount;
}
