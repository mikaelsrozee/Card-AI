package com.mikaelsrozee;

import com.mikaelsrozee.game.ai.AICheat;
import com.mikaelsrozee.game.games.GameCheat;

public class CardAI {

  public static void main(String[] args) {
    GameCheat game = new GameCheat();
    game.addPlayer(new AICheat("Adam"));
    game.addPlayer(new AICheat("Brenda"));
    game.addPlayer(new AICheat("Charlie"));
    game.addPlayer(new AICheat("Debbie"));
    game.addPlayer(new AICheat("Edward"));
    game.addPlayer(new AICheat("Florence"));
    game.setDeck(game.getDeck().shuffle());

    System.out.println("[LOG] Starting new game of " + game.getId());
    game.startNewGame();
  }

}
