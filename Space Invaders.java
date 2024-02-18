//inbuilt package
package com.example.space_invaders;

//import libraries
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//making the class for basic interface
public class SpaceInvadersGame extends Application
    {
        private Pane root=new Pane();
        private double t=0;
        private Sprite player=new Sprite(300,750,40,40,"Player",Color.CYAN);
        private Parent createContent()
        {
            root.setPrefSize(600,800);
            root.getChildren().add(player);
            AnimationTimer timer=new AnimationTimer()
            {
                @Override
                public void handle(long now)
                {
                   update();
                }
            };
            timer.start();
            //when all the current enemies are destroyed, start a new level
            nextLevel();
            return root;
        }

        private void nextLevel()
        {
            for(int i=0;i<5;i++)
            {
                for(int j=0;j<3;j++)
                {
                    Sprite s=new Sprite(90+i*100,50+j*100,30,30,"Enemy",Color.RED);
                    root.getChildren().add(s);
                }

            }
        }

        private List<Sprite> sprites()
        {
            return root.getChildren().stream().map(n->(Sprite)n).collect(Collectors.toList());
        }
        private void update()
        {
            t+=0.016;
            sprites().forEach(s->{
                switch(s.type)
                {
                    case "EnemyBullet":
                        s.movedown();
                        if(s.getBoundsInParent().intersects(player.getBoundsInParent()))
                        {
                            player.dead=true;
                            s.dead=true;
                        }
                        break;
                    case "PlayerBullet":
                        s.moveup();
                        sprites().stream().filter(e->e.type.equals("Enemy")).forEach(enemy->{
                            if(s.getBoundsInParent().intersects(enemy.getBoundsInParent()))
                            {
                                enemy.dead=true;
                                s.dead=true;
                            }
                        });
                        break;
                    case "Enemy":
                        if(t>2)
                        {
                            if(Math.random()<0.3)
                            {
                                shoot(s);
                            }
                        }
                        break;
                }
            });
            //when objects are dead, remove them from screen
            root.getChildren().removeIf(n->{
                Sprite s=(Sprite) n;
                return s.dead;
            });
            if(t>2)
            {
                t=0;
            }
        }

        private void shoot(Sprite who)
        {
            Sprite s=new Sprite((int)who.getTranslateX()+20,(int)who.getTranslateY(),5,20,who.type+"Bullet",Color.BLACK);
            root.getChildren().add(s);
        }
        public void start(Stage stage) throws Exception
        {
            Scene scene=new Scene(createContent());
            scene.setOnKeyPressed(e->{
                switch(e.getCode())
                {
                    case A:
                        player.moveleft();
                        break;//in case when A is pressed, move to left
                    case D:
                        player.moveright();
                        break;//in case when D is pressed, move to right
                    case W:
                        player.moveup();
                        break;//in case when W is pressed, move up
                    case S:
                        player.movedown();
                        break;//in case when S is pressed, move down
                    case SPACE:
                        shoot(player);
                        break;//in case when space bar is pressed, shoot
                }
            });
            stage.setScene(scene);
            stage.show();
        }
        private static class Sprite extends Rectangle
        {
            boolean dead=false;
            final String type;
            Sprite(int x, int y, int w, int h, String type, Color color)
            {
                super(w,h,color);
                this.type=type;
                setTranslateX(x);
                setTranslateY(y);
            }
            //for movement of the enemies and players
            void moveleft()
            {
                setTranslateX(getTranslateX()-5);
            }
            void moveright()
            {
                setTranslateX(getTranslateX()+5);
            }
            void moveup()
            {
                setTranslateY(getTranslateY()-5);
            }
            void movedown()
            {
                setTranslateY(getTranslateY()+5);
            }
        }
        public static void main(String args[])
        {
            launch (args);
        }
    }