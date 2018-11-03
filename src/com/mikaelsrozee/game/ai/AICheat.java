package com.mikaelsrozee.game.ai;

import com.mikaelsrozee.game.Game;
import com.mikaelsrozee.game.games.GameCheat;
import com.mikaelsrozee.game.objects.Card;
import com.mikaelsrozee.game.objects.Card.EnumValue;
import com.mikaelsrozee.game.objects.Player;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AICheat extends Player {

  private HashMap<EnumValue, Integer> playedCardsMap = new HashMap<>();

  public AICheat(String id) {
    super(id);
  }

  /**
   * Called at the start of the player's turn if it is their turn.
   */
  @Override
  public void takeTurn(Game game) {
    System.out.println("[DEBUG] Player ID '" + getId() + "' is now taking their turn.");

    switch (game.getId()) {
      case "GameCheat":
        GameCheat cheat = (GameCheat) game;
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
          if (playedCardIndex != values.size() - 1) {
            valueAbove = values.get(playedCardIndex + 1);
          }

          if (playedCardIndex > 0) {
            valueBelow = values.get(playedCardIndex - 1);
          }
        }

        /* Create a map of the quantity of each value in the hand. */
        HashMap<EnumValue, Integer> cardCounter = new HashMap<>();

        for (Card card : getHeldCards()) {
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
          valueToPlay = valueWithMaxQuantity;
          quantityToPlay = maxQuantity;
        }
        /* If there are more than 3 players remaining, play safe. */
        else if (cheat.getRemainingPlayers().size() > 3) {
          /* Search values to see if a 'truthful' move can be made. */
          for (EnumValue value : cardCounter.keySet()) {
            if (value.equals(playedCardValue) || value.equals(valueAbove) || value
                .equals(valueBelow)) {
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
            valueToPlay = getHeldCards().get(rand.nextInt(getHeldCards().size())).getValue();
            quantityToPlay = cardCounter.get(valueToPlay);
          }
        }
        else /* If there are three players remaining, play risky. */ {
          valueToPlay = valueWithMaxQuantity;
          quantityToPlay = maxQuantity;
        }

        /* Make the move decided. */
        Iterator<Card> iterator = getHeldCards().iterator();
        System.out.println("[DEBUG] " + getId() + " played " + quantityToPlay + " " + valueToPlay);

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

        /* Save move made to memory */
        if (!playedCardsMap.containsKey(valueToPlay))
          playedCardsMap.put(valueToPlay, quantityToPlay);
        else playedCardsMap.replace(valueToPlay, playedCardsMap.get(valueToPlay) + quantityToPlay);
    }
  }

  /**
   * Called whenever another player has made a move, but before their turn is over.
   */
  @Override
  public void onOtherPlayersGo(Game game, String flag) {
    if (!Objects.equals(game.getId(), "GameCheat"))
      return;

    GameCheat cheat = (GameCheat) game;

    /* Create a map of the quantity of each value that the AI can account for. */
    HashMap<EnumValue, Integer> cardCounter = new HashMap<>();

    /* Add held cards to cardCounter */
    for (Card card : getHeldCards()) {
      if (!cardCounter.containsKey(card.getValue())) {
        cardCounter.put(card.getValue(), 1);
      } else {
        cardCounter.put(card.getValue(), cardCounter.get(card.getValue()) + 1);
      }
    }

    /* Add previously played cards to cardCounter */
    for (EnumValue value : playedCardsMap.keySet()) {
      if (!cardCounter.containsKey(value)) {
        cardCounter.put(value, playedCardsMap.get(value));
      } else {
        cardCounter.replace(value, cardCounter.get(value) + playedCardsMap.get(value));
      }
    }

    boolean callingCheat = false;

    /* If you can account for enough of that card for them to by lying, call cheat. */
    if (cardCounter.containsKey(cheat.getPreviousMoveValue())) {
      if (cardCounter.get(cheat.getPreviousMoveValue()) > 4 - cheat.getPreviousMoveQuantity()) {
        System.out.println("[DEBUG] " + getId() + " is calling cheat because they know about " + cardCounter.get(cheat.getPreviousMoveValue()) + " " + cheat.getPreviousMoveValue());
        callingCheat = true;
      }
    }

    /* If that was the player's final move */
    if (cheat.getCurrentTurn().getHeldCards().size() == 0) {
      int pos = 0;
      for (Player player : cheat.getRemainingPlayers()) {
        if (game.getDeck().getCards().size() + getHeldCards().size() >= player.getHeldCards()
            .size()) {
          pos++;
        }
      }

      /* and if calling cheat won't cause you to be in a losing situation. */
      if (pos < 2) {
        System.out.println("[DEBUG] " + getId() + " is calling cheat because it was " + cheat.getCurrentTurn().getId() + "'s final turn and it was safe to do so.");
        callingCheat = true;
      }
    }

    /* If calling cheat, call cheat. */
    if (callingCheat) {
      cheat.callCheat(this);
    }

    /* Save this move to memory */
    if (!playedCardsMap.containsKey(cheat.getPreviousMoveValue())) {
      playedCardsMap.put(cheat.getPreviousMoveValue(), cheat.getPreviousMoveQuantity());
    } else {
      playedCardsMap.replace(cheat.getPreviousMoveValue(), playedCardsMap.get(cheat.getPreviousMoveValue()) + cheat.getPreviousMoveQuantity());
    }
  }

  public void clearMemory() {
    playedCardsMap.clear();
  }
}
