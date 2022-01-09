package com.falconrychess;

public class Config {
  public static final int PORT = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 8080;
  public static final String DB_NAME = "falconrychess";
  public static final String DB_COLLECTION_GAMES = "gameLogs";
}
