package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;


public class ClientLoanDTO {
    private long id;
    private double amount;
    private int payments;

    private String name;
    public ClientLoanDTO(ClientLoan clientLoan) {

        this.id = clientLoan.getId();

        this.amount = clientLoan.getAmount();

        this.payments = clientLoan.getPayments();
        name= clientLoan
                .getLoan()
                .getName();

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

}
