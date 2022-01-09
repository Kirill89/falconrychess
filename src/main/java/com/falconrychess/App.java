/*
* mvn package -Dmaven.test.skip=true
* java -jar target/game-1.0-SNAPSHOT.jar
* mvn test
* */

package com.falconrychess;


import com.falconrychess.controller.Command;
import com.falconrychess.controller.GameJSON;
import com.falconrychess.controller.Result;
import com.google.gson.Gson;
import org.glassfish.grizzly.http.server.*;

import java.io.File;

public class App {
public static void main(String[] args) {
  HttpServer server = HttpServer.createSimpleServer("web", Config.PORT);

  server.getServerConfiguration().addHttpHandler(new HttpHandler() {
    public void service(Request request, Response response) throws Exception {
      Gson gson = new Gson();
      Command cmd = gson.fromJson(request.getParameter("command"), Command.class);
      Result result;

      if (cmd != null) {
        result = GameJSON.getInstance().executeCommand(cmd);
      } else {
        result = new Result();
        result.status = "error";
        result.message = "brokenCommand";
      }

      String resp = gson.toJson(result);

      String cb = request.getParameter("callback");

      if (cb != null) {
      resp = cb + "(" + resp + ");";
      }

      response.setCharacterEncoding("utf-8");
      response.setContentType("application/json");
      response.setContentLength(resp.getBytes("UTF-8").length);
      response.getWriter().write(resp);
    }
  }, "/game");

  server.getServerConfiguration().addHttpHandler(new RndHttpHandler(), "/rnd");

    try {
      server.start();
      System.out.println("Press CTRL^C to exit..");
      Thread.currentThread().join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
