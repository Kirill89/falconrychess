package com.falconrychess.logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Log implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 4333296390176760958L;
  private ArrayList<LogAction> actions = new ArrayList<LogAction>();

  public Log() {

  }

  public void addAction(Point from, Point to, Action action) {
    actions.add(new LogAction(from, to, action));
  }

  public LogAction getLastAction() {
    if (actions.isEmpty())
      return null;
    return actions.get(getLastIndex());
  }

  public int getLastIndex() {
    return actions.size() - 1;
  }

  public LogAction getByIndex(int i) {
    return actions.get(i);
  }

  public void removeLastAction() {
    actions.remove(getLastIndex());
  }

  public int getSize() {
    return actions.size();
  }
}
