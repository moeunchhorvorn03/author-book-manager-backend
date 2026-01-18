package com.example.Author.Book.Manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private Long id;

    private String title;

    private Long authorId;

    private String publishedYear;

    private String author;

    private float price;

    private float rating;

    private String category;

    private String coverImage;

    private String description;

    private boolean is_best_seller;
}
