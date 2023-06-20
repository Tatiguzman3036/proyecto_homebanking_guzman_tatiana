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
    private Short cvv;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;
    public CardDTO(Card cards) {
        this.cardholder = cards.getClient().getFirstName()+ " "+ cards.getClient().getLastName();

        this.type= cards.getType();

        this.color = cards.getColor();

        this.cvv = cards.getCvv();

        this.fromDate = cards.getFromDate();

        this.thruDate = cards.getThruDate();
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

    public Short getCvv() {
        return cvv;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }
}
