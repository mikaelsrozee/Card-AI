package com.mikaelsrozee.game.games;

import com.mikaelsrozee.game.Game;
import com.mikaelsrozee.game.ai.AICheat;
import com.mikaelsrozee.game.objects.Card;
import com.mikaelsrozee.game.objects.Card.EnumValue;
import com.mikaelsrozee.game.objects.Deck;
import com.mikaelsrozee.game.objects.Player;
import java.util.LinkedList;
import java.util.List;

public class GameCheat extends Game {

  private int turnNumber, previousMoveQuantity = 0;
  private EnumValue previousMoveValue = null;
  private Player previousPlayer = null;
  private List<Player> remainingPlayers = new LinkedList<>();

  public GameCheat() {
    super("GameCheat");
  }

  @Override
  public void startNewGame() {
    System.out.println("[DEBUG] Request to start game of GameCheat received.");
    remainingPlayers.addAll(getPlayers());
    System.out.println("[DEBUG] Players in game: " + getPlayers().toString());
    dealAllCards();

    /* Loop the game until there are only 2 players remaining */
    while (remainingPlayers.size() > 2) {
      /* Set the current turn to the next player. */
      setCurrentTurn(remainingPlayers.get(getNextTurnIndex()));
      turnNumber++;

      /* For the sake of formatting */
      System.out.println();
      System.out.println();
      System.out.println();

      /* Print the current turn status */
      System.out.println(
          "[DEBUG] It is " + getCurrentTurn().getId() + "'s turn. This is turn " + turnNumber
              + ".");
      reportHandStatuses(this);

      /* Check to see if a player has successfully lost all their cards. */
      if (previousPlayer != null) {
        if (previousPlayer.getHeldCards().size() == 0) {
          remainingPlayers.remove(previousPlayer);
          System.out.println("[DEBUG] " + previousPlayer.getId()
              + " has ran out of cards and so won the game.");

          setCurrentTurn(remainingPlayers.get(getNextTurnIndex()));
          turnNumber++;

          System.out.println(
              "[DEBUG] It is " + getCurrentTurn().getId() + "'s turn. This is turn " + turnNumber
                  + ".");

          /* If there are no longer enough players, end the game. */
          if (remainingPlayers.size() < 3)
            break;
        }
      }

      /* Instruct the current player to perform their turn. */
      getCurrentTurn().takeTurn(this);

      /* Print information about the state of the game after their turn. */
      System.out.println("[DEBUG] " + getCurrentTurn().getId() + " has taken their turn.");

      /* Instruct the other players to call cheat if they want to. */
      previousPlayer = getCurrentTurn();
      for (Player player : remainingPlayers) {
        if (!previousPlayer.equals(player)) {
          player.onOtherPlayersGo(this);
        }
      }
    }

    System.out.println("[LOG] Game of GameCheat has ended.");
    StringBuilder losers = new StringBuilder();
    losers.append("[LOG] Losers:");
    for (Player loser : remainingPlayers)
      losers.append(" [").append(loser.getId()).append("]");
    System.out.println(losers.toString());
  }

  public void callCheat(Player player) {
    if (getDeck().getCards().size() > 0) {
      System.out.println(
          "[DEBUG] Player " + player.getId() + " has called cheat on " + previousPlayer.getId()
              + ".");
      List<Card> cards = getDeck().getCards();

      /* Clear the memory of the current deck */
      for (Player aPlayer : getPlayers()) {
        if (aPlayer instanceof AICheat) {
          ((AICheat) aPlayer).clearMemory();
        }
      }

      boolean wasCheating = false;
      for (int i = 1; i < previousMoveQuantity; i++) {
        if (!(previousMoveValue.equals(cards.get(cards.size() - i).getValue()))) {
          List<Card> newHeldCards = previousPlayer.getHeldCards();
          newHeldCards.addAll(cards);
          setDeck(new Deck(new LinkedList<>()));
          previousPlayer.setHeldCards(newHeldCards);
          wasCheating = true;
          System.out.println("[DEBUG] Player " + previousPlayer.getId() + " was cheating.");
          break;
        }
      }

      if (!wasCheating) {
        List<Card> newHeldCards = player.getHeldCards();
        newHeldCards.addAll(cards);
        setDeck(new Deck(new LinkedList<>()));
        player.setHeldCards(newHeldCards);
        System.out.println("[DEBUG] Player " + previousPlayer.getId() + " was not cheating.");
      }
    }
  }

  private void reportHandStatuses(Game game) {
    for (Player player : game.getPlayers()) {
      if (this.remainingPlayers.contains(player)) {
        System.out.println(
            "[DEBUG] " + player.getId() + " has " + player.getHeldCards().size() + " cards:");
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : player.getHeldCards()) {
          stringBuilder.append(card.getSuit()).append("/").append(card.getValue()).append(", ");
        }
        System.out.println(stringBuilder.toString());
      }
    }

    System.out.println("[DEBUG] " + "The pile has " + game.getDeck().getCards().size() + " cards.");
  }

  private int getNextTurnIndex() {
    int index = turnNumber;
    while (index >= remainingPlayers.size()) {
      index -= remainingPlayers.size();
    }
    return index;
  }

  public int getPreviousMoveQuantity() {
    return previousMoveQuantity;
  }

  public void setPreviousMoveQuantity(int previousMoveQuantity) {
    this.previousMoveQuantity = previousMoveQuantity;
  }

  public EnumValue getPreviousMoveValue() {
    return previousMoveValue;
  }

  public void setPreviousMoveValue(EnumValue previousMoveValue) {
    this.previousMoveValue = previousMoveValue;
  }

  public List<Player> getRemainingPlayers() {
    return remainingPlayers;
  }

  public void setRemainingPlayers(List<Player> remainingPlayers) {
    this.remainingPlayers = remainingPlayers;
  }
}
