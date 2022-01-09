package com.falconrychess.logic;

import com.falconrychess.controller.GameLog;

public class Game {
  private Field field = new Field();
  private Side side = Side.WHITE;
  private GameStatus status = GameStatus.NONE;
  private Figure replaceFigure = null;

  private GameLog gameLog = new GameLog();

  public enum MoveResult {
    gameIsOver, illegalSpecialMove, illegalMove, kingIsUnderAttackAfterMove, illegalFigureReplace, ok
  }

  public Game() {
    initialPosition();
  }

  public void restoreGameLog(String actions) {
    gameLog = new GameLog(actions);
  }

  public String getGameLog() {
    return gameLog.toString();
  }

  public Game(Log log) {
    initialPosition();
    int size = log.getSize();
    LogAction logAction;
    for (int i = 0; i < size; i++) {
      logAction = log.getByIndex(i);
      if (logAction.getFigure() != null) {
        logAction.getFigure().setMoveArea(new MoveArea());
        replaceFigure(logAction.getFigure());
      }
      moveFigure(logAction.getFrom(), logAction.getTo());
    }
  }

  public Log getLog() {
    int size = field.getLog().getSize();
    LogAction logAction;
    for (int i = 0; i < size; i++) {
      logAction = field.getLog().getByIndex(i);
      if (logAction.getFigure() != null) {
        logAction.getFigure().setMoveArea(null);
      }
    }
    return field.getLog();
  }

  public MoveArea getMoveArea(Point coordinates) {
    if (field.isEmptyCell(coordinates))
      return null;
    field.getFigure(coordinates).generateMoveArea(field);
    return field.getFigure(coordinates).getMoveArea();
  }

  public Side getSide() {
    return side;
  }

  public String[][] getField() {
    Point point;
    String[][] result = new String[Field.width][];
    for (int i = 0; i < Field.width; i++) {
      result[i] = new String[Field.height];
      for (int j = 0; j < Field.height; j++) {
        result[i][j] = new String();
        point = new Point(i, j);
        if (!field.isEmptyCell(point))
          result[i][j] = field.getFigure(point).getSide().toString()
            .substring(0, 1)
            + field.getFigure(point).getClassName()
            .substring(0, 2);
      }
    }
    return result;
  }

  public Field getFieldObj() {
    return field;
  }

  public int getFieldWidth() {
    return Field.width;
  }

  public int getFieldHeight() {
    return Field.height;
  }

  private void nextSide() {
    side = side.getAnother();
    checkStatus();
  }

  public void replaceFigure(Figure newFigure) {
    replaceFigure = newFigure;
  }

  public MoveResult moveFigure(Point from, Point to) {
    if (getStatus().equals(GameStatus.CHECKMATE)
      || getStatus().equals(GameStatus.JUJUBE))
      return MoveResult.gameIsOver;
    if (from.isInField() && to.isInField() && !field.isEmptyCell(from)
      && field.getFigure(from).getSide().equals(side)) {
      field.getFigure(from).generateMoveArea(field);
      if (field.getFigure(from).canGoTo(to)) {

        boolean isCastlingLong = false;
        boolean isCastlingShort = false;
        boolean isAttack = false;

        if (!field.specialMove(from, to)) {
          return MoveResult.illegalSpecialMove;
        } else {
          if (field.getFigure(from).getClassName().equals("King")
            && field.getFigure(from).getMoveArea()
            .getAction(to).equals(Action.SPECIAL)) {
            if (from.getX() > to.getX()) {
              isCastlingLong = true;
            } else {
              isCastlingShort = true;
            }
          }
        }

        if (field.getFigure(to) != null) {
          isAttack = true;
        }

        field.moveFigure(from, to);

        if (field.kingIsUnderAttack(side)) {
          field.rollBackLastAction();
          return MoveResult.kingIsUnderAttackAfterMove;
        }

        if (field.getFigure(to) instanceof Replacer) {
          Replacer replacer = (Replacer) field.getFigure(to);
          Replacer.Result result = replacer.replace(field,
            replaceFigure);
          if (result.equals(Replacer.Result.figureIncorrect)) {
            field.rollBackLastAction();
            return MoveResult.illegalFigureReplace;
          }
        }

        nextSide();

        if (isCastlingLong) {
          gameLog.castlingLong();
        } else if (isCastlingShort) {
          gameLog.castlingShort();
        } else {
          Action moveType;
          if (isAttack) {
            moveType = Action.ATTACK;
          } else {
            moveType = Action.MOVE;
          }
          gameLog.add(from, to, field.getFigure(to), moveType,
            getStatus());
        }
        replaceFigure(null);

        return MoveResult.ok;
      }
    }
    return MoveResult.illegalMove;
  }

  private void checkStatus() {
    boolean hasNoValidMove = !field.hasValidMove(side);
    if (field.kingIsUnderAttack(side)) {
      if (hasNoValidMove) {
        status = GameStatus.CHECKMATE;
      } else {
        status = GameStatus.CHECK;
      }
    } else if (hasNoValidMove) {
      status = GameStatus.JUJUBE;
    } else {
      status = GameStatus.NONE;
    }
  }

  public GameStatus getStatus() {
    return status;
  }

  private void initialPosition() {
    Horse[] horses = new Horse[4];
    horses[0] = new Horse(new Point(2, 0), Side.BLACK);
    horses[1] = new Horse(new Point(7, 0), Side.BLACK);
    horses[2] = new Horse(new Point(2, 9), Side.WHITE);
    horses[3] = new Horse(new Point(7, 9), Side.WHITE);
    for (int i = 0; i < 4; i++) {
      field.addFigure(horses[i]);
    }

    Elephant[] elephants = new Elephant[4];
    elephants[0] = new Elephant(new Point(3, 0), Side.BLACK);
    elephants[1] = new Elephant(new Point(6, 0), Side.BLACK);
    elephants[2] = new Elephant(new Point(3, 9), Side.WHITE);
    elephants[3] = new Elephant(new Point(6, 9), Side.WHITE);
    for (int i = 0; i < 4; i++) {
      field.addFigure(elephants[i]);
    }

    Rook[] rooks = new Rook[4];
    rooks[0] = new Rook(new Point(1, 0), Side.BLACK);
    rooks[1] = new Rook(new Point(8, 0), Side.BLACK);
    rooks[2] = new Rook(new Point(1, 9), Side.WHITE);
    rooks[3] = new Rook(new Point(8, 9), Side.WHITE);
    for (int i = 0; i < 4; i++) {
      field.addFigure(rooks[i]);
    }

    Dolphin[] dolphins = new Dolphin[4];
    dolphins[0] = new Dolphin(new Point(0, 0), Side.BLACK);
    dolphins[1] = new Dolphin(new Point(9, 0), Side.BLACK);
    dolphins[2] = new Dolphin(new Point(0, 9), Side.WHITE);
    dolphins[3] = new Dolphin(new Point(9, 9), Side.WHITE);
    for (int i = 0; i < 4; i++) {
      field.addFigure(dolphins[i]);
    }

    field.addFigure(new Queen(new Point(4, 0), Side.BLACK));
    field.addFigure(new Queen(new Point(4, 9), Side.WHITE));

    field.addFigure(new King(new Point(5, 0), Side.BLACK));
    field.addFigure(new King(new Point(5, 9), Side.WHITE));

    Prince[] princes = new Prince[4];
    princes[0] = new Prince(new Point(4, 1), Side.BLACK);
    princes[1] = new Prince(new Point(5, 1), Side.BLACK);
    princes[2] = new Prince(new Point(4, 8), Side.WHITE);
    princes[3] = new Prince(new Point(5, 8), Side.WHITE);
    for (int i = 0; i < 4; i++) {
      field.addFigure(princes[i]);
    }

    Falcon[] falcons = new Falcon[4];
    falcons[0] = new Falcon(new Point(3, 1), Side.BLACK);
    falcons[1] = new Falcon(new Point(6, 1), Side.BLACK);
    falcons[2] = new Falcon(new Point(3, 8), Side.WHITE);
    falcons[3] = new Falcon(new Point(6, 8), Side.WHITE);
    for (int i = 0; i < 4; i++) {
      field.addFigure(falcons[i]);
    }

    Pawn[] pawns = new Pawn[10];
    pawns[0] = new Pawn(new Point(0, 1), Side.BLACK);
    pawns[1] = new Pawn(new Point(1, 1), Side.BLACK);
    pawns[2] = new Pawn(new Point(2, 1), Side.BLACK);

    pawns[3] = new Pawn(new Point(3, 2), Side.BLACK);
    pawns[4] = new Pawn(new Point(4, 2), Side.BLACK);
    pawns[5] = new Pawn(new Point(5, 2), Side.BLACK);
    pawns[6] = new Pawn(new Point(6, 2), Side.BLACK);

    pawns[7] = new Pawn(new Point(7, 1), Side.BLACK);
    pawns[8] = new Pawn(new Point(8, 1), Side.BLACK);
    pawns[9] = new Pawn(new Point(9, 1), Side.BLACK);

    for (int i = 0; i < 10; i++) {
      field.addFigure(pawns[i]);
    }

    pawns = new Pawn[10];
    pawns[0] = new Pawn(new Point(0, 8), Side.WHITE);
    pawns[1] = new Pawn(new Point(1, 8), Side.WHITE);
    pawns[2] = new Pawn(new Point(2, 8), Side.WHITE);

    pawns[3] = new Pawn(new Point(3, 7), Side.WHITE);
    pawns[4] = new Pawn(new Point(4, 7), Side.WHITE);
    pawns[5] = new Pawn(new Point(5, 7), Side.WHITE);
    pawns[6] = new Pawn(new Point(6, 7), Side.WHITE);

    pawns[7] = new Pawn(new Point(7, 8), Side.WHITE);
    pawns[8] = new Pawn(new Point(8, 8), Side.WHITE);
    pawns[9] = new Pawn(new Point(9, 8), Side.WHITE);

    for (int i = 0; i < 10; i++) {
      field.addFigure(pawns[i]);
    }
  }
}
