package com.mikaelsrozee;

import com.mikaelsrozee.game.ai.CheatAI;
import com.mikaelsrozee.game.games.Cheat;

public class Main {

  public static void main(String[] args) {
    Cheat game = new Cheat();
    game.addPlayer(new CheatAI("Adam"));
    game.addPlayer(new CheatAI("Brenda"));
    game.addPlayer(new CheatAI("Charlie"));
    game.addPlayer(new CheatAI("Debbie"));
    game.addPlayer(new CheatAI("Edward"));
    game.addPlayer(new CheatAI("Florence"));
    game.setDeck(game.getDeck().shuffle());

    System.out.println("[LOG] Starting new game of " + game.getId());
    game.startNewGame();
  }

}
