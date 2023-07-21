package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.ColorType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CardDTO {
    private long id;
    private String cardholder;
    private CardType type;
    private ColorType color;
    private Integer cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private String number;
    private Boolean status;

    public CardDTO(Card cards) {
        this.id = cards.getId();
        this.cardholder = cards.getCardholder();
        this.type= cards.getType();
        this.color = cards.getColor();
        this.cvv = cards.getCvv();
        this.fromDate = cards.getFromDate();
        this.thruDate = cards.getThruDate();
        this.number = cards.getNumber();
        this.status = cards.getStatus();
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

    public Boolean getStatus() {
        return status;
    }

    public Integer getCvv() { return cvv; }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public String getNumber() {
        return number;
    }

}
