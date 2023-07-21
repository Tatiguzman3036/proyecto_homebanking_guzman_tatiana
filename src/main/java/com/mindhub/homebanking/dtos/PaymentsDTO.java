package com.mindhub.homebanking.dtos;

public class PaymentsDTO {
    private String number;
    private Integer cvv;
    private Double amount;
    private String description;

    public PaymentsDTO() {
    }

    public String getNumber() {
        return number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
