package com.falconrychess.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.falconrychess.Config;
import com.mongodb.*;
import org.bson.types.ObjectId;

import com.falconrychess.logic.Game;
import com.falconrychess.logic.Log;

public class GameController {
  private static class SaveAndRemoveNotActiveGamesJob extends Thread {

    public SaveAndRemoveNotActiveGamesJob() {
      setDaemon(true);
      setPriority(Thread.MIN_PRIORITY);
      start();
    }

    @Override
    public void run() {
      try {
        while (true) {
          Thread.sleep(31000);
          GameController.getInstance().saveAndRemoveNotActiveGames();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

  private static GameController instance = null;
  private DB db = null;
  DBCollection gameLogs;
  private Map<String, WrapperGame> gamesList = new HashMap<String, WrapperGame>();

  private GameController() {
    initDB();
  }

  public static synchronized GameController getInstance() {
    if (instance == null) {
      instance = new GameController();
      new SaveAndRemoveNotActiveGamesJob();
    }
    return instance;
  }

  public boolean isConnected() {
    if (db == null)
      return false;
    return true;
  }

  public String newGame() {
    WrapperGame wrapperGame = new WrapperGame();
    wrapperGame.setGame(new Game());
    String id = saveGame(wrapperGame.getGame(), "");
    if (id == null)
      return null;
    wrapperGame.setId(id);
    gamesList.put(id, wrapperGame);
    wrapperGame.setCurrentTime();
    return id;
  }

  public int gamesInMemory() {
    return gamesList.size();
  }

  public int gamesInDataBase() {
    return gameLogs.find().count();
  }

  public Game getGame(String id) {
    WrapperGame wrapperGame = gamesList.get(id);
    if (wrapperGame == null) {
      Game game = loadGame(id);
      if (game == null)
        return null;
      wrapperGame = new WrapperGame();
      wrapperGame.setGame(game);
      wrapperGame.setId(id);
      gamesList.put(id, wrapperGame);
      wrapperGame.setCurrentTime();
      return wrapperGame.getGame();
    } else {
      wrapperGame.setCurrentTime();
      return wrapperGame.getGame();
    }
  }

  public void saveAndRemoveNotActiveGames() {
    ArrayList<WrapperGame> list = new ArrayList<WrapperGame>(
      gamesList.values());
    for (WrapperGame wrapperGame : list) {
      if (wrapperGame.isTimeOver()) {
        saveGame(wrapperGame.getGame(), wrapperGame.getId());
        gamesList.remove(wrapperGame.getId());
      }
    }
  }

  private String saveGame(Game game, String id) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      ObjectOutputStream oos = new ObjectOutputStream(os);
      oos.writeObject(game.getLog());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    BasicDBObject document = new BasicDBObject();
    try {
      document.put("log", Base64.getEncoder().encodeToString(os.toByteArray()));
      document.put("gameLog", game.getGameLog()); // new
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (id.equals("")) {
      gameLogs.insert(document);
      ObjectId objectId = (ObjectId) document.get("_id");
      return objectId.toString();
    } else {
      BasicDBObject searchQuery = new BasicDBObject();
      ObjectId objectId = new ObjectId(id);
      searchQuery.put("_id", objectId);
      gameLogs.update(searchQuery, document);
      return id;
    }
  }

  private Game loadGame(String id) {
    BasicDBObject searchQuery = new BasicDBObject();
    ObjectId objectId = new ObjectId(id);
    searchQuery.put("_id", objectId);
    DBCursor cursor = gameLogs.find(searchQuery);
    if (!cursor.hasNext())
      return null;
    DBObject entity = cursor.next();
    String log = (String) entity.get("log");

    try {
      ByteArrayInputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(log));
      ObjectInputStream ois = new ObjectInputStream(is);
      Game game = new Game((Log) ois.readObject());
      game.restoreGameLog((String) entity.get("gameLog")); // new
      return game;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private void initDB() {
    try {
      MongoClient m = new MongoClient();
      db = m.getDB(Config.DB_NAME);
      gameLogs = db.getCollection(Config.DB_COLLECTION_GAMES);
      DBCursor cursor = gameLogs.find();
      cursor.count();
    } catch (Exception e) {
      db = null;
      e.printStackTrace();
    }
  }
}
