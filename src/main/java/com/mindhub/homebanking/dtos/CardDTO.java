package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.ColorType;

import java.time.LocalDateTime;

public class CardDTO {
    private long id;
    private String cardholder;
    private CardType type;
    private ColorType color;
    private int cvv;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;
    private String number;
    public CardDTO(Card cards) {
        this.id = cards.getId();
        this.cardholder = cards.getCardholder();
        this.type= cards.getType();
        this.color = cards.getColor();
        this.cvv = cards.getCvv();
        this.fromDate = cards.getFromDate();
        this.thruDate = cards.getThruDate();
        this.number = cards.getNumber();
    }

    public long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public CardType getType() {
        return type;
    }

    public ColorType getColor() {
        return color;
    }

    public int getCvv() { return cvv; }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public String getNumber() {
        return number;
    }
}
