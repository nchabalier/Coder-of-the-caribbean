package com.company;

import java.io.IOException;

class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Coucou");
        Referee ref = new Referee(System.in, System.out, System.err);
        Coord coord = new Coord(1, 2);
        PlayerAgent player = new PlayerAgent(0);
        System.out.println(coord.toString());
    }
}
