package com.library.api.application.service;

import com.library.api.application.command.RegisterBorrowerCommand;
import com.library.api.application.mapper.ApplicationResponseMapper;
import com.library.api.application.port.in.RegisterBorrowerUseCase;
import com.library.api.application.port.out.BorrowerRepositoryPort;
import com.library.api.application.response.*;
import com.library.api.domain.exception.DomainException;
import com.library.api.domain.exception.ErrorCode;
import com.library.api.domain.model.Borrower;
import com.library.api.domain.valueobject.BorrowerId;

import java.time.ZonedDateTime;
import java.util.UUID;

public class BorrowerApplicationService implements RegisterBorrowerUseCase {
    private final BorrowerRepositoryPort borrowerRepository;

    public BorrowerApplicationService(BorrowerRepositoryPort borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    @Override
    public ApiResponse<BorrowerResponse> execute(RegisterBorrowerCommand command) {
        if (borrowerRepository.existsByEmail(command.email())) {
            throw new DomainException(ErrorCode.DUPLICATE_EMAIL);
        }
        Borrower borrower = new Borrower(BorrowerId.generate(), command.name(), command.email());
        Borrower saved = borrowerRepository.save(borrower);

        return new ApiResponse<>("0000", "SUCCESS", ZonedDateTime.now(),
                ApplicationResponseMapper.toResponse(saved));
    }
}