package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private Long idLoanType;
    private Double amount;
    private Integer payments;
    private String accountDestination;
    private Double percentage;

    public LoanApplicationDTO() {
    }

    public Long getIdLoanType() {
        return idLoanType;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccountDestination() {
        return accountDestination;
    }

    public Double getPercentage() {
        return percentage;
    }
}
