package com.falconrychess.logic;

public class Falcon extends Figure {

  /**
   *
   */
  private static final long serialVersionUID = -6189875021112687220L;

  public Falcon(Point coordinates, Side side) {
    super(coordinates, side);
  }

  @Override
  public void generateMoveArea(Field field) {
    moveArea.clear();

    Point[] moves = new Point[8];

    for (int i = 0; i < 8; i++) {
      moves[i] = new Point(coordinates);
    }

    moves[0].addXY(-3, -1);
    moves[1].addXY(-3, +1);

    moves[2].addXY(+3, -1);
    moves[3].addXY(+3, +1);

    moves[4].addXY(-1, -3);
    moves[5].addXY(+1, -3);

    moves[6].addXY(-1, +3);
    moves[7].addXY(+1, +3);

    for (int i = 0; i < 8; i++) {
      if (moves[i].getX() >= 0 && moves[i].getX() < Field.width
        && moves[i].getY() >= 0 && moves[i].getY() < Field.height) {
        if (field.isEmptyCell(moves[i])) {
          moveArea.setAction(moves[i], Action.MOVE);
        } else if (!field.getFigure(moves[i]).getSide().equals(side)) {
          moveArea.setAction(moves[i], Action.ATTACK);
        }
      }
    }

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
      while (point.isInField()
        && point.getX() <= coordinates.getX() + 3
        && point.getX() >= coordinates.getX() - 3
        && point.getY() <= coordinates.getY() + 3
        && point.getY() >= coordinates.getY() - 3) {
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
