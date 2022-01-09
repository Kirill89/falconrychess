package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

public class MoveAreaTest {
  private MoveArea moveArea = new MoveArea();

  @Test
  public void moveAreaTest() {
    for (int i = 0; i < Field.width; i++) {
      for (int j = 0; j < Field.height; j++) {
        assertEquals(moveArea.getAction(new Point(i, j)), Action.NONE);
      }
    }
  }

  @Test
  public void setAndGetActionTest() {
    moveArea.setAction(new Point(3, 3), Action.SPECIAL);
    assertEquals(moveArea.getAction(new Point(3, 3)), Action.SPECIAL);
  }

  @Test
  public void iteratorTest() {
    moveArea.clear();

    moveArea.setAction(new Point(3, 4), Action.ATTACK);
    moveArea.setAction(new Point(7, 2), Action.MOVE);
    moveArea.setAction(new Point(5, 0), Action.ATTACK);
    moveArea.setAction(new Point(9, 2), Action.MOVE);

    moveArea.setAction(new Point(7, 2), Action.NONE);
    moveArea.setAction(new Point(5, 0), Action.MOVE);

    moveArea.resetIterator();
    assertEquals(moveArea.getNext(), new Point(3, 4));
    assertEquals(moveArea.getNext(), new Point(5, 0));
    assertEquals(moveArea.getNext(), new Point(9, 2));
    assertFalse(moveArea.hasNext());
  }

  @After
  public void removeMoveArea() {
    moveArea = null;
  }

}
