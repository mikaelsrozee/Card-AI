package com.mikaelsrozee.game.games;

import com.mikaelsrozee.game.Game;

public class GameGoFish extends Game {
    
    private int turnNumber = 0;
    
    public GameGoFish() {
        super("GameGoFish");
    }
    
    @Override
    public void startNewGame() {
        System.out.println("[DEBUG] Request to start game of GameGoFish received.");
        dealToAllPlayers(5);
        
        if (getPlayers().size() == 2)
            dealToAllPlayers(2);
        
        setCurrentTurn(getPlayers().get(getNextTurnIndex()));
        
        while (/* unless someone runs out of cards or deck is empty */          true) {    
            turnNumber++;
            
            /* For the sake of formatting */
            System.out.println();
            System.out.println();
            System.out.println();

            /* Print the current turn status */
            System.out.println(
            "[DEBUG] It is " + getCurrentTurn().getId() + "'s turn. This is turn " + turnNumber
              + ".");
            // reportHandStatuses(this); TODO
            
            /* Instruct the current player to perform their turn. */
            getCurrentTurn().takeTurn(this);

            /* Print information about the state of the game after their turn. */
            System.out.println("[DEBUG] " + getCurrentTurn().getId() + " has taken their turn.");
            
            /* Next player is the one who called go fish */
        }
        
        /* Winner is whoever has the most sets of four */
    }
    
    private int getNextTurnIndex() {
        int index = turnNumber;
        while (index >= getPlayers().size()) {
            index -= getPlayers().size();
        }
    return index;
  }
    
}
