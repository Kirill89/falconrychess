package com.falconrychess.logic;

public class Queen extends Figure {

  /**
   *
   */
  private static final long serialVersionUID = 3410191228957507070L;

  public Queen(Point coordinates, Side side) {
    super(coordinates, side);
  }

  @Override
  public void generateMoveArea(Field field) {
    moveArea.clear();
    Rook rook = new Rook(coordinates, side);
    Elephant elephant = new Elephant(coordinates, side);
    rook.generateMoveArea(field);
    elephant.generateMoveArea(field);
    MoveArea elephantMoveArea = elephant.getMoveArea();
    MoveArea rookMoveArea = rook.getMoveArea();
    Point point = new Point();

    for (int i = 0; i < Field.width; i++) {
      for (int j = 0; j < Field.height; j++) {
        point.setXY(i, j);
        if (elephantMoveArea.getAction(point).equals(Action.ATTACK)
          || rookMoveArea.getAction(point).equals(Action.ATTACK)) {
          moveArea.setAction(point, Action.ATTACK);
        } else if (elephantMoveArea.getAction(point)
          .equals(Action.MOVE)
          || rookMoveArea.getAction(point).equals(Action.MOVE)) {
          moveArea.setAction(point, Action.MOVE);
        }
      }
    }
  }
}
