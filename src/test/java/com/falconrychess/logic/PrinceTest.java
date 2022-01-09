package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrinceTest {

  @Test
  public void princeGenerateMoveArea() {
    Field field = new Field();
    Point firstPricePoint = new Point(5, 5);
    Point secondPricePoint = new Point(0, 2);
    Prince firstPrice = new Prince(firstPricePoint, Side.WHITE);
    Prince secondPrice = new Prince(secondPricePoint, Side.BLACK);

    field.addFigure(firstPrice);
    field.addFigure(secondPrice);

    firstPrice.generateMoveArea(field);
    secondPrice.generateMoveArea(field);

    assertEquals(firstPrice.getMoveArea().getAction(new Point(5, 4)),
      Action.MOVE);
    assertEquals(firstPrice.getMoveArea().getAction(new Point(5, 3)),
      Action.MOVE);
    assertEquals(firstPrice.getMoveArea().getAction(new Point(3, 3)),
      Action.MOVE);
    assertEquals(firstPrice.getMoveArea().getAction(new Point(4, 4)),
      Action.MOVE);
    assertEquals(firstPrice.getMoveArea().getAction(new Point(6, 4)),
      Action.MOVE);
    assertEquals(firstPrice.getMoveArea().getAction(new Point(7, 3)),
      Action.MOVE);

    secondPricePoint = new Point(4, 4);
    secondPrice = new Prince(secondPricePoint, Side.BLACK);
    field.addFigure(secondPrice);

    firstPrice.generateMoveArea(field);
    assertEquals(firstPrice.getMoveArea().getAction(new Point(4, 4)),
      Action.ATTACK);
    assertEquals(firstPrice.getMoveArea().getAction(new Point(3, 3)),
      Action.NONE);

    secondPricePoint = new Point(6, 4);
    secondPrice = new Prince(secondPricePoint, Side.BLACK);
    field.addFigure(secondPrice);

    firstPrice.generateMoveArea(field);
    assertEquals(firstPrice.getMoveArea().getAction(new Point(6, 4)),
      Action.ATTACK);
    assertEquals(firstPrice.getMoveArea().getAction(new Point(7, 3)),
      Action.NONE);
  }
}
