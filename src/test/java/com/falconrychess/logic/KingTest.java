package com.falconrychess.logic;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class KingTest {

  @Test
  public void kingGenerateMoveArea() {
    Field field = new Field();
    field.addFigure(new King(new Point(5, 9), Side.WHITE));
    field.addFigure(new Rook(new Point(1, 9), Side.WHITE));
    field.addFigure(new Rook(new Point(8, 9), Side.WHITE));
    field.addFigure(new Pawn(new Point(5, 8), Side.BLACK));

    field.getFigure(new Point(5, 9)).generateMoveArea(field);
    MoveArea moveArea = field.getFigure(new Point(5, 9)).getMoveArea();

    assertEquals(moveArea.getAction(new Point(5, 8)), Action.ATTACK);
    assertEquals(moveArea.getAction(new Point(3, 9)), Action.SPECIAL);
    assertEquals(moveArea.getAction(new Point(7, 9)), Action.SPECIAL);
    assertEquals(moveArea.getAction(new Point(6, 8)), Action.MOVE);
  }

  @Test
  public void kingCastlingRightTest() {
    // mvn package -Dtest=KingTest

    Game game = new Game();

    game.moveFigure(new Point(7, 8), new Point(7, 6));
    game.moveFigure(new Point(6, 2), new Point(6, 3));
    game.moveFigure(new Point(6, 9), new Point(7, 8));
    game.moveFigure(new Point(7, 1), new Point(7, 2));
    game.moveFigure(new Point(7, 9), new Point(8, 7));
    game.moveFigure(new Point(0, 1), new Point(0, 3));
    game.moveFigure(new Point(5, 9), new Point(7, 9));

    assertEquals(game.getFieldObj().getFigure(new Point(7, 9)).getClassName(), "King");
    assertEquals(game.getFieldObj().getFigure(new Point(6, 9)).getClassName(), "Rook");

    // To cover known issue: castling rollback last opponent's move.
    assertNotNull(game.getFieldObj().getFigure(new Point(0, 3)));
  }

}
