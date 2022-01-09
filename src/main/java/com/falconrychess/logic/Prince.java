package com.falconrychess.logic;

public class Prince extends Figure implements Special, Replacer {

  /**
   *
   */
  private static final long serialVersionUID = 1400002745843778903L;

  public Prince(Point coordinates, Side side) {
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

    Point specialPoint = null;

    LogAction lastAction = field.getLog().getLastAction();
    if (lastAction != null) {
      Figure lastActionFigure = field.getFigure(lastAction.getTo());
      if (lastActionFigure.getClassName().equals("Pawn")
        || lastActionFigure.getClassName().equals("Prince")) {
        if (lastActionFigure.getMoveCount() == 1) {
          if (Math.abs(lastAction.getFrom().getY()
            - lastAction.getTo().getY()) == 2) {
            specialPoint = new Point(lastAction.getFrom().getX()
              + ((lastAction.getTo().getX() - lastAction
              .getFrom().getX()) / 2), lastAction
              .getFrom().getY()
              + ((lastAction.getTo().getY() - lastAction
              .getFrom().getY()) / 2));
          }
        }
      }
    }


    point.addX(-1);
    if (point.isInField()) {
      if (point.equals(specialPoint)) {
        moveArea.setAction(point, Action.SPECIAL);
      } else if (field.isEmptyCell(point)) {
        moveArea.setAction(point, Action.MOVE);
      } else if (!field.getFigure(point).getSide().equals(side)) {
        moveArea.setAction(point, Action.ATTACK);
      }
    }

    point.addX(+2);
    if (point.isInField()) {
      if (point.equals(specialPoint)) {
        moveArea.setAction(point, Action.SPECIAL);
      } else if (field.isEmptyCell(point)) {
        moveArea.setAction(point, Action.MOVE);
      } else if (!field.getFigure(point).getSide().equals(side)) {
        moveArea.setAction(point, Action.ATTACK);
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
      point.addX(-2);
      additionalPoint.addX(-1);
      if (point.isInField() && field.isEmptyCell(point) && moveArea.getAction(additionalPoint).equals(Action.MOVE)) {
        moveArea.setAction(point, Action.MOVE);
      }
      point.addX(+4);
      additionalPoint.addX(+2);
      if (point.isInField() && field.isEmptyCell(point) && moveArea.getAction(additionalPoint).equals(Action.MOVE)) {
        moveArea.setAction(point, Action.MOVE);
      }
    }
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
    if (newFigure instanceof Dolphin || newFigure instanceof Falcon)
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

  @Override
  public boolean specialAction(Field field, Point to) {
    LogAction lastAction = field.getLog().getLastAction();
    Point specialPoint = new Point(lastAction.getTo());
    field.removeFigure(specialPoint);
    return true;
  }
}
