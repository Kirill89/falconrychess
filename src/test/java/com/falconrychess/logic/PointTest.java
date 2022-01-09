package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

public class PointTest {
  private Point point = new Point(10, 5);

  @Test
  public void pointTest() {
    point = new Point(10, 5);
    assertEquals(point.getX(), 10);
    assertEquals(point.getY(), 5);
  }

  @Test
  public void addXYTest() {
    point.setX(3);
    point.setY(7);
    point.addXY(1, -3);
    assertEquals(point.getX(), 4);
    assertEquals(point.getX(), point.getY());
  }

  @Test
  public void copyTest() {
    point.setX(9);
    point.setY(5);
    Point copy = new Point(point);
    point.setX(0);
    point.setY(0);
    assertEquals(copy.getX(), 9);
    assertEquals(copy.getY(), 5);
  }

  @Test
  public void isInFieldTest() {
    point.setX(0);
    point.setY(0);
    assertTrue(point.isInField());
    point.setY(-1);
    assertFalse(point.isInField());
    point.setY(0);
    point.setX(-1);
    assertFalse(point.isInField());
    point.setX(Field.width - 1);
    point.setY(Field.height - 1);
    assertTrue(point.isInField());
    point.setY(Field.height);
    assertFalse(point.isInField());
    point.setX(Field.width);
    point.setY(Field.height - 1);
    assertFalse(point.isInField());
  }

  @Test
  public void setXYTest() {
    point = new Point();
    assertEquals(point.getX(), 0);
    assertEquals(point.getY(), 0);
    point.setXY(5, 3);
    assertEquals(point.getX(), 5);
    assertEquals(point.getY(), 3);
  }

  @Test
  public void equalsTest() {
    Point a = new Point(1, 4);
    Point b = new Point(1, 4);
    assertTrue(a.equals(b));
    b.setX(3);
    assertFalse(a.equals(b));
  }

  @After
  public void removePoint() {
    point = null;
  }

}
