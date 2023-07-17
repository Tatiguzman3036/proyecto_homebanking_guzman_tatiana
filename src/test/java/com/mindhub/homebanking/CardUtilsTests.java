package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {
    Random random = new Random();
    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getStringBuilder(random).toString();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }
    @Test
    public void existLargeCardNumber(){
        String cardNumber = CardUtils.getStringBuilder(random).toString();
        assertThat(cardNumber, hasLength(19));
    }
    @Test
    public void cvvNumberCreated(){
        int cvvNumber = CardUtils.getCvv(random);
        assertThat(String.valueOf(cvvNumber), is(not(emptyOrNullString())));
    }
    @Test
    public void cvvNumber() {
        int cvvNumber = CardUtils.getCvv(random);
        assertThat(String.valueOf(cvvNumber).length(), is(3));
    }
    @Test
    public void numberAccountCreated(){
        String accountNumber = CardUtils.getRandomNumber();
        assertThat(accountNumber,is(not(emptyOrNullString())));
    }
    @Test
    public void existAccountNumber(){
        String accountNumber = CardUtils.getRandomNumber();
        assertThat(accountNumber, hasLength(12));
    }
}

