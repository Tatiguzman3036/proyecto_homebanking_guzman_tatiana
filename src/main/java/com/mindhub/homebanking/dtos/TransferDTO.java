package com.mindhub.homebanking.dtos;
public class TransferDTO {
    private Double amount;
    private String accountOrigin;
    private String accountDestination;
    private String description;

    public TransferDTO() {
    }

    public Double getAmount() {
        return amount;
    }

    public String getAccountOrigin() {
        return accountOrigin;
    }

    public String getAccountDestination() {
        return accountDestination;
    }

    public String getDescription() {
        return description;
    }
}
