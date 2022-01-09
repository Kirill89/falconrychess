package com.falconrychess.logic;

public class Dolphin extends Figure {

  /**
   *
   */
  private static final long serialVersionUID = 5220609057150242795L;

  public Dolphin(Point coordinates, Side side) {
    super(coordinates, side);
  }

  @Override
  public void generateMoveArea(Field field) {
    moveArea.clear();
    boolean canAttack;
    Point point = new Point();

    for (int i = 0; i < 4; i++) {
      canAttack = true;
      for (int move = 1; move <= 3; move++) {
        switch (i) {
          case 0:
            point.setXY(coordinates.getX() - move, coordinates.getY());
            break;
          case 1:
            point.setXY(coordinates.getX() + move, coordinates.getY());
            break;
          case 2:
            point.setXY(coordinates.getX(), coordinates.getY() - move);
            break;
          case 3:
            point.setXY(coordinates.getX(), coordinates.getY() + move);
            break;
        }

        if (!point.isInField())
          break;

        if (field.isEmptyCell(point)) {
          moveArea.setAction(point, Action.MOVE);
        } else {
          if (field.getFigure(point).getSide().equals(side)) {
            canAttack = false;
          } else if (canAttack) {
            moveArea.setAction(point, Action.ATTACK);
            canAttack = false;
          }
        }
      }
    }
  }
}
