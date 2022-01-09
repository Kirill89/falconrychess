package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class ElephantTest {

  @Test
  public void elephantTestGenerateMoveArea() {
    Field field = new Field();

    field.addFigure(new Elephant(new Point(2, 2), Side.WHITE));
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


  }

}
