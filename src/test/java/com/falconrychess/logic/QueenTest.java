package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class QueenTest {

  @Test
  public void queenTestGenerateMoveArea() {
    Field field = new Field();

    field.addFigure(new Queen(new Point(2, 2), Side.WHITE));
    field.addFigure(new Horse(new Point(1, 3), Side.BLACK));
    field.addFigure(new Horse(new Point(3, 1), Side.WHITE));

    field.getFigure(new Point(2, 2)).generateMoveArea(field);
    MoveArea moveArea = field.getFigure(new Point(2, 2)).getMoveArea();
    assertEquals(moveArea.getAction(new Point(1, 3)), Action.ATTACK);
    assertEquals(moveArea.getAction(new Point(0, 4)), Action.NONE);
    assertEquals(moveArea.getAction(new Point(3, 1)), Action.NONE);
    assertEquals(moveArea.getAction(new Point(4, 0)), Action.NONE);
    assertEquals(moveArea.getAction(new Point(9, 9)), Action.MOVE);
    assertEquals(moveArea.getAction(new Point(0, 0)), Action.MOVE);

    field = new Field();

    field.addFigure(new Queen(new Point(5, 5), Side.WHITE));
    field.addFigure(new Horse(new Point(6, 5), Side.WHITE));
    field.addFigure(new Horse(new Point(5, 8), Side.BLACK));
    field.getFigure(new Point(5, 5)).generateMoveArea(field);
    moveArea = field.getFigure(new Point(5, 5)).getMoveArea();
    Point point = new Point(0, 5);
    int moveCount = 0;
    int attackCount = 0;
    int noneCount = 0;

    while (point.getX() < Field.width) {
      if (moveArea.getAction(point).equals(Action.MOVE))
        moveCount++;
      else if (moveArea.getAction(point).equals(Action.ATTACK))
        attackCount++;
      else if (moveArea.getAction(point).equals(Action.NONE))
        noneCount++;
      point.addX(1);
    }
    assertEquals(moveCount, 5);
    assertEquals(attackCount, 0);
    assertEquals(noneCount, 5);

    moveCount = 0;
    attackCount = 0;
    noneCount = 0;
    point = new Point(5, 0);
    while (point.getY() < Field.height) {
      if (moveArea.getAction(point).equals(Action.MOVE))
        moveCount++;
      else if (moveArea.getAction(point).equals(Action.ATTACK))
        attackCount++;
      else if (moveArea.getAction(point).equals(Action.NONE))
        noneCount++;
      point.addY(1);
    }
    assertEquals(moveCount, 7);
    assertEquals(attackCount, 1);
    assertEquals(noneCount, 2);

    field.addFigure(new Horse(new Point(5, 6), Side.BLACK));
    field.getFigure(new Point(5, 5)).generateMoveArea(field);

    moveCount = 0;
    attackCount = 0;
    noneCount = 0;
    point = new Point(5, 0);
    while (point.getY() < Field.height) {
      if (moveArea.getAction(point).equals(Action.MOVE))
        moveCount++;
      else if (moveArea.getAction(point).equals(Action.ATTACK))
        attackCount++;
      else if (moveArea.getAction(point).equals(Action.NONE))
        noneCount++;
      point.addY(1);
    }
    assertEquals(moveCount, 5);
    assertEquals(attackCount, 1);
    assertEquals(noneCount, 4);

    field.addFigure(new Horse(new Point(5, 4), Side.BLACK));
    field.getFigure(new Point(5, 5)).generateMoveArea(field);

    moveCount = 0;
    attackCount = 0;
    noneCount = 0;
    point = new Point(5, 0);
    while (point.getY() < Field.height) {
      if (moveArea.getAction(point).equals(Action.MOVE))
        moveCount++;
      else if (moveArea.getAction(point).equals(Action.ATTACK))
        attackCount++;
      else if (moveArea.getAction(point).equals(Action.NONE))
        noneCount++;
      point.addY(1);
    }
    assertEquals(moveCount, 0);
    assertEquals(attackCount, 2);
    assertEquals(noneCount, 8);

    moveCount = 0;
    attackCount = 0;
    noneCount = 0;
    field.removeFigure(new Point(6, 5));
    field.getFigure(new Point(5, 5)).generateMoveArea(field);
    point = new Point(0, 5);

    while (point.getX() < Field.width) {
      if (moveArea.getAction(point).equals(Action.MOVE))
        moveCount++;
      else if (moveArea.getAction(point).equals(Action.ATTACK))
        attackCount++;
      else if (moveArea.getAction(point).equals(Action.NONE))
        noneCount++;
      point.addX(1);
    }
    assertEquals(moveCount, 9);
    assertEquals(attackCount, 0);
    assertEquals(noneCount, 1);
  }

}
