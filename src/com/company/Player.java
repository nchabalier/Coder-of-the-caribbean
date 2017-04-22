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



        int round = 1;

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
                        Referee.Ship ship = new Referee.Ship(entityId, x,y,arg1, arg2, arg3, arg4);
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
            ref.prepare(round);


            String[] outputs = new String[myShipCount];

            for (int i = 0; i < myShipCount; i++) {


                List<Referee.Ship> ennemiesShips = players.get(0).getShips();
                List<Referee.Ship> myShips = players.get(1).getShips();

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");

                if(round % 4 == 0) {
                    //Fire the ennemie 1 on 4 tours
                    outputs[i] = "FIRE " + ennemiesShips.get(0).toPositionString();

                } else {

                    Referee.Entity nearestEntity = myShips.get(i).getNearestEntity(rumBarrels);
                    if(nearestEntity!=null) {
                        outputs[i] = "MOVE " + nearestEntity.toPositionString(); // Any valid action, such as "WAIT" or "MOVE x y"
                    } else {
                        Random rand = new Random();
                        int xRand = Math.abs(rand.nextInt())%Referee.MAP_WIDTH;
                        int yRand = Math.abs(rand.nextInt())%Referee.MAP_HEIGHT;
                        outputs[i] = "MOVE " + xRand + " " + yRand; // Any valid action, such as "WAIT" or "MOVE x y"
                    }
                }


            }

            try {
                ref.handlePlayerOutput(1,round,1,outputs);
                ref.updateGame(round);
            } catch (Exception e) {
                e.printStackTrace();
            }


            //Generate random solution and test it
            int[] solution = generateRandomSolution(3,myShipCount);
            int score = evaluateSolution(round,3,myShipCount,solution,ref);
            System.err.println("Score: " + score);

            for (int i = 0; i < myShipCount; i++) {
                System.out.println(outputs[i]);
            }

            round++;


            String[] myInput = ref.getInputForPlayer(round,1);
            for(String line : myInput) {
                System.err.println(line);
            }

        }
    }

    /**
     * Generate a random solution with a vector [ship1MoveAtRound1, ship2MoveAtRound1, ..., ship1MoveAtRoundnbOfRounds, ship2MoveAtRoundnbOfRounds]
     * @param nbOfRounds : nb of round to evaluate
     * @param nbOfShips : nb of ships
     * @return the random solution
     */
    public static int[] generateRandomSolution(int nbOfRounds, int nbOfShips) {
        int[] solution = new int[nbOfRounds*nbOfShips];
        Random rand = new Random();

        for(int i = 0; i<nbOfRounds*nbOfShips; i++) {
            solution[i] = rand.nextInt(7);
        }

        return solution;
    }

    public static void displaySolution(int[] solution) {
        System.err.print("SOLUTION ");
        for(int i = 0; i<solution.length; i++) {
            System.err.print(solution[i] + " ");
        }
        System.err.println("");
    }

    public static int evaluateSolution(int currentRound, int nbOfRounds, int nbOfShips, int[] mySolution, Referee ref) {

        displaySolution(mySolution);

        //Make a copy of ref
        Referee testRef = new Referee(ref);



        for(int i = 0; i<nbOfRounds; i++) {
            testRef.prepare(currentRound);
            String[] currentOutputs = getOutputOfSolution(mySolution,i,nbOfShips,testRef);

            for(int k=0; k<nbOfShips; k++) {
                System.err.println("Move(" + k + ") = " + currentOutputs[k]);
            }

            try {
                testRef.handlePlayerOutput(1,currentRound,1,currentOutputs);
                testRef.updateGame(currentRound);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        int score = ref.evaluateScore(testRef);
        return score;
    }

    public static String[] getOutputOfSolution(int[] solution, int index, int nbShips, Referee ref) {
        String[] outputs = new String[nbShips];

        for (int i = index * nbShips; i < index* nbShips + nbShips; i++) {

            int currentAction = solution[i];
            switch (currentAction) {
                case 0: // FIRE x y
                    Referee.Coord coord = ref.getNearestEnnemiesCoord(1, i - index * nbShips);
                    outputs[i - index * nbShips] = "FIRE " + coord.toString();
                    break;
                case 1: // MINE
                    outputs[i - index * nbShips] = "MINE";
                    break;
                case 2: // PORT
                    outputs[i - index * nbShips] = "PORT";
                    break;
                case 3: // STARBOARD
                    outputs[i - index * nbShips] = "STARBOARD";
                    break;
                case 4: // FASTER
                    outputs[i - index * nbShips] = "FASTER";
                    break;
                case 5: // SLOWER
                    outputs[i - index * nbShips] = "SLOWER";
                    break;
                case 6: // WAIT
                    outputs[i - index * nbShips] = "WAIT";
                    break;

            }

        }

        return outputs;
    }

}

