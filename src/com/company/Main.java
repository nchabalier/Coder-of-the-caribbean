package com.company;

import java.io.IOException;
import java.util.Properties;

class Main {

    public static final int SHIPS_COUNT = 2;

    public static void main(String[] args) throws Exception {
        System.out.println("Coucou");
        Properties properties = new Properties();
        properties.put("seed", "1");
        properties.put("shipsPerPlayer", String.valueOf(SHIPS_COUNT));
        properties.put("mineCount", "0");
        properties.put("barrelCount", "100");

        Referee ref = new Referee(System.in, System.out, System.err);
        ref.initReferee(2, properties);
        System.out.println(ref.getConfiguration());

        int round = 1;

        while(round < 100) {

            ref.prepare(round);

            String[] outputs = new String[SHIPS_COUNT];
            outputs[0] = "MOVE 9 16";
            outputs[1] = "MOVE 17 17";
            ref.handlePlayerOutput(1,round,1,outputs);

            ref.updateGame(round);
            String[] action = ref.getPlayerActions(1,round);
            String[] inputs = ref.getInputForPlayer(round,1);

            Player.displayStringArray(inputs);


            ref.displayMap();
        }

        //int myShipCount = Integer.parseInt(inputs[0]);


    }
}
