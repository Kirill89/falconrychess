package com.falconrychess.logic;

public interface Replacer {
  public enum Result {
    conditionFailed, figureIncorrect, ok
  }

  public Result replace(Field field, Figure newFigure);
}
