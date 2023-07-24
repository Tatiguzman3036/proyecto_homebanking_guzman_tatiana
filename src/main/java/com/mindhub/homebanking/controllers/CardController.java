package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController @RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType type, @RequestParam ColorType color, Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());
        // Verifico si el cliente ya tiene 3 tarjetas del mismo tipo
        if (client.getCards().stream().filter(card -> card.getType() == type & card.getStatus()).count() >= 3) {
            return new ResponseEntity<>("Exceeded maximum number of cards", HttpStatus.FORBIDDEN);
        }
        //verifico si ya existe la tarjeta x tipo y color y estado
        if (client.getCards().stream().anyMatch(card -> card.getType() == type && card.getColor() == color && card.getStatus())){
            return new ResponseEntity<>("A card already exists", HttpStatus.FORBIDDEN);
        }
            // Genero número de tarjeta único
            String cardNumber = " ";
            Random random = new Random();
        int cvv = CardUtils.getCvv(random);
        do {
            cardNumber = CardUtils.getStringBuilder(random).toString();
        } while ( cardService.existsByNumber(cardNumber));
                Card card = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cvv, LocalDate.now().plusYears(5), LocalDate.now(), cardNumber,true);
                client.addCards(card);
                cardService.save(card);
                return new ResponseEntity<>("Account Created",HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/cards/active")
    public List<CardDTO> cardsActive (Authentication authentication){
        Client client =  clientService.findByEmail(authentication.getName());
        return client.getCards().stream().map(card -> new CardDTO(card)).filter(cardDTO -> cardDTO.getStatus()).collect(Collectors.toList());
    }
    @PostMapping("/clients/current/cards/renew")
    public ResponseEntity<Object> renewCard(@RequestParam String number, Authentication authentication){
        if (!authentication.isAuthenticated()){
            return new ResponseEntity<>("The client is not authenticated", HttpStatus.FORBIDDEN);
        }
        if (number.isBlank()){
            return new ResponseEntity<>("Missing card number", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        Card card = cardService.findByNumber(number);
        if (!card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("Card is not expired", HttpStatus.FORBIDDEN);
        }
        if (client.getCards().stream().noneMatch(card1 -> card1.getNumber().equals(number))){
            return new ResponseEntity<>("The card does not belong to the client.",HttpStatus.FORBIDDEN);
        }
        card.setStatus(false);
        cardService.save(card);
        createCard(card.getType(),card.getColor(),authentication);
        return new ResponseEntity<>("Card renew", HttpStatus.OK);
    }

    @PatchMapping("/clients/current/cards/{id}/desactivate")
    public ResponseEntity<Object> deleteCard(@PathVariable Long id, Authentication authentication){
        if (authentication == null){
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }
        if (!authentication.isAuthenticated()){
            return new ResponseEntity<>("Authentication no exist", HttpStatus.UNAUTHORIZED);
        }
        if (id == null){
            return new ResponseEntity<>("Id is required", HttpStatus.UNAUTHORIZED);
        }
        Client client = clientService.findByEmail(authentication.getName());
        if (client == null){
            return new ResponseEntity<>("Client no exist", HttpStatus.UNAUTHORIZED);
        }
        Card card = cardService.findById(id);
        if (card == null){
            return new ResponseEntity<>("Card is not exist", HttpStatus.NOT_FOUND);
        }
        if (!card.getClient().equals(client)){
            return new ResponseEntity<>("The authenticated client is not the legitimate owner of the card.", HttpStatus.FORBIDDEN);
        }
        if (!client.getCards().contains(card)){
            return new ResponseEntity<>("The card is not present in the client's list of cards.", HttpStatus.FORBIDDEN);
        }
        card.setStatus(false);
        cardService.save(card);
           return new ResponseEntity<>("Card deactivated successfully", HttpStatus.OK);
       }

}