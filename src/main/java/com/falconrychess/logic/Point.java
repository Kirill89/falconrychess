package com.falconrychess.logic;

import java.io.Serializable;

public class Point implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = -1510415793036005922L;
  private int x, y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point(Point original) {
    this.x = original.getX();
    this.y = original.getY();
  }

  public Point() {
    x = 0;
    y = 0;
  }

  public void addX(int add) {
    x += add;
  }

  public void addY(int add) {
    y += add;
  }

  public void setXY(int x, int y) {
    setX(x);
    setY(y);
  }

  public void addXY(int addX, int addY) {
    addX(addX);
    addY(addY);
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public boolean isInField() {
    if (x >= 0 && y >= 0 && x < Field.width && y < Field.height)
      return true;
    return false;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null)
      return false;
    if (!this.getClass().equals(other.getClass()))
      return false;
    Point point = (Point) other;
    if (this.getX() == point.getX() && this.getY() == point.getY())
      return true;
    return false;
  }
}