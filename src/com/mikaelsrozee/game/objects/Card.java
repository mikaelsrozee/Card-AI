package com.mikaelsrozee.game.objects;

public class Card {

  private EnumSuit suit;
  private EnumValue value;

  public Card(EnumSuit suit, EnumValue value) {
    this.suit = suit;
    this.value = value;
  }

  public EnumSuit getSuit() {
    return suit;
  }

  public EnumValue getValue() {
    return value;
  }

  public enum EnumSuit {
    HEARTS,
    DIAMONDS,
    CLUBS,
    SPADES
  }

  public enum EnumValue {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING
  }
}
