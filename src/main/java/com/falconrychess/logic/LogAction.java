package com.falconrychess.logic;

import java.io.Serializable;

public class LogAction implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = -5027404815487679424L;
  private Point from;
  private Point to;
  private Action action;
  private Figure figure = null;

  public LogAction(Point from, Point to, Action action) {
    this.from = new Point(from);
    this.to = new Point(to);
    this.action = action;
  }

  public Point getFrom() {
    return new Point(from);
  }

  public Point getTo() {
    return new Point(to);
  }

  public Action getAction() {
    return action;
  }

  public Figure getFigure() {
    return figure;
  }

  public void setFigure(Figure figure) {
    this.figure = figure;
  }
}
