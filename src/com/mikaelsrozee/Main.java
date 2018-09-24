package com.mikaelsrozee;

import com.mikaelsrozee.game.Game;
import com.mikaelsrozee.game.games.Cheat;
import com.mikaelsrozee.game.objects.Card;
import com.mikaelsrozee.game.objects.Player;

public class Main {

  public static void main(String[] args) {
    Cheat game = new Cheat();
    game.addPlayer(new Player("Adam"));
    game.addPlayer(new Player("Brenda"));
    game.addPlayer(new Player("Charlie"));
    game.addPlayer(new Player("Debbie"));
    game.addPlayer(new Player("Edward"));
    game.addPlayer(new Player("Florence"));
    game.setDeck(game.getDeck().shuffle());

    System.out.println("[LOG] Starting new game of " + game.getId());
    game.startNewGame();
  }

  public static void reportHandStatuses(Game game) {
    for (Player player : game.getPlayers()) {
      System.out.println("[DEBUG] " + player.getId() + " has " + player.getHeldCards().size() + " cards:");
      StringBuilder stringBuilder = new StringBuilder();
      for (Card card : player.getHeldCards()) {
        stringBuilder.append(card.getSuit()).append("/").append(card.getValue()).append(", ");
      }
      System.out.println(stringBuilder.toString());
    }

    System.out.println("[DEBUG] " + "The deck has " + game.getDeck().getCards().size() + " cards.");
  }

}
