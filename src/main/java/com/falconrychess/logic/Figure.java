package com.falconrychess.logic;

import java.io.Serializable;

public abstract class Figure implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 8659408790313479984L;
  protected Point coordinates;
  protected Side side;
  protected MoveArea moveArea = new MoveArea();
  protected int moveCount = 0;

  public Figure(Point coordinates, Side side) {
    this.coordinates = coordinates;
    this.side = side;
  }

  public String getClassName() {
    return this.getClass().getSimpleName();
  }

  public Side getSide() {
    return side;
  }

  public Point getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Point coordinates) {
    this.coordinates = coordinates;
  }

  public abstract void generateMoveArea(Field field);

	/*public MoveArea getTemporaryMoveArea(Field field) {
		MoveArea oldMoveArea = moveArea;
		moveArea = new MoveArea();
		generateMoveArea(field);
		MoveArea newMoveArea = moveArea;
		moveArea = oldMoveArea;
		return newMoveArea;
	}*/

  //public abstract void specialAction(Field field, Point to);

  public MoveArea getMoveArea() {
    return moveArea;
  }

  public void setMoveArea(MoveArea moveArea) {
    this.moveArea = moveArea;
  }

  public boolean canGoTo(Point coordinates) {
    if (!moveArea.getAction(coordinates).equals(Action.NONE)) {
      return true;
    }
    return false;
  }

  public void increaseMoveCount() {
    moveCount++;
  }

  public int getMoveCount() {
    return moveCount;
  }

  public void setMoveCount(int count) {
    moveCount = count;
  }
}
