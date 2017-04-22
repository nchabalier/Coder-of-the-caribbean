package com.company;

/**
 * Created by Nicolas on 14/04/2017.
 */
import com.sun.org.apache.regexp.internal.RE;

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);


        //-------------------------------TEST----------------------
        Referee ref = null;
        try {
            ref = new Referee(System.in, System.out, System.err);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ref.initReferee2();
        //-------------------------------TEST----------------------


        List<Referee.Player> players = new ArrayList<>();
        players.add(new  Referee.Player(0));
        players.add(new  Referee.Player(1));



        //TO REMOVE
        int cmpCannon= 0;

        // game loop
        while (true) {

            players.get(0).clearShip();
            players.get(1).clearShip();
            //List<Referee.Entity> myShips = new ArrayList<>();
            //List<Referee.Entity> ennemiesShips = new ArrayList<>();
            List<Referee.Entity> rumBarrels = new ArrayList<>();
            List<Referee.Entity> mines = new ArrayList<>();
            List<Referee.Entity> cannonballs = new ArrayList<>();

            int myShipCount = in.nextInt(); // the number of remaining ships
            int entityCount = in.nextInt(); // the number of entities (e.g. ships, mines or cannonballs)

            for (int i = 0; i < entityCount; i++) {
                int entityId = in.nextInt();
                String entityType = in.next();
                int x = in.nextInt();
                int y = in.nextInt();
                int arg1 = in.nextInt(); //orientation (0,5)
                int arg2 = in.nextInt(); //vitesse (0,1,2)
                int arg3 = in.nextInt(); //niveau de stock de rhum
                int arg4 = in.nextInt(); //1 si le bateau vous appartient, 0 sinon

                switch (entityType) {
                    case "SHIP":
                        Referee.Ship ship = new Referee.Ship(x,y,arg1, arg2, arg3, arg4);
                        ship.setSpeed(arg2);
                        ship.setHealth(arg3);
                        players.get(arg4).addShip(ship);


                        break;
                    case "BARREL":
                        Referee.RumBarrel rumBarrel = new Referee.RumBarrel(x,y,arg3);
                        rumBarrels.add(rumBarrel);
                        break;
                    case"MINE":
                        Referee.Mine mine = new Referee.Mine(x, y);
                        mines.add(mine);
                        break;
                    case "CANNONBALL":
                        //Change cannonball method
                        Referee.Cannonball cannonball = new Referee.Cannonball(0,0,0,0,0,1);
                        cannonballs.add(cannonball);
                        break;
                }

            }


            ref.updateReferee2(players, mines, rumBarrels, cannonballs);
            ref.prepare(1);
            try {
                //ref.updateGame(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < myShipCount; i++) {


                List<Referee.Ship> ennemiesShips = players.get(0).getShips();
                List<Referee.Ship> myShips = players.get(1).getShips();

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");

                if(cmpCannon >= 4) {
                    cmpCannon = 0;
                    //Fire the ennemie 1 on 4 tours
                    System.out.println("FIRE " + ennemiesShips.get(0).toPositionString());

                } else {

                    Referee.Entity nearestEntity = myShips.get(i).getNearestEntity(rumBarrels);
                    if(nearestEntity!=null) {
                        System.out.println("MOVE " + nearestEntity.toPositionString()); // Any valid action, such as "WAIT" or "MOVE x y"
                    } else {
                        Random rand = new Random();
                        int xRand = rand.nextInt()%Referee.MAP_WIDTH;
                        int yRand = rand.nextInt()%Referee.MAP_HEIGHT;
                        System.out.println("MOVE " + xRand + " " + yRand); // Any valid action, such as "WAIT" or "MOVE x y"
                    }
                }

            }

            cmpCannon++;
        }
    }
}
