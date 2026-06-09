package com.library.api.domain.model;

import com.library.api.domain.valueobject.BorrowerId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Borrower {
    private final BorrowerId id;
    private String name;
    private final String email;
}