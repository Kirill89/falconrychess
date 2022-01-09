package com.falconrychess.logic;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Main extends JFrame {
  private Game game = new Game();
  private JButton[][] jButton;

  private Point from, to;

  private static final long serialVersionUID = 2582788285973231132L;

  private void suncField() {
    setTitle(game.getSide().toString());
    String field[][] = game.getField();
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (!field[i][j].equals("")) {
          jButton[i][j].setText(field[i][j]);
        } else {
          jButton[i][j].setText("");
        }
      }
    }
  }

  private void drawArea(int x, int y) {
    if (x == 0 && y == 0) {
      try {
        FileOutputStream fos = new FileOutputStream("temp.out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(game.getLog());
        oos.flush();
        oos.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (x == 9 && y == 9) {
      try {
        FileInputStream fis = new FileInputStream("temp.out");
        ObjectInputStream oin = new ObjectInputStream(fis);
        game = new Game((Log) oin.readObject());
        suncField();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    boolean c = false;
    for (Integer i = 0; i < 10; i++) {
      c = !c;
      for (Integer j = 0; j < 10; j++) {
        c = !c;
        if (c)
          jButton[i][j].setBackground(Color.white);
        else
          jButton[i][j].setBackground(Color.lightGray);
      }
    }
    Point point = new Point(x, y);
    MoveArea ma = game.getMoveArea(point);
    if (ma != null) {
      for (Integer i = 0; i < 10; i++) {
        for (Integer j = 0; j < 10; j++) {
          if (!ma.getAction(new Point(i, j)).equals(Action.NONE)) {
            jButton[i][j].setBackground(Color.cyan);
          }
        }
      }
      from = point;
    }
  }

  public static void main(String[] args) {
    // ���������
    // http://ru.wikipedia.org/wiki/%D0%A0%D0%BE%D0%BA%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0
    // ������ �� �������
    // http://ru.wikipedia.org/wiki/%D0%92%D0%B7%D1%8F%D1%82%D0%B8%D0%B5_%D0%BD%D0%B0_%D0%BF%D1%80%D0%BE%D1%85%D0%BE%D0%B4%D0%B5
    // ������� ������
    // http://ru.wikipedia.org/wiki/%D0%A8%D0%B0%D1%85%D0%BC%D0%B0%D1%82%D1%8B#.D0.9F.D1.80.D0.B0.D0.B2.D0.B8.D0.BB.D0.B0
    // ���������� JSON ����������
    // http://www.json.org/java/index.html

    new Main().setVisible(true);
  }

  public Main() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setLocation(0, 0);
    setSize(700, 700);
    setTitle("Chess");

    Container container = getContentPane();
    container.setLayout(new GridLayout(10, 10));

    jButton = new JButton[10][];
    for (Integer i = 0; i < 10; i++) {
      jButton[i] = new JButton[10];
      for (Integer j = 0; j < 10; j++) {
        jButton[i][j] = new JButton(" ");
        jButton[i][j].setForeground(Color.gray);
      }
    }
    for (Integer i = 0; i < 10; i++) {
      for (Integer j = 0; j < 10; j++) {
        container.add(jButton[j][i]);
        jButton[j][i].setName("b" + j.toString() + i.toString());
        jButton[j][i].addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            Integer x = Integer.parseInt(b.getName()
              .substring(1, 2));
            Integer y = Integer.parseInt(b.getName()
              .substring(2, 3));
            if (jButton[x][y].getBackground().equals(Color.cyan)) {
              to = new Point(x, y);
              game.replaceFigure(new Queen(null, null));
              game.moveFigure(from, to);
              suncField();
            } else {
              drawArea(x, y);
            }
          }
        });
      }
    }
    suncField();
    drawArea(0, 8);
  }

  public void windowClosing(WindowEvent ev) {
    System.exit(0);
  }

}
