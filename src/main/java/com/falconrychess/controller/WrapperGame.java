package com.falconrychess.controller;

import com.falconrychess.logic.Game;

public class WrapperGame {
  private Game game;
  private long time;
  private String id;

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public boolean isTimeOver() {
    long time = System.currentTimeMillis() / 1000;
    if (Math.abs(time - this.time) > 30)
      return true;
    else
      return false;
  }

  public void setCurrentTime() {
    this.time = System.currentTimeMillis() / 1000;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
