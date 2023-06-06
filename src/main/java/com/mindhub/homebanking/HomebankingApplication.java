package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;


@SpringBootApplication
public class  HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData (ClientRepository clientRepository, AccountRepository accountRepository){
		return args -> {
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com" );
			Client tatiana = new Client("Tatiana", "Guzman", "tatiguzmn@hotmail.com");
			Account VIN001 = new Account("VIN001", LocalDate.now(), 5000);
			Account VIN002 = new Account("VIN002", LocalDate.now().plusDays(1),7500);
			Account VIN003 = new Account("VIN003", LocalDate.now().plusDays(3), 15000);
			Account VIN004 = new Account("VIN004", LocalDate.now(), 80000);
			melba.addAccount(VIN001);
			melba.addAccount(VIN002);
			tatiana.addAccount(VIN003);
			tatiana.addAccount(VIN004);
			clientRepository.save(melba);
			clientRepository.save(tatiana);
			accountRepository.save(VIN001);
			accountRepository.save(VIN002);
			accountRepository.save(VIN003);
			accountRepository.save(VIN004);




		};

	}
}
