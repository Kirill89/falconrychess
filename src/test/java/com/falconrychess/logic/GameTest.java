package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

public class GameTest {
  private Game game = new Game();

  @Test
  public void getMoveAreaTest() {
    MoveArea moveArea = game.getMoveArea(new Point(2, 0));
    assertEquals(moveArea.getAction(new Point(1, 2)), Action.MOVE);
    assertEquals(moveArea.getAction(new Point(2, 2)), Action.NONE);
    assertNull(game.getMoveArea(new Point(7, 7)));
  }

  @Test
  public void nextSideTest() {
    game = new Game();
    assertEquals(game.getSide(), Side.WHITE);
    game.moveFigure(new Point(2, 9), new Point(1, 7));
    assertEquals(game.getSide(), Side.BLACK);
  }

  @Test
  public void moveFigureTest() {
    game = new Game();
    assertEquals(game.moveFigure(new Point(2, 9), new Point(1, 7)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(2, 0), new Point(1, 2)),
      Game.MoveResult.ok);

    assertEquals(game.moveFigure(new Point(1, 7), new Point(0, 5)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(1, 2), new Point(0, 4)),
      Game.MoveResult.ok);

    assertEquals(game.moveFigure(new Point(0, 5), new Point(9, 9)),
      Game.MoveResult.illegalMove);
    assertEquals(game.moveFigure(new Point(0, 4), new Point(3, 2)),
      Game.MoveResult.illegalMove);
  }

  @Test
  public void completeGameTest() {
    game = new Game();
    assertEquals(game.moveFigure(new Point(5, 7), new Point(5, 5)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(6, 2), new Point(6, 3)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 5), new Point(5, 4)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(4, 2), new Point(4, 4)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 4), new Point(4, 3)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(4, 1), new Point(4, 3)),
      Game.MoveResult.illegalMove);
    assertEquals(game.moveFigure(new Point(3, 2), new Point(4, 3)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(3, 8), new Point(4, 5)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(4, 1), new Point(4, 2)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(4, 9), new Point(0, 5)),
      Game.MoveResult.ok);
    assertEquals(game.getStatus(), GameStatus.CHECK);
    assertEquals(game.moveFigure(new Point(5, 0), new Point(4, 1)),
      Game.MoveResult.kingIsUnderAttackAfterMove);
    assertEquals(game.moveFigure(new Point(2, 0), new Point(3, 2)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(7, 8), new Point(7, 6)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(1, 1), new Point(1, 3)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(6, 9), new Point(9, 6)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(1, 0), new Point(1, 2)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(7, 9), new Point(8, 7)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(1, 3), new Point(1, 4)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(4, 5), new Point(1, 4)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(1, 2), new Point(1, 4)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 9), new Point(7, 9)),
      Game.MoveResult.ok);
    assertEquals(game.getStatus(), GameStatus.NONE);
    assertEquals(game.moveFigure(new Point(5, 2), new Point(5, 4)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 8), new Point(5, 6)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 4), new Point(5, 5)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 6), new Point(4, 5)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 5), new Point(5, 6)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(4, 5), new Point(4, 4)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 6), new Point(5, 7)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(4, 4), new Point(5, 3)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 7), new Point(5, 8)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 3), new Point(5, 2)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 8), new Point(5, 9)),
      Game.MoveResult.illegalFigureReplace);
    game.replaceFigure(new Rook(null, null));
    assertEquals(game.moveFigure(new Point(5, 8), new Point(5, 9)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(0, 5), new Point(0, 8)),
      Game.MoveResult.illegalMove);
    assertEquals(game.moveFigure(new Point(0, 5), new Point(1, 4)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 9), new Point(5, 5)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(5, 2), new Point(6, 1)),
      Game.MoveResult.ok);
    assertEquals(game.getStatus(), GameStatus.CHECK);
    assertEquals(game.moveFigure(new Point(5, 0), new Point(4, 1)),
      Game.MoveResult.ok);
    assertEquals(game.getStatus(), GameStatus.NONE);
    assertEquals(game.moveFigure(new Point(6, 1), new Point(5, 0)),
      Game.MoveResult.illegalFigureReplace);
    game.replaceFigure(new Queen(null, null));
    assertEquals(game.moveFigure(new Point(6, 1), new Point(5, 0)),
      Game.MoveResult.illegalFigureReplace);
    game.replaceFigure(new Pawn(null, null));
    assertEquals(game.moveFigure(new Point(6, 1), new Point(5, 0)),
      Game.MoveResult.illegalFigureReplace);
    game.replaceFigure(new Falcon(null, null));
    assertEquals(game.moveFigure(new Point(6, 1), new Point(5, 0)),
      Game.MoveResult.ok);
    assertEquals(game.getStatus(), GameStatus.CHECK);
    assertEquals(game.moveFigure(new Point(4, 1), new Point(5, 0)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(0, 8), new Point(0, 7)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(6, 3), new Point(6, 4)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(0, 7), new Point(0, 6)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(6, 4), new Point(6, 5)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(1, 4), new Point(9, 4)),
      Game.MoveResult.ok);
    assertEquals(game.getStatus(), GameStatus.CHECK);
    assertEquals(game.moveFigure(new Point(8, 1), new Point(8, 3)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(8, 7), new Point(7, 5)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(0, 1), new Point(0, 2)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(7, 5), new Point(8, 3)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(8, 0), new Point(8, 3)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(0, 9), new Point(0, 7)),
      Game.MoveResult.ok);
    assertEquals(game.moveFigure(new Point(7, 1), new Point(7, 3)),
      Game.MoveResult.ok);

    assertEquals(game.moveFigure(new Point(9, 4), new Point(8, 3)),
      Game.MoveResult.ok);
    assertEquals(game.getStatus(), GameStatus.CHECKMATE);

    assertEquals(game.moveFigure(new Point(5, 0), new Point(4, 1)),
      Game.MoveResult.gameIsOver);
  }

  @After
  public void removeGame() {
    game = null;
  }

}
