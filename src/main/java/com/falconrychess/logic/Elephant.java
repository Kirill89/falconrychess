package com.falconrychess.logic;

public class Elephant extends Figure {

  /**
   *
   */
  private static final long serialVersionUID = 210574885421840196L;

  public Elephant(Point coordinates, Side side) {
    super(coordinates, side);
  }

  @Override
  public void generateMoveArea(Field field) {
    moveArea.clear();
    Point point;
    int addX = 0;
    int addY = 0;

    for (int i = 0; i < 4; i++) {
      switch (i) {
        case 0:
          addX = -1;
          addY = -1;
          break;
        case 1:
          addX = 1;
          addY = 1;
          break;
        case 2:
          addX = -1;
          addY = 1;
          break;
        case 3:
          addX = 1;
          addY = -1;
          break;
      }
      point = new Point(coordinates);
      point.addXY(addX, addY);
      while (point.isInField()) {
        if (field.isEmptyCell(point)) {
          moveArea.setAction(point, Action.MOVE);
        } else {
          if (field.getFigure(point).getSide().equals(side)) {
            break;
          } else {
            moveArea.setAction(point, Action.ATTACK);
            break;
          }
        }
        point.addXY(addX, addY);
      }
    }
  }
}
