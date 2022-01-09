package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class DolphinTest {

  @Test
  public void dolphinTestGenerateMoveArea() {
    Field field = new Field();

    field.addFigure(new Dolphin(new Point(4, 5), Side.WHITE));

    field.addFigure(new Horse(new Point(3, 5), Side.WHITE));
    field.addFigure(new Horse(new Point(1, 5), Side.BLACK));
    field.addFigure(new Horse(new Point(6, 5), Side.BLACK));

    field.getFigure(new Point(4, 5)).generateMoveArea(field);
    MoveArea moveArea = field.getFigure(new Point(4, 5)).getMoveArea();

    assertEquals(moveArea.getAction(new Point(3, 5)), Action.NONE);
    assertEquals(moveArea.getAction(new Point(2, 5)), Action.MOVE);
    assertEquals(moveArea.getAction(new Point(1, 5)), Action.NONE);

    assertEquals(moveArea.getAction(new Point(6, 5)), Action.ATTACK);
    assertEquals(moveArea.getAction(new Point(5, 5)), Action.MOVE);
    assertEquals(moveArea.getAction(new Point(7, 5)), Action.MOVE);
    assertEquals(moveArea.getAction(new Point(8, 5)), Action.NONE);

    assertEquals(moveArea.getAction(new Point(4, 1)), Action.NONE);
    assertEquals(moveArea.getAction(new Point(4, 9)), Action.NONE);

    assertEquals(moveArea.getAction(new Point(4, 2)), Action.MOVE);
    assertEquals(moveArea.getAction(new Point(4, 8)), Action.MOVE);
  }

}
