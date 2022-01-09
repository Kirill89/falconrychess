# Falconry Chess

A 100-square (10x10) chess board game with three additional pieces.

Play here: http://falconrychess.com/

Learn more about the rules, and the game itself [here](http://www.fishka.spb.ru/artickles/recenzii/delfini/75.htm) (rus).

Note: It is a result of my 2012 university diploma project. So, the code is, say the least of it, not perfect.

![](screenshot.png?raw=true)

## Deploy

The server use MongoDB to store games. The easiest way to run server locally:

```shell
docker run -it --rm -p 27017:27017 --name mongo mongo:4
mvn package -Dmaven.test.skip=true
java -jar target/game-1.0-SNAPSHOT.jar
```

At this point you can open http://localhost:8080 in your browser.

## License

MIT
