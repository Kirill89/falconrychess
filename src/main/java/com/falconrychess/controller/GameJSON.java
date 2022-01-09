package com.falconrychess.controller;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.falconrychess.logic.Action;
import com.falconrychess.logic.Dolphin;
import com.falconrychess.logic.Elephant;
import com.falconrychess.logic.Falcon;
import com.falconrychess.logic.Field;
import com.falconrychess.logic.Game;
import com.falconrychess.logic.Game.MoveResult;
import com.falconrychess.logic.Horse;
import com.falconrychess.logic.MoveArea;
import com.falconrychess.logic.Point;
import com.falconrychess.logic.Queen;
import com.falconrychess.logic.Rook;
import com.falconrychess.logic.Side;

public class GameJSON {
  private Gson gson = new Gson();
  private GameController gameController = GameController.getInstance();
  private static GameJSON instance = null;

  public static synchronized GameJSON getInstance() {
    if (instance == null) {
      instance = new GameJSON();
    }
    return instance;
  }

  private Result getMoveArea(Command command, Game game) {
    Result result = new Result();
    MoveArea moveArea = game.getMoveArea(new Point(command.x0, command.y0));
    if (moveArea == null) {
      result.status = "error";
      result.message = "emptyCell";
      return result;
    }
    ArrayList<Point> field = new ArrayList<Point>();
    moveArea.resetIterator();
    Point point;
    Action action;
    while (moveArea.hasNext()) {
      point = moveArea.getNext();
      action = moveArea.getAction(point);
      if (!action.equals(Action.NONE)) {
        field.add(point);
      }
    }
    result.status = "ok";
    result.message = gson.toJson(field);
    return result;
  }

  private Result getStatus(Command command, Game game) {
    Result result = new Result();
    result.status = "ok";
    result.message = "{\"side\":\"" + game.getSide().toString()
      + "\",\"status\":\"" + game.getStatus().toString() + "\"}";
    return result;
  }

  private Result replace(Command command, Game game) {
    Result result = new Result();
    if (command.figure.equals("Queen")) {
      game.replaceFigure(new Queen(null, null));
    } else if (command.figure.equals("Horse")) {
      game.replaceFigure(new Horse(null, null));
    } else if (command.figure.equals("Rook")) {
      game.replaceFigure(new Rook(null, null));
    } else if (command.figure.equals("Elephant")) {
      game.replaceFigure(new Elephant(null, null));
    } else if (command.figure.equals("Falcon")) {
      game.replaceFigure(new Falcon(null, null));
    } else if (command.figure.equals("Dolphin")) {
      game.replaceFigure(new Dolphin(null, null));
    } else {
      result.status = "error";
      result.message = "wrongFigure";
      return result;
    }
    result.status = "ok";
    result.message = "";
    return result;
  }

  private Result move(Command command, Game game) {
    Result result = new Result();
    MoveResult moveResault = game.moveFigure(new Point(command.x0,
      command.y0), new Point(command.x1, command.y1));
    if (moveResault.equals(MoveResult.ok)) {
      result.status = "ok";
      result.message = "";
      return result;
    } else {
      result.status = "error";
      result.message = moveResault.toString();
      return result;
    }
  }

  private Result getField(Command command, Game game) {
    Result result = new Result();
    FigureInfo figureInfo;
    ArrayList<FigureInfo> figures = new ArrayList<FigureInfo>();
    Field field = game.getFieldObj();
    Point point;
    for (int i = 0; i < Field.width; i++) {
      for (int j = 0; j < Field.height; j++) {
        point = new Point(i, j);
        if (!field.isEmptyCell(point)) {
          figureInfo = new FigureInfo();
          figureInfo.name = field.getFigure(point).getClassName();
          figureInfo.side = field.getFigure(point).getSide()
            .toString();
          figureInfo.x = point.getX();
          figureInfo.y = point.getY();
          figures.add(figureInfo);
        }
      }
    }
    result.status = "ok";
    result.message = gson.toJson(figures);
    return result;
  }

  private Result getGameLog(Command command, Game game) {
    Result result = new Result();
    result.status = "ok";
    result.message = game.getGameLog();
    return result;
  }

  public Result executeCommand(Command command) {
    Result result = new Result();

    if (command.action.equals("newGame")) {
      String id = gameController.newGame();
      result.status = "ok";
      result.message = id;
      return result;
    }

    Game game = gameController.getGame(command.gameId);
    if (game == null) {
      result.status = "error";
      result.message = "invalidGameId";
      return result;
    }

    if (command.action.equals("getMoveArea")) {
      return getMoveArea(command, game);
    } else if (command.action.equals("getStatus")) {
      return getStatus(command, game);
    } else if (command.action.equals("replace")) {
      return replace(command, game);
    } else if (command.action.equals("move")) {
      return move(command, game);
    } else if (command.action.equals("getField")) {
      return getField(command, game);
    } else if (command.action.equals("getGameLog")) {
      return getGameLog(command, game);
    }

    result.status = "error";
    result.message = "wrongCommand";
    return result;
  }
//
//  private GameJSON() {
//
//  }
}
