package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

public class FigureTest {
  private Point point = new Point(1, 1);
  private Figure figure = new Horse(point, Side.BLACK);

  @Test
  public void getSideTest() {
    assertEquals(figure.getSide(), Side.BLACK);
  }

  @Test
  public void getCoordinatesTest() {
    assertEquals(figure.getCoordinates(), point);
  }

  @Test
  public void getClassNameTest() {
    assertEquals(figure.getClassName(), "Horse");
  }

  @Test
  public void canGoToTest() {
    Field field = new Field();
    field.addFigure(figure);
    field.addFigure(new Pawn(new Point(2, 3), Side.BLACK));
    field.addFigure(new Pawn(new Point(3, 2), Side.WHITE));
    figure.generateMoveArea(field);
    assertTrue(figure.canGoTo(new Point(3, 2)));
    assertFalse(figure.canGoTo(new Point(2, 3)));
    assertTrue(figure.canGoTo(new Point(0, 3)));
  }

  @Test
  public void moveCountTest() {
    figure.increaseMoveCount();
    assertEquals(figure.getMoveCount(), 1);
    figure.increaseMoveCount();
    assertEquals(figure.getMoveCount(), 2);
  }

  @After
  public void removeMembers() {
    point = null;
    figure = null;
  }

}
