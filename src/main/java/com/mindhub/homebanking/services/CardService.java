package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {
    boolean existsByNumber(String number);
    Card findById(Long id);
    Card findByNumber(String number);
    void save (Card card);

}
