package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
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
	public CommandLineRunner initData (ClientService clientService, AccountService accountService, TransactionService transactionService, LoanService loanService, ClientLoanService clientLoanService, CardService cardService){
		return args -> {

			Client admin = new Client("admin","admin","admin@admin.com", passwordEncoder.encode("admin"));
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com" , passwordEncoder.encode("Melba123"));
			Client tatiana = new Client("Tatiana", "Guzman", "tatiguzmn@hotmail.com", passwordEncoder.encode("Tatiana123"));

			Account VIN001 = new Account("VIN001", LocalDate.now(), 5000.00, true,AccountType.CURRENT);
			Account VIN002 = new Account("VIN002", LocalDate.now().plusDays(1),7500.00,true, AccountType.SAVINGS);
			Account VIN003 = new Account("VIN003", LocalDate.now().plusDays(3), 15000.00,true,AccountType.CURRENT);
			Account VIN004 = new Account("VIN004", LocalDate.now(), 80000.00,true,AccountType.SAVINGS);

			Transaction transaction01 = new Transaction(TransactionType.CREDIT, 1000.00,"Mercado Libre", LocalDateTime.now(),200.0);
			Transaction transaction02 = new Transaction(TransactionType.DEBIT, -1800.00,"Mc Donalds", LocalDateTime.now(),500.0);
			Transaction transaction03 = new Transaction(TransactionType.CREDIT, 1300.00,"Rock Donuts", LocalDateTime.now(),600.0);
			Transaction transaction04 = new Transaction(TransactionType.DEBIT, -1500.00,"Suship", LocalDateTime.now(),200.0);

			List<Integer> mortgageList = List.of(12,24,36,48,60);
			Loan mortgage = new Loan("Mortgage", 500000.0, mortgageList, 0.20);
			List<Integer> personalList = List.of(6,12,24);
			Loan personal = new Loan("Personal", 100000.0,personalList,0.30);
			List<Integer> automotiveList = List.of(6,12,24,36);
			Loan automotive = new Loan("Automotive", 300000.0, automotiveList,0.35);
			loanService.save(mortgage);
			loanService.save(personal);
			loanService.save(automotive);
			//AGREGO CLIENTES CON CLIENTLOAN::///
			ClientLoan mortgageMelba = new ClientLoan(400000.0,60,4,60.0);
			ClientLoan personalMelba = new ClientLoan(50000.0,12,12,5000.0);
			ClientLoan personalTatiana = new ClientLoan(100000.0,24,5,20000.0);
			ClientLoan automotiveTatiana = new ClientLoan(200000.0,36,6,5000.0);

			clientService.save(melba);
			clientService.save(tatiana);
			clientService.save(admin);
			melba.addAccount(VIN001);
			melba.addAccount(VIN002);
			tatiana.addAccount(VIN003);
			tatiana.addAccount(VIN004);

			accountService.save(VIN001);
			accountService.save(VIN002);
			accountService.save(VIN003);
			accountService.save(VIN004);

			VIN001.addTransaction(transaction01);
			VIN001.addTransaction(transaction02);
			VIN002.addTransaction(transaction03);
//			VIN002.addTransaction(transaction04);
			VIN003.addTransaction(transaction04);
			transactionService.save(transaction01);
			transactionService.save(transaction02);
			transactionService.save(transaction03);
			transactionService.save(transaction04);


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
			clientLoanService.save(mortgageMelba);
			clientLoanService.save(personalMelba);
			clientLoanService.save(personalTatiana);
			clientLoanService.save(automotiveTatiana);
			Card gold = new Card(melba.getFirstName()+ " "+ melba.getLastName(), CardType.DEBIT,ColorType.GOLD, 750,LocalDate.now().minusDays(5),LocalDate.now().minusDays(10),"5541-7685-9210-0016",true);
			Card titanium = new Card(melba.getFirstName()+ " "+ melba.getLastName(),CardType.CREDIT,ColorType.TITANIUM,698,LocalDate.now().plusYears(5),LocalDate.now(),"1918-4645-7070-0302",true);
			Card silver = new Card(melba.getFirstName()+" "+ melba.getLastName(), CardType.CREDIT,ColorType.SILVER,711,LocalDate.now().plusYears(5),LocalDate.now(),"5578-2588-4091-3036",true);

			melba.addCards(gold);
			melba.addCards(titanium);
			melba.addCards(silver);
			cardService.save(gold);
			cardService.save(titanium);
			cardService.save(silver);
//			tatiana.addCards(silver);
//			cardRepository.save(silver);
		};

	}
}
