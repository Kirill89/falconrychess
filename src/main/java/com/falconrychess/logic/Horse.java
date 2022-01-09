package com.falconrychess.logic;

public class Horse extends Figure {

  /**
   *
   */
  private static final long serialVersionUID = -1301612507861602724L;

  public Horse(Point coordinates, Side side) {
    super(coordinates, side);
  }

  @Override
  public void generateMoveArea(Field field) {
    moveArea.clear();

    Point[] moves = new Point[8];

    for (int i = 0; i < 8; i++) {
      moves[i] = new Point(coordinates);
    }

    moves[0].addXY(-2, -1);
    moves[1].addXY(-2, +1);

    moves[2].addXY(+2, -1);
    moves[3].addXY(+2, +1);

    moves[4].addXY(-1, -2);
    moves[5].addXY(+1, -2);

    moves[6].addXY(-1, +2);
    moves[7].addXY(+1, +2);

    for (int i = 0; i < 8; i++) {
      if (moves[i].isInField()) {
        if (field.isEmptyCell(moves[i])) {
          moveArea.setAction(moves[i], Action.MOVE);
        } else if (!field.getFigure(moves[i]).getSide().equals(side)) {
          moveArea.setAction(moves[i], Action.ATTACK);
        }
      }
    }
  }
}
