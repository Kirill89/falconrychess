package com.falconrychess;

import com.falconrychess.controller.Command;
import com.falconrychess.controller.GameJSON;
import com.falconrychess.controller.Result;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import java.util.*;

public class RndHttpHandler extends HttpHandler {

  private static final long ACTIVE_REQUEST_LIFETIME = 10000; // 10 sec
  private static final long PERFORMED_REQUEST_LIFETIME = 60000; // 60 sec

  public static class GameRequest {
    public long id = 0;
    public long ts = 0;
    public String gameId = "";
    public boolean performed = false;

    public boolean isExpired() {
      long lifetime = performed ? PERFORMED_REQUEST_LIFETIME : ACTIVE_REQUEST_LIFETIME;
      return ts + lifetime < Calendar.getInstance().getTimeInMillis();
    }
  }

  private long lastId;
  private GameRequest curGameRequest = null;
  private HashMap<Long, GameRequest> performedGameRequests;


  public RndHttpHandler() {
    super();
    lastId = 99 + (long) ((new Random()).nextDouble() * 9999);
    performedGameRequests = new HashMap<>();
  }

  @Override
  synchronized public void service(Request request, Response response) throws Exception {
    String idParameter = request.getParameter("id");
    String cb = request.getParameter("callback");

    if (idParameter == null) {
      gameRequestWithoutId(cb, response);
    } else {
      long id = Long.parseLong(idParameter);
      gameRequestWithId(id, cb, response);
    }

    cleanupPerformedGameRequests();
  }

  private void gameRequestWithId(long id, String cb, Response response) throws Exception {
    if (performedGameRequests.containsKey(id)) {
      sendResponse("{\"gameId\":\"" + performedGameRequests.get(id).gameId + "0\"}", cb, response);

      performedGameRequests.remove(id);
      return;
    }

    if (curGameRequest != null && curGameRequest.id == id) {
      curGameRequest.ts = Calendar.getInstance().getTimeInMillis();

      sendResponse("{\"id\":\"" + curGameRequest.id + "\"}", cb, response);
      return;
    }

    gameRequestWithoutId(cb, response);
  }

  private void gameRequestWithoutId(String cb, Response response) throws Exception {
    if (curGameRequest == null || curGameRequest.isExpired()) {
      curGameRequest = new GameRequest();
      curGameRequest.id = lastId++;
      curGameRequest.ts = Calendar.getInstance().getTimeInMillis();

      sendResponse("{\"id\":\"" + curGameRequest.id + "\"}", cb, response);

      return;
    }

    createGame();
    sendResponse("{\"gameId\":\"" + curGameRequest.gameId + "1\"}", cb, response);

    curGameRequest.performed = true;
    performedGameRequests.put(curGameRequest.id, curGameRequest);
    curGameRequest = null;

    return;
  }

  private void cleanupPerformedGameRequests() {
    List<Long> keysToDelete = new ArrayList<>();

    performedGameRequests.entrySet().forEach(entry -> {
      if (entry.getValue().isExpired()) {
        keysToDelete.add(entry.getKey());
      }
    });

    keysToDelete.forEach(performedGameRequests::remove);
  }

  private void sendResponse(String resp, String cb, Response response) throws Exception {
    if (cb != null) {
      resp = cb + "(" + resp + ");";
    }

    response.setContentType("application/json");
    response.setContentLength(resp.length());
    response.getWriter().write(resp);
  }

  private void createGame() {
    Command cmd = new Command();
    cmd.action = "newGame";
    Result result = GameJSON.getInstance().executeCommand(cmd);
    curGameRequest.gameId = result.message;
  }
}
