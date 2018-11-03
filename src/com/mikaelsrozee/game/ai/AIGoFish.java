package com.mikaelsrozee.game.ai;

import com.mikaelsrozee.game.Game;
import com.mikaelsrozee.game.objects.Player;

public class AIGoFish extends Player {
    
  public AIGoFish(String id) {
      super(id);
  }
    
  @Override
  public void takeTurn(Game game) {
      boolean hasNextTurn = true;
      while(hasNextTurn) {
          /* Count all cards known about */
          /* Request a card known about, or random if not */
          /* If move passed, you have next turn */
          /* Else go fish, you do not have next turn */
      }
  }
    
}
