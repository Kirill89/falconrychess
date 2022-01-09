package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class HorseTest {

  @Test
  public void horseGenerateMoveArea() {
    Field field = new Field();
    field.addFigure(new Horse(new Point(5, 5), Side.WHITE));
    field.addFigure(new Horse(new Point(3, 4), Side.WHITE));
    field.addFigure(new Horse(new Point(7, 4), Side.BLACK));
    field.getFigure(new Point(5, 5)).generateMoveArea(field);
    MoveArea moveArea = field.getFigure(new Point(5, 5)).getMoveArea();
    assertEquals(moveArea.getAction(new Point(3, 4)), Action.NONE);
    assertEquals(moveArea.getAction(new Point(7, 4)), Action.ATTACK);
    assertEquals(moveArea.getAction(new Point(4, 3)), Action.MOVE);
  }

}
