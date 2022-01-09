package com.falconrychess.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Field {
  public static final int width = 10;
  public static final int height = 10;
  private ArrayList<Figure> figures = new ArrayList<Figure>();
  private Figure[][] field = new Figure[width][height];
  private Iterator<Figure> iterator;
  private Log log = new Log();
  private HashMap<Side, King> kings = new HashMap<Side, King>();
  private Figure lastRemovedFigure;

  public Field() {

  }

  public void addFigure(Figure figure) {
    if (figure.getClassName().equals("King")) {
      kings.put(figure.side, (King) figure);
    }
    figures.add(figure);
    field[figure.getCoordinates().getX()][figure.getCoordinates().getY()] = figure;
  }

  public boolean isEmptyCell(Point coordinates) {
    if (getFigure(coordinates) == null) {
      return true;
    }
    return false;
  }

  public Figure getFigure(Point coordinates) {
    return field[coordinates.getX()][coordinates.getY()];
  }

  private Figure getLastRemovedFigure() {
    return lastRemovedFigure;
  }

  public void rollBackLastAction() {
    int actionIndex = log.getLastIndex();
    if (actionIndex >= 1) {

      // If is castling
      if (log.getLastAction().getAction().equals(Action.SPECIAL)
        && getFigure(log.getLastAction().getTo()).getClassName()
        .equals("King")) {

        LogAction kingLogAction = log.getLastAction();
        moveFigure(kingLogAction.getTo(), kingLogAction.getFrom());
        getFigure(kingLogAction.getFrom()).setMoveCount(0);
        log.removeLastAction();
        log.removeLastAction();

        LogAction rookLogAction = log.getLastAction();

        // Check it is was really castling (it may was move check).
        if (getFigure(rookLogAction.getTo()).getSide().equals(getFigure(kingLogAction.getFrom()).getSide())) {
          moveFigure(rookLogAction.getTo(), rookLogAction.getFrom());
          getFigure(rookLogAction.getFrom()).setMoveCount(0);
          log.removeLastAction();
          log.removeLastAction();
        }

        return;
      }
    }

    LogAction logAction = log.getLastAction();

    if (logAction.getFigure() != null) {
      Figure replaced = logAction.getFigure();
      Figure replacer;
      Point coordinates = logAction.getTo();
      if (replaced.getClassName().equals("Dolphin")
        || replaced.getClassName().equals("Falcon")) {
        replacer = new Prince(coordinates, replaced.getSide());
      } else {
        replacer = new Pawn(coordinates, replaced.getSide());
      }

      replacer.setMoveCount(replaced.getMoveCount());

      int index = figures.indexOf(getFigure(coordinates));
      field[coordinates.getX()][coordinates.getY()] = null;
      if (index != -1) {
        figures.remove(index);
      }

      field[coordinates.getX()][coordinates.getY()] = replacer;
      figures.add(replacer);
    }

    moveFigure(logAction.getTo(), logAction.getFrom());
    getFigure(logAction.getFrom()).setMoveCount(
      getFigure(logAction.getFrom()).getMoveCount() - 2);
    log.removeLastAction();
    log.removeLastAction();
    if (!logAction.getAction().equals(Action.MOVE)
      && getLastRemovedFigure() != null) {
      addFigure(getLastRemovedFigure());
    }
  }

  public void removeFigure(Point coordinates) {
    int index = figures.indexOf(getFigure(coordinates));
    lastRemovedFigure = field[coordinates.getX()][coordinates.getY()];
    field[coordinates.getX()][coordinates.getY()] = null;
    if (index != -1) {
      figures.remove(index);
    }
  }

  public void resetIterator() {
    iterator = figures.iterator();
  }

  public boolean hasNext() {
    return iterator.hasNext();
  }

  public Figure getNext() {
    return iterator.next();
  }

  public Log getLog() {
    return log;
  }

  public boolean kingIsUnderAttack(Side side) {
    King king = kings.get(side);
    Point point = king.getCoordinates();
    side = side.getAnother();
    Figure enemyFigure;
    resetIterator();
    while (hasNext()) {
      enemyFigure = getNext();
      if (enemyFigure.getSide().equals(side)) {
        enemyFigure.generateMoveArea(this);
        if (enemyFigure.getMoveArea().getAction(point)
          .equals(Action.ATTACK)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean hasValidMove(Side side) {
    resetIterator();
    Figure friendFigure;
    int figuresCount = figures.size();
    for (int i = 0; i < figuresCount; i++) {
      friendFigure = figures.get(i);
      if (friendFigure.getSide().equals(side)) {
        friendFigure.generateMoveArea(this);
        MoveArea friendFigureMoveArea = friendFigure.getMoveArea();
        Point point;
        friendFigureMoveArea.resetIterator();
        while (friendFigureMoveArea.hasNext()) {
          point = friendFigureMoveArea.getNext();
          if (friendFigureMoveArea.getAction(point).equals(
            Action.SPECIAL)) {
            if (!specialMove(friendFigure.getCoordinates(), point))
              continue;
          }
          moveFigure(friendFigure.getCoordinates(), point);
          if (!kingIsUnderAttack(side)) {
            rollBackLastAction();
            return true;
          }
          rollBackLastAction();
        }
      }
    }
    return false;
  }

  public void moveFigure(Point from, Point to) {
    if (!isEmptyCell(to)) {
      removeFigure(to);
    }
    Figure figure = getFigure(from);
    field[from.getX()][from.getY()] = null;
    field[to.getX()][to.getY()] = figure;
    figure.setCoordinates(to);
    figure.increaseMoveCount();
    log.addAction(from, to, figure.getMoveArea().getAction(to));
  }

  public boolean specialMove(Point from, Point to) {
    Figure figure = this.getFigure(from);
    if (figure instanceof Special
      && figure.getMoveArea().getAction(to).equals(Action.SPECIAL)) {
      Special special = (Special) figure;
      if (!special.specialAction(this, to))
        return false;
    }
    return true;
  }
}
