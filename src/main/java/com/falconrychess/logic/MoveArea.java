package com.falconrychess.logic;

import java.util.ArrayList;
import java.util.Iterator;

public class MoveArea {
  private Action[][] actionField = new Action[Field.width][Field.height];
  private ArrayList<Point> actionPoints;
  private Iterator<Point> iterator;

  public MoveArea() {
    clear();
  }

  public Action getAction(Point coordinates) {
    return actionField[coordinates.getX()][coordinates.getY()];
  }

  public void setAction(Point coordinates, Action action) {
    if (action.equals(Action.NONE)) {
      actionPoints.remove(coordinates);
    } else {
      if (actionPoints.indexOf(coordinates) == -1)
        actionPoints.add(new Point(coordinates));
    }
    actionField[coordinates.getX()][coordinates.getY()] = action;
  }

  public void clear() {
    actionPoints = new ArrayList<Point>();
    for (int i = 0; i < Field.width; i++) {
      for (int j = 0; j < Field.height; j++) {
        actionField[i][j] = Action.NONE;
      }
    }
  }

  public void resetIterator() {
    iterator = actionPoints.iterator();
  }

  public boolean hasNext() {
    return iterator.hasNext();
  }

  public Point getNext() {
    return iterator.next();
  }
}
