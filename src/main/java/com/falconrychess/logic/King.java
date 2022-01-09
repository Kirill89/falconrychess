package com.falconrychess.logic;

public class King extends Figure implements Special {

  /**
   *
   */
  private static final long serialVersionUID = 7026161391703868132L;

  public King(Point coordinates, Side side) {
    super(coordinates, side);
  }

  @Override
  public void generateMoveArea(Field field) {
    moveArea.clear();
    Point[] moves = new Point[8];

    for (int i = 0; i < 8; i++) {
      moves[i] = new Point(coordinates);
    }

    moves[0].addX(-1);
    moves[1].addX(+1);
    moves[2].addY(-1);
    moves[3].addY(+1);

    moves[4].addXY(-1, -1);
    moves[5].addXY(-1, +1);
    moves[6].addXY(+1, -1);
    moves[7].addXY(+1, +1);

    for (int i = 0; i < 8; i++) {
      if (moves[i].isInField()) {
        if (field.isEmptyCell(moves[i])) {
          moveArea.setAction(moves[i], Action.MOVE);
        } else if (!field.getFigure(moves[i]).getSide().equals(side)) {
          moveArea.setAction(moves[i], Action.ATTACK);
        }
      }
    }

    Point point = new Point(coordinates);
    point.addX(+3);
    if (moveCount == 0 && point.isInField()) {
      if (!field.isEmptyCell(point)
        && field.getFigure(point).getClassName().equals("Rook")
        && field.getFigure(point).getMoveCount() == 0) {
        point.addX(-1);
        if (field.isEmptyCell(point)) {
          point.addX(-1);
          if (field.isEmptyCell(point)) {
            point.addX(+1);
            moveArea.setAction(point, Action.SPECIAL);
          }
        }
      }
    }

    point = new Point(coordinates);
    point.addX(-4);
    if (moveCount == 0 && point.isInField()) {
      if (!field.isEmptyCell(point)
        && field.getFigure(point).getClassName().equals("Rook")
        && field.getFigure(point).getMoveCount() == 0) {
        point.addX(+1);
        if (field.isEmptyCell(point)) {
          point.addX(+1);
          if (field.isEmptyCell(point)) {
            point.addX(+1);
            if (field.isEmptyCell(point)) {
              point.addX(-1);
              moveArea.setAction(point, Action.SPECIAL);
            }
          }
        }
      }
    }
  }

  private boolean castlingLeft(Field field, Point to) {
    if (field.kingIsUnderAttack(side))
      return false;

    Point testTo = new Point(coordinates);
    testTo.addX(-1);
    field.moveFigure(coordinates, testTo);
    if (field.kingIsUnderAttack(side)) {
      field.rollBackLastAction();
      return false;
    }
    field.rollBackLastAction();

    testTo.addX(-1);
    field.moveFigure(coordinates, testTo);
    if (field.kingIsUnderAttack(side)) {
      field.rollBackLastAction();
      return false;
    }
    field.rollBackLastAction();

    // TODO: maybe missed one more move check

    Point rookFrom = new Point(to);
    Point rookTo = new Point(to);
    rookFrom.addX(-2);
    rookTo.addX(+1);
    field.moveFigure(rookFrom, rookTo);
    return true;
  }

  private boolean castlingRight(Field field, Point to) {
    if (field.kingIsUnderAttack(side))
      return false;

    Point testTo = new Point(coordinates);
    testTo.addX(+1);
    field.moveFigure(coordinates, testTo);
    if (field.kingIsUnderAttack(side)) {
      field.rollBackLastAction();
      return false;
    }
    field.rollBackLastAction();

    testTo.addX(+1);
    field.moveFigure(coordinates, testTo);
    if (field.kingIsUnderAttack(side)) {
      field.rollBackLastAction();
      return false;
    }
    field.rollBackLastAction();

    Point rookFrom = new Point(to);
    Point rookTo = new Point(to);
    rookFrom.addX(+1);
    rookTo.addX(-1);
    field.moveFigure(rookFrom, rookTo);
    return true;
  }

  @Override
  public boolean specialAction(Field field, Point to) {
    if (coordinates.getX() < to.getX()) {
      return castlingRight(field, to);
    } else {
      return castlingLeft(field, to);
    }
  }

}
