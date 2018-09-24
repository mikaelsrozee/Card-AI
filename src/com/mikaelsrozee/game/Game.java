package com.mikaelsrozee.game;

import com.mikaelsrozee.game.objects.Deck;
import com.mikaelsrozee.game.objects.Player;
import java.util.LinkedList;
import java.util.List;

public class Game {

  private String id;
  private Deck deck;
  private List<Player> players = new LinkedList<>();
  private Player currentTurn;

  public Game() {
    this("Blank game");
  }

  public Game(String id) {
    this.deck = new Deck();
    this.id = id;
  }

  public void startNewGame() {

  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public Player getPlayerById(String ID) {
    for (Player player : players) {
      if (player.getId().equals(ID)) {
        return player;
      }
    }
    System.out.println("[ERROR] No player found with ID '" + ID + "'.");
    return null;
  }

  public Deck dealToAllPlayers() {
    for (Player player : players) {
      player.drawFromDeck(deck);
    }
    return deck;
  }

  public Deck dealToAllPlayers(int quantity) {
    for (int i = 0; i < quantity; i++) {
      dealToAllPlayers();
    }
    return deck;
  }

  public Deck dealAllCards() {
    while (deck.getCards().size() > 0) {
      for (Player player : players) {
        player.drawFromDeck(deck);
      }
    }
    return deck;
  }

  public Deck getDeck() {
    return deck;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setDeck(Deck deck) {
    this.deck = deck;
  }

  public Player getCurrentTurn() {
    return currentTurn;
  }

  public void setCurrentTurn(Player currentTurn) {
    this.currentTurn = currentTurn;
  }

  public String getId() {
    return id;
  }
}
