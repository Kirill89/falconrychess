package com.falconrychess.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class SideTest {

  @Test
  public void getAnotherTest() {
    Side side = Side.WHITE;
    assertEquals(side, Side.WHITE);
    side = side.getAnother();
    assertEquals(side, Side.BLACK);
    side = side.getAnother();
    assertEquals(side, Side.WHITE);
    side = Side.NONE;
    assertEquals(side, Side.NONE);
    side = side.getAnother();
    assertEquals(side, Side.NONE);
  }

}
