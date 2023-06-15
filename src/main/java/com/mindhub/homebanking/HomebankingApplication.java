package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@SpringBootApplication
public class  HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData (ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository){
		return args -> {
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com" );
			Client tatiana = new Client("Tatiana", "Guzman", "tatiguzmn@hotmail.com");

			Account VIN001 = new Account("VIN001", LocalDate.now(), 5000.00);
			Account VIN002 = new Account("VIN002", LocalDate.now().plusDays(1),7500.00);
			Account VIN003 = new Account("VIN003", LocalDate.now().plusDays(3), 15000.00);
			Account VIN004 = new Account("VIN004", LocalDate.now(), 80000.00);

			Transaction transaction01 = new Transaction(TransactionType.CREDIT, 1000.00,"Mercado Libre", LocalDateTime.now());
			Transaction transaction02 = new Transaction(TransactionType.DEBIT, -1800.00,"Mc Donalds", LocalDateTime.now());
			Transaction transaction03 = new Transaction(TransactionType.CREDIT, 1300.00,"Rock Donuts", LocalDateTime.now());
			Transaction transaction04 = new Transaction(TransactionType.DEBIT, -1500.00,"Suship", LocalDateTime.now());

			List<Integer> mortgage = List.of(12,24,36,48,60);
			Loan loan1 = new Loan("Mortgage", 500.000, mortgage);
			List<Integer> personal = List.of(6,12,24);
			Loan loan2 = new Loan("Personal", 100.000,personal);
			List<Integer> automotive = List.of(6,12,24,36);
			Loan loan3 = new Loan("Automotive", 300.000, automotive);
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			////AGREGAR A LOS CLIENTES::://///////
//			Loan loanMelba = new Loan("Mortgage Loan",400000.0, Collections.singletonList(60));
//			Loan loanMelba2 = new Loan("Personal Loan", 50000.0,Collections.singletonList(12));
//			Loan loanTatiana = new Loan("Personal Loan", 100000.0, Collections.singletonList(24));
//			Loan loanTatiana2 = new Loan("Automotive Loan",200000.0, Collections.singletonList(36));

			//AGREGO CLIENTES CON CLIENTLOAN::///
			ClientLoan mortgageMelba = new ClientLoan(400000.0,60);
			ClientLoan personalMelba = new ClientLoan(50000.0,12);
			ClientLoan personalTatiana = new ClientLoan(100000.0,24);
			ClientLoan automotiveTatiana = new ClientLoan(200000.0,36);

			clientRepository.save(melba);
			clientRepository.save(tatiana);
			melba.addAccount(VIN001);
			melba.addAccount(VIN002);
			tatiana.addAccount(VIN003);
			tatiana.addAccount(VIN004);

			accountRepository.save(VIN001);
			accountRepository.save(VIN002);
			accountRepository.save(VIN003);
			accountRepository.save(VIN004);

			VIN001.addTransaction(transaction01);
			VIN001.addTransaction(transaction02);
			VIN002.addTransaction(transaction03);
			VIN002.addTransaction(transaction04);
			transactionRepository.save(transaction01);
			transactionRepository.save(transaction02);
			transactionRepository.save(transaction03);
			transactionRepository.save(transaction04);


//			loanRepository.save(loanMelba);
//			loanRepository.save(loanMelba2);
//			loanRepository.save(loanTatiana);
//			loanRepository.save(loanTatiana2);
			melba.addClientLoan(mortgageMelba);
			loan1.addClientLoan(mortgageMelba);
			melba.addClientLoan(personalMelba);
			loan2.addClientLoan(personalMelba);
			tatiana.addClientLoan(personalTatiana);
			tatiana.addClientLoan(automotiveTatiana);
			clientLoanRepository.save(mortgageMelba);
			clientLoanRepository.save(personalMelba);
			clientLoanRepository.save(personalTatiana);
			clientLoanRepository.save(automotiveTatiana);


		};

	}
}
