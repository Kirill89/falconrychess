package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

public class FieldTest {
  private Field field = new Field();

  @Test
  public void addFigureTest() {
    Figure test = new Pawn(new Point(1, 1), Side.WHITE);
    field.addFigure(test);
    assertEquals(field.getFigure(new Point(1, 1)), test);
  }

  @Test
  public void isEmptyCellTest() {
    Figure figure = new Pawn(new Point(7, 7), Side.WHITE);
    field.addFigure(figure);
    assertTrue(field.isEmptyCell(new Point(6, 6)));
    assertFalse(field.isEmptyCell(new Point(7, 7)));
  }

  @Test
  public void removeFigureTest() {
    Figure test = new Pawn(new Point(9, 9), Side.WHITE);
    field.addFigure(test);
    field.removeFigure(new Point(9, 9));
    assertNull(field.getFigure(new Point(9, 9)));
  }

  @Test
  public void moveFigureTest() {
    field = new Field();
    Point p0 = new Point(0, 0);
    Point p1 = new Point(1, 2);
    Figure figureMove = new Horse(p0, Side.WHITE);
    Figure figureAttack = new Horse(p1, Side.BLACK);
    field.addFigure(figureMove);
    field.addFigure(figureAttack);
    figureMove.generateMoveArea(field);
    field.moveFigure(p0, p1);
    assertEquals(field.getFigure(p1), figureMove);
    assertEquals(field.getFigure(p1).getCoordinates().getX(), p1.getX());
    assertEquals(field.getFigure(p1).getCoordinates().getY(), p1.getY());
  }

  @Test
  public void kingIsUnderAttackTest() {
    field = new Field();
    field.addFigure(new King(new Point(5, 0), Side.BLACK));
    field.addFigure(new King(new Point(5, 9), Side.WHITE));
    field.addFigure(new Queen(new Point(5, 4), Side.WHITE));
    field.addFigure(new Queen(new Point(0, 0), Side.BLACK));
    assertTrue(field.kingIsUnderAttack(Side.BLACK));
    assertFalse(field.kingIsUnderAttack(Side.WHITE));
  }

  @Test
  public void rollBackLastActionTest() {
    field = new Field();
    Pawn pawn = new Pawn(new Point(5, 0), Side.BLACK);
    field.addFigure(pawn);
    assertEquals(pawn.getMoveCount(), 0);
    field.moveFigure(pawn.getCoordinates(), new Point(5, 2));
    assertEquals(pawn.getMoveCount(), 1);
    field.rollBackLastAction();
    assertEquals(field.getFigure(new Point(5, 0)).getMoveCount(), 0);

    Pawn whitePawn = new Pawn(new Point(6, 1), Side.WHITE);
    field.addFigure(whitePawn);
    field.moveFigure(pawn.getCoordinates(), new Point(6, 1));
    assertEquals(pawn.getMoveCount(), 1);
    field.rollBackLastAction();
    assertEquals(pawn.getMoveCount(), 0);
    assertEquals(field.getFigure(new Point(6, 1)).getSide(), Side.WHITE);

    field = new Field();
    field.addFigure(new King(new Point(5, 9), Side.WHITE));
    field.addFigure(new Rook(new Point(1, 9), Side.WHITE));
    field.getFigure(new Point(5, 9)).generateMoveArea(field);
    assertEquals(field.getFigure(new Point(5, 9)).getMoveArea().getAction(new Point(3, 9)), Action.SPECIAL);
    assertEquals(field.getFigure(new Point(1, 9)).getClassName(), "Rook");
    assertEquals(field.getFigure(new Point(5, 9)).getClassName(), "King");
    //field.getFigure(new Point(5, 9)).specialAction(field, new Point(3, 9));
    Figure figure = field.getFigure(new Point(5, 9));
    Special special = (Special) figure;
    special.specialAction(field, new Point(3, 9));
    field.moveFigure(new Point(5, 9), new Point(3, 9));
    assertEquals(field.getFigure(new Point(4, 9)).getClassName(), "Rook");
    assertEquals(field.getFigure(new Point(3, 9)).getClassName(), "King");


    field.rollBackLastAction();
    assertEquals(field.getFigure(new Point(5, 9)).getClassName(), "King");
    assertEquals(field.getFigure(new Point(1, 9)).getClassName(), "Rook");

    assertEquals(field.getFigure(new Point(5, 9)).getMoveCount(), 0);
    assertEquals(field.getFigure(new Point(1, 9)).getMoveCount(), 0);
  }

  @Test
  public void hasValidMoveTest() {
    field = new Field();

    field.addFigure(new King(new Point(0, 9), Side.WHITE));
    field.addFigure(new Pawn(new Point(0, 8), Side.BLACK));
    field.addFigure(new Pawn(new Point(1, 8), Side.BLACK));
    assertTrue(field.hasValidMove(Side.WHITE));

    field.addFigure(new Queen(new Point(0, 7), Side.BLACK));
    assertFalse(field.hasValidMove(Side.WHITE));
  }

  @After
  public void removeField() {
    field = null;
  }

}
