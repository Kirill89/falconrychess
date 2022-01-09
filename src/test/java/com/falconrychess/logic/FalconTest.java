package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class FalconTest {

  @Test
  public void falconTestGenerateMoveArea() {
    Field field = new Field();

    Point point = new Point();
    field.addFigure(new Falcon(new Point(4, 5), Side.WHITE));

    field.addFigure(new Horse(new Point(3, 4), Side.WHITE));
    field.addFigure(new Horse(new Point(6, 3), Side.BLACK));
    field.addFigure(new Horse(new Point(7, 4), Side.BLACK));
    field.getFigure(new Point(4, 5)).generateMoveArea(field);
    MoveArea moveArea = field.getFigure(new Point(4, 5)).getMoveArea();

    int moveCount = 0;
    int attackCount = 0;
    int noneCount = 0;

    for (int i = 0; i < Field.width; i++) {
      for (int j = 0; j < Field.height; j++) {
        point.setXY(i, j);
        switch (moveArea.getAction(point)) {
          case ATTACK:
            attackCount++;
            break;
          case MOVE:
            moveCount++;
            break;
          case NONE:
            noneCount++;
            break;
        }
      }
    }

    assertEquals(moveCount, 14);
    assertEquals(attackCount, 2);
    assertEquals(noneCount, 100 - 2 - 14);
    assertEquals(moveArea.getAction(new Point(1, 2)), Action.NONE);
    assertEquals(moveArea.getAction(new Point(7, 2)), Action.NONE);
    assertEquals(moveArea.getAction(new Point(1, 8)), Action.MOVE);
    assertEquals(moveArea.getAction(new Point(7, 8)), Action.MOVE);
    assertEquals(moveArea.getAction(new Point(6, 3)), Action.ATTACK);
  }

}
