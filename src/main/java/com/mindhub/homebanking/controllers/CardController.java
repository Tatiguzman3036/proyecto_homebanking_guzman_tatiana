package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Random;

@RestController @RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam CardType type, @RequestParam ColorType color, Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());
        long cardLimit = client.getCards().stream().filter(card -> card.getType() == type).count();
        // Verificar si el cliente ya tiene 3 tarjetas del mismo tipo
        if (cardLimit >= 3) {
            return new ResponseEntity<>("Exceeded maximum number of cards", HttpStatus.FORBIDDEN);
        }
        //verifico si ya existe la tarjeta x tipo y color
        boolean cardExist = client.getCards().stream().anyMatch(card -> card.getType() == type && card.getColor() == color);
        if (cardExist){
            return new ResponseEntity<>("A card already exists", HttpStatus.FORBIDDEN);
        }
            // Generar número de tarjeta único
            String cardNumber = " ";
            boolean cardNumberUnique = false;
            int cvv;
            Random random = new Random();
            do {
                StringBuilder cardNumberBuilder = new StringBuilder();
                cvv = random.nextInt(999);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        int digit = random.nextInt(10);
                        cardNumberBuilder.append(digit);
                    }
                    if (i < 3) {
                        cardNumberBuilder.append("-");
                    }
                    cardNumber = cardNumberBuilder.toString();
                    cardNumberUnique = cardRepository.existsByNumber(cardNumber);
                }
            } while (cardNumberUnique);
                Card card = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cvv, LocalDateTime.now().plusYears(5), LocalDateTime.now(), cardNumber);
                client.addCards(card);
                cardRepository.save(card);
                return new ResponseEntity<>(HttpStatus.CREATED);
    }
}