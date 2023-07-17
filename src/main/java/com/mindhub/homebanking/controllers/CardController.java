package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
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
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType type, @RequestParam ColorType color, Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());
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
            Random random = new Random();
        int cvv = CardUtils.getCvv(random);
        do {
            cardNumber = CardUtils.getStringBuilder(random).toString();
        } while ( cardService.existsByNumber(cardNumber));
                Card card = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cvv, LocalDateTime.now().plusYears(5), LocalDateTime.now(), cardNumber);
                client.addCards(card);
                cardService.save(card);
                return new ResponseEntity<>("Account Created",HttpStatus.CREATED);
    }



}