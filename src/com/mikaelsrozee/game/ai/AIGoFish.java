package com.mikaelsrozee.game.ai;

import com.mikaelsrozee.game.Game;
import com.mikaelsrozee.game.objects.Card;
import com.mikaelsrozee.game.objects.Card.EnumValue;
import com.mikaelsrozee.game.objects.Player;
import java.util.HashMap;

public class AIGoFish extends Player {

  private HashMap<EnumValue, Integer> playedCardsMap = new HashMap<>();

  public AIGoFish(String id) {
      super(id);
  }
    
  @Override
  public void takeTurn(Game game) {
      boolean hasNextTurn = true;
      while(hasNextTurn) {
        /* Create a map of the quantity of each value in the hand. */
        HashMap<EnumValue, Integer> cardCounter = new HashMap<>();

        for (Card card : getHeldCards()) {
          if (!cardCounter.containsKey(card.getValue())) {
            cardCounter.put(card.getValue(), 1);
          } else {
            cardCounter.put(card.getValue(), cardCounter.get(card.getValue()) + 1);
          }
        }


          /* Request a card known about, or random if not */
          /* If move passed, you have next turn */
          /* Else go fish, you do not have next turn */
      }
  }

  @Override
  public void onOtherPlayersGo(Game game, String flag) {

  }
    
}
