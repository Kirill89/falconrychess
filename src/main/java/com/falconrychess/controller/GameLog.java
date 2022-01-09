package com.falconrychess.controller;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.falconrychess.logic.Action;
import com.falconrychess.logic.Figure;
import com.falconrychess.logic.GameStatus;
import com.falconrychess.logic.Point;

//http://ru.wikipedia.org/wiki/%D0%A8%D0%B0%D1%85%D0%BC%D0%B0%D1%82%D0%BD%D0%B0%D1%8F_%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F
public class GameLog {
  private List<String> actions = new ArrayList<String>();

  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(actions);
  }

  public void castlingShort() {
    actions.add("0-0");
  }

  public void castlingLong() {
    actions.add("0-0-0");
  }

  public void add(Point from, Point to, Figure figure, Action moveType, GameStatus gameStatus) {
    String message = "";
    message += translateFigure(figure);
    message += translateCoordinates(from);
    message += translateMoveType(moveType);
    message += translateCoordinates(to);
    message += translateStatus(gameStatus);
    actions.add(message);
  }

  public GameLog() {

  }

  public GameLog(String actions) {
    Gson gson = new Gson();
    Type StringType = new TypeToken<ArrayList<String>>() {
    }.getType();
    this.actions = gson.fromJson(actions, StringType);
  }

  private String translateX(int x) {
    switch (x) {
      case 0:
        return "Δ";
      case 1:
        return "A";
      case 2:
        return "B";
      case 3:
        return "C";
      case 4:
        return "D";
      case 5:
        return "E";
      case 6:
        return "F";
      case 7:
        return "G";
      case 8:
        return "H";
      case 9:
        return "I";
    }
    return "";
  }

  private String translateY(int y) {
    switch (y) {
      case 0:
        return "10";
      case 1:
        return "9";
      case 2:
        return "8";
      case 3:
        return "7";
      case 4:
        return "6";
      case 5:
        return "5";
      case 6:
        return "4";
      case 7:
        return "3";
      case 8:
        return "2";
      case 9:
        return "1";
    }
    return "";
  }

  private String translateFigure(Figure figure) {
    if (figure.getClassName().equals("Pawn")) {
      return "P";
    }
    if (figure.getClassName().equals("King")) {
      return "K";
    }
    if (figure.getClassName().equals("Queen")) {
      return "Q";
    }
    if (figure.getClassName().equals("Rook")) {
      return "R";
    }
    if (figure.getClassName().equals("Horse")) {
      return "N";
    }
    if (figure.getClassName().equals("Elephant")) {
      return "B";
    }
    if (figure.getClassName().equals("Prince")) {
      return "Pr";
    }
    if (figure.getClassName().equals("Dolphin")) {
      return "D";
    }
    if (figure.getClassName().equals("Falcon")) {
      return "F";
    }
    return "";
  }

  private String translateCoordinates(Point point) {
    String x = translateX(point.getX());
    String y = translateY(point.getY());
    return x + y;
  }

  private String translateStatus(GameStatus gameStatus) {
    if (gameStatus.equals(GameStatus.CHECK)) {
      return "+";
    }
    if (gameStatus.equals(GameStatus.CHECKMATE)) {
      return "#";
    }
    if (gameStatus.equals(GameStatus.JUJUBE)
      || gameStatus.equals(GameStatus.DRAW)) {
      return "=";
    }
    return "";
  }

  private String translateMoveType(Action action) {
    if (action.equals(Action.MOVE)) {
      return "-";
    }
    if (action.equals(Action.ATTACK)) {
      return "x";
    }
    return "";
  }
}
