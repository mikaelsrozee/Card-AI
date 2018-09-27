package com.mikaelsrozee.game.objects;

import com.mikaelsrozee.game.Game;
import java.util.LinkedList;
import java.util.List;

public class Player {

  private List<Card> heldCards = new LinkedList<>();
  private String id;

  public Player(String id) {
    this.id = id;
  }

  /**
   * Called at the start of the player's turn if it is their turn.
   */
  public void takeTurn(Game game) {

  }

  /**
   * Called whenever another player has made a move, but before their turn is over.
   */
  public void onOtherPlayersGo(Game game) {

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
