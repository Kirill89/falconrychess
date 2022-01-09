package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import com.falconrychess.logic.Replacer.Result;

public class ReplacerTest {

  @Test
  public void replaceTest() {
    Field field = new Field();
    field.addFigure(new Pawn(new Point(0, 8), Side.BLACK));
    field.moveFigure(new Point(0, 8), new Point(0, 9));
    Replacer replacer = (Replacer) field.getFigure(new Point(0, 9));
    assertEquals(replacer.replace(field, new Queen(null, null)), Result.ok);
    assertTrue(field.getFigure(new Point(0, 9)) instanceof Queen);
    field.rollBackLastAction();
    assertEquals(field.getFigure(new Point(0, 8)).getClassName(), "Pawn");

    field.addFigure(new Pawn(new Point(0, 1), Side.WHITE));
    field.moveFigure(new Point(0, 1), new Point(0, 0));
    replacer = (Replacer) field.getFigure(new Point(0, 0));
    assertEquals(replacer.replace(field, new Rook(null, null)), Result.ok);
    assertTrue(field.getFigure(new Point(0, 0)) instanceof Rook);
    assertEquals(field.getFigure(new Point(0, 0)).getSide(), Side.WHITE);

    assertEquals(field.getLog().getLastAction().getFigure().getClassName(), "Rook");

    field.addFigure(new Prince(new Point(6, 1), Side.WHITE));
    field.moveFigure(new Point(6, 1), new Point(7, 0));
    replacer = (Replacer) field.getFigure(new Point(7, 0));
    assertEquals(replacer.replace(field, new Dolphin(null, null)), Result.ok);
    field.rollBackLastAction();
    assertEquals(field.getFigure(new Point(6, 1)).getClassName(), "Prince");
  }

}
