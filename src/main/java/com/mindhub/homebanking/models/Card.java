package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String cardholder;
    private CardType type;
    private ColorType color;
    private Short cvv;
    private LocalDateTime thruDate;
    private LocalDateTime fromDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Card() { }

    public Card(String cardholder, CardType type, ColorType color, Short cvv, LocalDateTime thruDate, LocalDateTime fromDate) {
        this.cardholder = cardholder;
        this.type = type;
        this.color = color;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.fromDate = fromDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardholder() {

        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public ColorType getColor() {
        return color;
    }

    public void setColor(ColorType color) {
        this.color = color;
    }

    public Short getCvv() {
        return cvv;
    }

    public void setCvv(Short cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }
    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
