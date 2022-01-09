package com.falconrychess.logic;

public enum Side {
  WHITE, BLACK, NONE;

  Side getAnother() {
    if (this.equals(Side.WHITE))
      return Side.BLACK;
    if (this.equals(Side.BLACK))
      return Side.WHITE;
    return Side.NONE;
  }
}
