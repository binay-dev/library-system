package com.library.api.domain.model;

import com.library.api.domain.valueobject.BookId;
import com.library.api.domain.valueobject.Isbn;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Book {
    private final BookId id;
    private final Isbn isbn;
    private final String title;
    private final String author;
}