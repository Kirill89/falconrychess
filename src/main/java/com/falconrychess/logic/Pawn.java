package com.falconrychess.logic;

public class Pawn extends Figure implements Special, Replacer {

  /**
   *
   */
  private static final long serialVersionUID = -1554267839956994064L;

  public Pawn(Point coordinates, Side side) {
    super(coordinates, side);
  }

  @Override
  public void generateMoveArea(Field field) {
    moveArea.clear();
    Point point;
    int addY;
    if (side.equals(Side.WHITE)) {
      addY = -1;
    } else {
      addY = +1;
    }

    point = new Point(coordinates);
    point.addY(addY);
    if (point.isInField() && field.isEmptyCell(point)) {
      moveArea.setAction(point, Action.MOVE);
    }

    int addX = 0;
    for (int i = 0; i < 2; i++) {
      switch (i) {
        case 0:
          addX = -1;
          break;
        case 1:
          addX = +1;
          break;
      }

      point = new Point(coordinates);
      point.addXY(addX, addY);
      if (!point.isInField())
        continue;
      if (!field.isEmptyCell(point) && !field.getFigure(point).getSide().equals(side)) {
        moveArea.setAction(point, Action.ATTACK);
      } else {
        LogAction lastAction = field.getLog().getLastAction();
        if (lastAction == null)
          continue;
        Figure lastActionFigure = field.getFigure(lastAction.getTo());
        if (!(lastActionFigure.getClassName().equals("Pawn") || lastActionFigure
          .getClassName().equals("Prince")))
          continue;
        if (lastActionFigure.getMoveCount() != 1)
          continue;
        if (Math.abs(lastAction.getFrom().getY()
          - lastAction.getTo().getY()) != 2)
          continue;
        Point specialPoint = new Point(lastAction.getFrom().getX()
          + ((lastAction.getTo().getX() - lastAction.getFrom()
          .getX()) / 2), lastAction.getFrom().getY()
          + ((lastAction.getTo().getY() - lastAction.getFrom()
          .getY()) / 2));
        if (specialPoint.equals(point))
          moveArea.setAction(point, Action.SPECIAL);
      }

    }

    if (moveCount == 0) {
      point = new Point(coordinates);
      point.addY(addY * 2);
      Point additionalPoint = new Point(coordinates);
      additionalPoint.addY(addY);
      if (point.isInField() && field.isEmptyCell(point) && moveArea.getAction(additionalPoint).equals(Action.MOVE)) {
        moveArea.setAction(point, Action.MOVE);
      }
    }

  }

  @Override
  public boolean specialAction(Field field, Point to) {
    LogAction lastAction = field.getLog().getLastAction();
    Point specialPoint = new Point(lastAction.getTo());
    field.removeFigure(specialPoint);
    return true;
  }

  private void replaceFigure(Field field, Figure newFigure) {
    newFigure.setCoordinates(coordinates);
    newFigure.side = side;
    newFigure.moveCount = moveCount;
    field.removeFigure(coordinates);
    field.addFigure(newFigure);
    LogAction logAction = field.getLog().getLastAction();
    logAction.setFigure(newFigure);
  }

  private boolean isReplaceFigureCorrect(Figure newFigure) {
    if (newFigure instanceof Elephant || newFigure instanceof Horse
      || newFigure instanceof Queen || newFigure instanceof Rook)
      return true;
    return false;
  }

  @Override
  public Result replace(Field field, Figure newFigure) {
    if (side.equals(Side.WHITE) && coordinates.getY() == 0) {
      if (!isReplaceFigureCorrect(newFigure))
        return Result.figureIncorrect;
      replaceFigure(field, newFigure);
      return Result.ok;
    } else if (side.equals(Side.BLACK) && coordinates.getY() == Field.height - 1) {
      if (!isReplaceFigureCorrect(newFigure))
        return Result.figureIncorrect;
      replaceFigure(field, newFigure);
      return Result.ok;
    }
    return Result.conditionFailed;
  }
}
