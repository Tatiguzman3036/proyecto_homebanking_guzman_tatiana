package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class  HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public CommandLineRunner initData (ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository,CardRepository cardRepository){
		return args -> {


			Client melba = new Client("Melba", "Morel", "melba@mindhub.com" , passwordEncoder.encode("Melba123"));
			Client tatiana = new Client("Tatiana", "Guzman", "tatiguzmn@hotmail.com", passwordEncoder.encode("Tatiana123"));

			Account VIN001 = new Account("VIN001", LocalDate.now(), 5000.00);
			Account VIN002 = new Account("VIN002", LocalDate.now().plusDays(1),7500.00);
			Account VIN003 = new Account("VIN003", LocalDate.now().plusDays(3), 15000.00);
			Account VIN004 = new Account("VIN004", LocalDate.now(), 80000.00);

			Transaction transaction01 = new Transaction(TransactionType.CREDIT, 1000.00,"Mercado Libre", LocalDateTime.now());
			Transaction transaction02 = new Transaction(TransactionType.DEBIT, -1800.00,"Mc Donalds", LocalDateTime.now());
			Transaction transaction03 = new Transaction(TransactionType.CREDIT, 1300.00,"Rock Donuts", LocalDateTime.now());
			Transaction transaction04 = new Transaction(TransactionType.DEBIT, -1500.00,"Suship", LocalDateTime.now());

			List<Integer> mortgageList = List.of(12,24,36,48,60);
			Loan mortgage = new Loan("Mortgage", 500.000, mortgageList);
			List<Integer> personalList = List.of(6,12,24);
			Loan personal = new Loan("Personal", 100.000,personalList);
			List<Integer> automotiveList = List.of(6,12,24,36);
			Loan automotive = new Loan("Automotive", 300.000, automotiveList);
			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);
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


//			loanRepository.save(loanMelba);//
//			loanRepository.save(loanMelba2);//
//			loanRepository.save(loanTatiana);//
//			loanRepository.save(loanTatiana2);//
			melba.addClientLoan(mortgageMelba);
			mortgage.addClientLoan(mortgageMelba);
			melba.addClientLoan(personalMelba);
			personal.addClientLoan(personalMelba);
//			tatiana.addClientLoan(personalTatiana);
//			personal.addClientLoan(personalTatiana);
//			tatiana.addClientLoan(automotiveTatiana);
//			automotive.addClientLoan(automotiveTatiana);
			clientLoanRepository.save(mortgageMelba);
			clientLoanRepository.save(personalMelba);
			clientLoanRepository.save(personalTatiana);
			clientLoanRepository.save(automotiveTatiana);
			Card gold = new Card(melba.getFirstName()+ " "+ melba.getLastName(), CardType.DEBIT,ColorType.GOLD, (short) 750,LocalDateTime.now(),LocalDateTime.now().plusYears(5),"5541-7685-9210-0016");
			Card titanium = new Card(melba.getFirstName()+ " "+ melba.getLastName(),CardType.CREDIT,ColorType.TITANIUM,(short)698,LocalDateTime.now(),LocalDateTime.now().plusYears(5),"1918-4645-7070-0302");
			Card silver = new Card(melba.getFirstName()+" "+ melba.getLastName(), CardType.CREDIT,ColorType.SILVER,(short)711,LocalDateTime.now(),LocalDateTime.now().plusYears(5),"5578-2588-4091-3036");

			melba.addCards(gold);
			melba.addCards(titanium);
			melba.addCards(silver);
			cardRepository.save(gold);
			cardRepository.save(titanium);
			cardRepository.save(silver);
//			tatiana.addCards(silver);
//			cardRepository.save(silver);
		};

	}
}
