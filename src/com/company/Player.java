package com.company;

/**
 * Created by Nicolas on 14/04/2017.
 */
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

        PlayerAgent myAgent = new PlayerAgent(0);

        //TO REMOVE
        int cmpCannon= 0;

        // game loop
        while (true) {


            List<Entity> myShips = new ArrayList<>();
            List<Entity> ennemiesShips = new ArrayList<>();
            List<Entity> rumBarrels = new ArrayList<>();
            List<Entity> mines = new ArrayList<>();

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
                        Ship ship = new Ship(x,y,arg1,arg4);
                        ship.setSpeed(arg2);
                        ship.setHealth(arg3);

                        if(arg4 == 1) {
                            myShips.add(ship);
                        } else {
                            ennemiesShips.add(ship);
                        }
                        break;
                    case "BARREL":
                        RumBarrel rumBarrel = new RumBarrel(x,y,arg3);
                        rumBarrels.add(rumBarrel);
                        break;
                    case"MINE":
                        Mine mine = new Mine(x, y);
                        mines.add(mine);
                        break;
                    case "CANNONBALL":
                        System.err.println("Canonball");
                        break;
                }


            }


            for (int i = 0; i < myShipCount; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");

                if(cmpCannon >= 4) {
                    cmpCannon = 0;
                    //Fire the ennemie 1 on 4 tours
                    System.out.println("FIRE " + ennemiesShips.get(0).toPositionString());

                } else {

                    Entity nearestEntity = myShips.get(i).getNearestEntity(rumBarrels);
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
