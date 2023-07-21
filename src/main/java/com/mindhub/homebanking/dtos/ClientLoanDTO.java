package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;


public class ClientLoanDTO {
    private long id;
    private double amount;
    private int payments;
    private Integer loanPayments;
    private Double restAmount;
    private String name;
    public ClientLoanDTO(ClientLoan clientLoan) {

        this.id = clientLoan.getId();

        this.amount = clientLoan.getAmount();

        this.loanPayments = clientLoan.getLoanPayments();

        this.restAmount = clientLoan.getRestAmount();

        this.payments = clientLoan.getPayments();
        this.name= clientLoan.getLoan().getName();

    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public Integer getLoanPayments() {
        return loanPayments;
    }

    public Double getRestAmount() {
        return restAmount;
    }
}
