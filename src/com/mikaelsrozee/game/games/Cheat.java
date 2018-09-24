package com.mikaelsrozee.game.games;

import com.mikaelsrozee.Main;
import com.mikaelsrozee.game.Game;
import com.mikaelsrozee.game.objects.Card;
import com.mikaelsrozee.game.objects.Card.EnumValue;
import com.mikaelsrozee.game.objects.Deck;
import com.mikaelsrozee.game.objects.Player;
import java.util.LinkedList;
import java.util.List;

public class Cheat extends Game {

  private int turnNumber, previousMoveQuantity = 0;
  private EnumValue previousMoveValue = null;
  private Player previousPlayer = null;
  private List<Player> remainingPlayers = new LinkedList<>();

  public Cheat() {
    super("Cheat");
  }

  @Override
  public void startNewGame() {
    System.out.println("[DEBUG] Request to start game of Cheat received.");
    remainingPlayers.addAll(getPlayers());
    System.out.println("[DEBUG] Players in game: " + getPlayers().toString());
    dealAllCards();

    while (remainingPlayers.size() > 2) {
      setCurrentTurn(remainingPlayers.get(getNextTurnIndex()));
      turnNumber++;

      if (getCurrentTurn().getHeldCards().size() == 0) {
        remainingPlayers.remove(getCurrentTurn());
        System.out.println("[DEBUG] Player " + getCurrentTurn().getId() + " has ran out of cards and so won the game.");
        setCurrentTurn(remainingPlayers.get(getNextTurnIndex()));
        turnNumber++;
      }

      System.out.println("[DEBUG] It is " + getCurrentTurn().getId() + "'s turn. This is turn " + turnNumber + ".");
      getCurrentTurn().takeTurn(this);
      System.out.println("[DEBUG] " + getCurrentTurn().getId() + " has taken their turn.");
      Main.reportHandStatuses(this);
      previousPlayer = getCurrentTurn();
      for (Player player : remainingPlayers) {
        if (!previousPlayer.equals(player))
          player.onOtherPlayersGo(this);
      }
    }

    System.out.println("[LOG] Game of Cheat has ended.");
  }

  public void callCheat(Player player) {
    if (getDeck().getCards().size() > 0) {
      System.out.println("[DEBUG] Player " + player.getId() + " has called cheat on " + previousPlayer.getId() + ".");
      List<Card> cards = getDeck().getCards();

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
      Main.reportHandStatuses(this);
    }
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
