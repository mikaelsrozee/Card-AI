package com.mikaelsrozee.game.objects;

import com.mikaelsrozee.game.Game;
import com.mikaelsrozee.game.games.Cheat;
import com.mikaelsrozee.game.objects.Card.EnumValue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Player {

  private List<Card> heldCards = new LinkedList<>();
  private String id;

  public Player(String id) {
    this.id = id;
  }

  public Player(String id, List<Card> heldCards) {
    this.id = id;
    this.heldCards = heldCards;
  }

  /** Called at the start of the player's turn if it is their turn. */
  public void takeTurn(Game game) {
    System.out.println("[DEBUG] Player ID '" + getId() + "' is now taking their turn.");

    switch (game.getId()) {
      case "Cheat":
        Cheat cheat = (Cheat) game;
        /* Initialise data set to base decisions off of. */
        EnumValue playedCardValue = cheat.getPreviousMoveValue();
        List<EnumValue> values = Arrays.asList(EnumValue.values());
        int playedCardIndex = values.indexOf(playedCardValue);
        EnumValue valueAbove = null;
        EnumValue valueBelow = null;
        EnumValue valueToPlay = null;
        int quantityToPlay = 0;
        EnumValue valueWithMaxQuantity = null;
        int maxQuantity = 0;

        if (playedCardIndex != -1) {
          if (playedCardIndex != values.size() - 1)
            valueAbove = values.get(playedCardIndex + 1);

          if (playedCardIndex > 0)
            valueBelow = values.get(playedCardIndex - 1);
        }

        /* Create a map of the quantity of each value in the hand. */
        HashMap<EnumValue, Integer> cardCounter = new HashMap<>();

        for (Card card : heldCards) {
          if (!cardCounter.containsKey(card.getValue())) {
            cardCounter.put(card.getValue(), 1);
          } else {
            cardCounter.put(card.getValue(), cardCounter.get(card.getValue()) + 1);
          }

          if (maxQuantity < cardCounter.get(card.getValue())) {
            valueWithMaxQuantity = card.getValue();
            maxQuantity = cardCounter.get(card.getValue());
          }
        }

        if (game.getDeck().getCards().size() == 0) {
          /* Make a random move. */
          Random rand = new Random();
          valueToPlay = heldCards.get(rand.nextInt(heldCards.size())).getValue();
          quantityToPlay = cardCounter.get(valueToPlay);
        }
        /* If there are more than 3 players remaining, play safe. */
        else if (cheat.getRemainingPlayers().size() > 3) {
          /* Search values to see if a 'truthful' move can be made. */
          for (EnumValue value : cardCounter.keySet()) {
            if (value.equals(playedCardValue) || value.equals(valueAbove) || value.equals(valueBelow)) {
              /* If this is the best truthful move found so far, set it to be played. */
              if (cardCounter.get(value) > quantityToPlay) {
                valueToPlay = value;
                quantityToPlay = cardCounter.get(value);
              }
            }
          }
          /* If no 'truth' has been found, play a lie at random. */
          if (valueToPlay == null) {
            Random rand = new Random();
            valueToPlay = heldCards.get(rand.nextInt(heldCards.size())).getValue();
            quantityToPlay = cardCounter.get(valueToPlay);
          }
        }
        /* If there are three players remaining, play risky. */
        else {
          valueToPlay = valueWithMaxQuantity;
          quantityToPlay = maxQuantity;
        }
        /* Make the move decided. */
        Iterator<Card> iterator = getHeldCards().iterator();
        while (iterator.hasNext()) {
          Card card = iterator.next();

          if (card.getValue().equals(valueToPlay)) {
            iterator.remove();
            game.getDeck().getCards().add(card);
            List<Card> newHeldCards = getHeldCards();
            newHeldCards.remove(card);
            setHeldCards(newHeldCards);
            cheat.setPreviousMoveValue(card.getValue());
            cheat.setPreviousMoveQuantity(quantityToPlay);
          }
        }
    }
  }

  /** Called whenever another player has made a move, but before their turn is over. */
  public void onOtherPlayersGo(Game game) {
    switch (game.getId()) {
      case "Cheat":
        Cheat cheat = (Cheat) game;
        /* Create a map of the quantity of each value in the hand. */
        HashMap<EnumValue, Integer> cardCounter = new HashMap<>();

        for (Card card : heldCards) {
          if (!cardCounter.containsKey(card.getValue())) {
            cardCounter.put(card.getValue(), 1);
          } else {
            cardCounter.put(card.getValue(), cardCounter.get(card.getValue()) + 1);
          }
        }

        boolean callingCheat = false;
        for (EnumValue value : cardCounter.keySet()) {
          /* If you own enough of that card for them to by lying, call cheat. */
          if (cardCounter.get(value) > 4 - cardCounter.get(value)) {
            callingCheat = true;
          }
        }
        /* If that was the player's final move */
        if (cheat.getCurrentTurn().getHeldCards().size() == 0) {
          int pos = 0;
          for (Player player : cheat.getRemainingPlayers()) {
            if (game.getDeck().getCards().size() + getHeldCards().size() >= player.getHeldCards().size()) {
              pos++;
            }
          }

          /* and if calling cheat won't cause you to be in a losing situation. */
          if (pos < 2)
            callingCheat = true;
        }

        /* If calling cheat, call cheat. */
        if (callingCheat)
          cheat.callCheat(this);
    }
  }

  public List<Card> drawFromDeck(Deck deck) {
    if (deck.getCards().size() > 0) {
      Card card = deck.getCards().get(0);
      deck.getCards().remove(card);
      heldCards.add(card);
    } else {
      System.out.println("[ERROR] Cannot draw card; deck is empty.");
    }
    return heldCards;
  }

  public List<Card> drawFromDeck(Deck deck, int quantity) {
    for (int i = 0; i < quantity; i++) {
      drawFromDeck(deck);
    }
    return heldCards;
  }

  public String getId() {
    return id;
  }

  public List<Card> getHeldCards() {
    return heldCards;
  }

  public void setHeldCards(List<Card> heldCards) {
    this.heldCards = heldCards;
  }
}
