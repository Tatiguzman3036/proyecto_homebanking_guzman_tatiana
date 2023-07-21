package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double amount;
    private int payments;
    private Integer loanPayments;
    private Double restAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;

    public ClientLoan() {
    }

    public ClientLoan(double amount, int payments, Integer loanPayments, Double restAmount) {
        this.amount = amount;
        this.payments = payments;
        this.loanPayments = loanPayments;
        this.restAmount = restAmount;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Integer getLoanPayments() {
        return loanPayments;
    }

    public void setLoanPayments(Integer loanPayments) {
        this.loanPayments = loanPayments;
    }

    public Double getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(Double restAmount) {
        this.restAmount = restAmount;
    }
}
