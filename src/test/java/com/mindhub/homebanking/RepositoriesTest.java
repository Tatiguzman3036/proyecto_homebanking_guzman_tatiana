package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.mindhub.homebanking.models.ColorType.GOLD;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }
    @Test
    public void existTypeTransactions(){
        List<Transaction> transaction = transactionRepository.findAll();
        assertThat(transaction, hasItem(hasProperty("type", is(CREDIT))));
    }
    @Test
    public void existAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }
    @Test
    public void existNumberAccount(){
        List<Account> account = accountRepository.findAll();
        assertThat(account, hasItem(hasProperty("number", is(not(empty())))));
    }
    @Test
    public void existCard(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }
    @Test
    public void existGold(){
        List<Card> card = cardRepository.findAll();
        assertThat(card, hasItem(hasProperty("color", is(GOLD))));
    }
    @Test
    public void existClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }
    @Test
    public void existEmailClient(){
        List<Client> client = clientRepository.findAll();
        assertThat(client, hasItem(hasProperty("email", is(not(empty())))));
    }
    @Test
    public void existPasswordClient(){
        List<Client> client = clientRepository.findAll();
        assertThat(client, hasItem(hasProperty("password", is(not(empty())))));
    }
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));

    }
    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

}
