package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;


@SpringBootApplication
public class  HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData (ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
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
			melba.addAccount(VIN001);
			melba.addAccount(VIN002);
			tatiana.addAccount(VIN003);
			tatiana.addAccount(VIN004);
			VIN001.addTransaction(transaction01);
			VIN001.addTransaction(transaction02);
			VIN002.addTransaction(transaction03);
			VIN002.addTransaction(transaction04);
			clientRepository.save(melba);
			clientRepository.save(tatiana);
			accountRepository.save(VIN001);
			accountRepository.save(VIN002);
			accountRepository.save(VIN003);
			accountRepository.save(VIN004);
			transactionRepository.save(transaction01);
			transactionRepository.save(transaction02);
			transactionRepository.save(transaction03);
			transactionRepository.save(transaction04);
			System.out.println(transaction02);




		};

	}
}
