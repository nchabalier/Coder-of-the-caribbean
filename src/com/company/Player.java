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


        // game loop
        while (true) {


            List<Ship> myShips = new ArrayList<>();
            List<RumBarrel> rumBarrels = new ArrayList<>();

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
                int arg4 = in.nextInt(); //1 si le ateau vous appartient, 0 sinon

                switch (entityType) {
                    case "SHIP":
                        //TODO: to the same for ennemy's ships
                        if(entityId == 0) {
                            Ship ship = new Ship(x,y,arg1,arg4);
                            ship.setSpeed(arg2);
                            ship.setHealth(arg3);
                        }
                        break;
                    case "BARREL":
                        RumBarrel rumBarrel = new RumBarrel(x,y,arg3);
                        rumBarrels.add(rumBarrel);
                        break;
                    case"MINE":
                        //TODO
                        break;
                    case "CANNONBALL":
                        //TODO
                        break;
                }


            }
            for (int i = 0; i < myShipCount; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");

                System.out.println("MOVE 11 10"); // Any valid action, such as "WAIT" or "MOVE x y"
            }
        }
    }
}
