package com.mikaelsrozee.game.objects;

import static java.util.Collections.swap;

import com.mikaelsrozee.game.objects.Card.EnumSuit;
import com.mikaelsrozee.game.objects.Card.EnumValue;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;

public class Deck {

  private List<Card> cards = new LinkedList<>();

  public Deck() {
    for (EnumSuit suit : EnumSuit.values()) {
      for (EnumValue value : EnumValue.values()) {
        cards.add(new Card(suit, value));
      }
    }
  }

  public Deck(List<Card> cards) {
    this.cards = cards;
  }

  public Deck shuffle() {
    List<Card> list = cards;
    Random rnd = new Random();

    int size = list.size();
    if (size < 5 || list instanceof RandomAccess) {
      for (int i = size; i > 1; i--) {
        swap(list, i - 1, rnd.nextInt(i));
      }
    } else {
      Object arr[] = list.toArray();

      for (int i = size; i > 1; i--) {
        swap(Arrays.asList(arr), i - 1, rnd.nextInt(i));
      }

      ListIterator it = list.listIterator();
      for (Object anArr : arr) {
        it.next();
        it.set(anArr);
      }
    }

    return new Deck(list);
  }

  public List<Card> getCards() {
    return this.cards;
  }

}
