package com.company;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Coucou");
        Referee ref = new Referee(System.in, System.out, System.err);
        Coord coord = new Coord(1, 2);
        Player player = new Player(0);
        System.out.println(coord.toString());
    }
}
