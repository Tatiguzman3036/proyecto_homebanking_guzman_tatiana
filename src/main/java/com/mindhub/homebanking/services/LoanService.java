package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    void save (Loan loan);
    List<LoanDTO> getLoanDTOs();
    Loan findById(long id);
}
