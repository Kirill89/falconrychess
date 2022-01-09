package com.falconrychess.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.falconrychess.logic.Game;

public class GameControllerTest {

  private int dbCnt;
  private GameController gameController = GameController.getInstance();

  @Before
  public void createSomeGames() {
    dbCnt = gameController.gamesInDataBase();
  }

  @Test
  public void connectionTest() {
    assertTrue(gameController.isConnected());
  }

  @Test
  public void newGameTest() {
    int memCnt = gameController.gamesInMemory();
    String id = gameController.newGame();
    assertNotNull(id);
    assertEquals(gameController.gamesInMemory(), memCnt + 1);
    assertEquals(dbCnt + 1, gameController.gamesInDataBase());
  }

  @Test
  public void getGameTest() {
    String id = gameController.newGame();
    Game game = gameController.getGame(id);
    assertNotNull(game);
  }

  @Test
  public void saveAndRemoveNotActiveGamesTest() throws InterruptedException {
    int memCnt = gameController.gamesInMemory();
    int dbCnt = gameController.gamesInDataBase();

    String id0 = gameController.newGame();
    String id1 = gameController.newGame();
    String id2 = gameController.newGame();

    assertEquals(dbCnt + 3, gameController.gamesInDataBase());
    assertEquals(memCnt + 3, gameController.gamesInMemory());

    Thread.sleep(10000);

    gameController.getGame(id0);
    gameController.getGame(id1);

    Thread.sleep(22000);

    assertEquals(dbCnt + 3, gameController.gamesInDataBase());
    assertEquals(memCnt + 2, gameController.gamesInMemory());

    gameController.getGame(id2);

    assertEquals(memCnt + 3, gameController.gamesInMemory());
  }
}
