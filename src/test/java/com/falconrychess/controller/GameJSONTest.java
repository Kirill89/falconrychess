package com.falconrychess.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class GameJSONTest {

  private GameJSON test;
  private String gameId;

  @Before
  public void addNewGame() {
    test = new GameJSON();
    Command command = new Command();
    command.action = "newGame";
    Result result = test.executeCommand(command);
    gameId = result.message;
  }

  @Test
  public void getStatusTest() {
    Command command = new Command();
    command.action = "getStatus";
    command.gameId = gameId;
    Result result = test.executeCommand(command);
    assertEquals("{\"side\":\"WHITE\",\"status\":\"NONE\"}", result.message);
    assertEquals("ok", result.status);
  }

}
