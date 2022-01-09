package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class PawnTest {

  @Test
  public void pawnGenerateMoveArea() {
    Field field = new Field();
    Point whitePawn = new Point(1, 8);
    Point blackPawn = new Point(0, 6);
    Point point;
    MoveArea whiteMoveArea;
    MoveArea blackMoveArea;

    field.addFigure(new Pawn(whitePawn, Side.WHITE));
    field.addFigure(new Pawn(new Point(9, 8), Side.WHITE));
    field.addFigure(new Pawn(blackPawn, Side.BLACK));
    field.addFigure(new Horse(new Point(2, 7), Side.BLACK));

    field.getFigure(whitePawn).generateMoveArea(field);
    field.getFigure(blackPawn).generateMoveArea(field);
    whiteMoveArea = field.getFigure(whitePawn).getMoveArea();
    blackMoveArea = field.getFigure(blackPawn).getMoveArea();

    point = new Point(blackPawn);
    point.addY(+1);
    assertEquals(blackMoveArea.getAction(point), Action.MOVE);
    point.addY(+1);
    assertEquals(blackMoveArea.getAction(point), Action.MOVE);
    point.addXY(+1, -1);
    assertEquals(blackMoveArea.getAction(point), Action.NONE);

    point = new Point(whitePawn);
    point.addY(-1);
    assertEquals(whiteMoveArea.getAction(point), Action.MOVE);
    point.addY(-1);
    assertEquals(whiteMoveArea.getAction(point), Action.MOVE);
    point.addY(+1);
    point.addX(+1);
    assertEquals(whiteMoveArea.getAction(point), Action.ATTACK);
    point.addX(-2);
    assertEquals(whiteMoveArea.getAction(point), Action.NONE);

    field.moveFigure(blackPawn, new Point(0, 8));
    field.getFigure(whitePawn).generateMoveArea(field);

    assertEquals(whiteMoveArea.getAction(point), Action.SPECIAL);

    field = new Field();

    field.addFigure(new Pawn(new Point(1, 8), Side.WHITE));
    field.addFigure(new Pawn(new Point(9, 8), Side.WHITE));
    field.addFigure(new Pawn(new Point(0, 6), Side.BLACK));
    field.addFigure(new Pawn(new Point(5, 6), Side.BLACK));

    field.getFigure(new Point(1, 8)).generateMoveArea(field);
    field.getFigure(new Point(0, 6)).generateMoveArea(field);
    field.getFigure(new Point(9, 8)).generateMoveArea(field);
    field.getFigure(new Point(5, 6)).generateMoveArea(field);

    field.moveFigure(new Point(0, 6), new Point(0, 8));
    field.getFigure(new Point(1, 8)).generateMoveArea(field);
    assertEquals(
      field.getFigure(new Point(1, 8)).getMoveArea()
        .getAction(new Point(0, 7)), Action.SPECIAL);

    field.moveFigure(new Point(9, 8), new Point(9, 7));
    field.moveFigure(new Point(5, 6), new Point(5, 7));

    field.getFigure(new Point(1, 8)).generateMoveArea(field);
    whiteMoveArea = field.getFigure(new Point(1, 8)).getMoveArea();
    assertEquals(whiteMoveArea.getAction(new Point(0, 7)), Action.NONE);
  }

  @Test
  public void specialMoveTest() {
    Field field = new Field();
    field.addFigure(new Pawn(new Point(3, 2), Side.BLACK));
    field.addFigure(new Pawn(new Point(4, 4), Side.WHITE));

    field.getFigure(new Point(4, 4)).generateMoveArea(field);
    field.moveFigure(new Point(4, 4), new Point(4, 2));


    field.getFigure(new Point(3, 2)).generateMoveArea(field);
    assertEquals(
      field.getFigure(new Point(3, 2)).getMoveArea()
        .getAction(new Point(4, 3)), Action.SPECIAL);

    field = new Field();
    field.addFigure(new Prince(new Point(3, 2), Side.BLACK));
    field.addFigure(new Pawn(new Point(4, 4), Side.WHITE));

    field.getFigure(new Point(4, 4)).generateMoveArea(field);
    field.moveFigure(new Point(4, 4), new Point(4, 2));


    field.getFigure(new Point(3, 2)).generateMoveArea(field);
    assertEquals(
      field.getFigure(new Point(3, 2)).getMoveArea()
        .getAction(new Point(4, 3)), Action.SPECIAL);

    field = new Field();
    field.addFigure(new Prince(new Point(3, 2), Side.BLACK));
    field.addFigure(new Prince(new Point(4, 4), Side.WHITE));

    field.getFigure(new Point(4, 4)).generateMoveArea(field);
    field.moveFigure(new Point(4, 4), new Point(4, 2));


    field.getFigure(new Point(3, 2)).generateMoveArea(field);
    assertEquals(
      field.getFigure(new Point(3, 2)).getMoveArea()
        .getAction(new Point(4, 3)), Action.SPECIAL);

    field = new Field();
    field.addFigure(new Pawn(new Point(3, 2), Side.BLACK));
    field.addFigure(new Prince(new Point(4, 4), Side.WHITE));

    field.getFigure(new Point(4, 4)).generateMoveArea(field);
    field.moveFigure(new Point(4, 4), new Point(4, 2));


    field.getFigure(new Point(3, 2)).generateMoveArea(field);
    assertEquals(
      field.getFigure(new Point(3, 2)).getMoveArea()
        .getAction(new Point(4, 3)), Action.SPECIAL);
  }

}
