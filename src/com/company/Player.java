package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 14/04/2017.
 */
public class Player {
    private int id;
    private List<Referee.Ship> ships;
    private List<Referee.Ship> shipsAlive;

    public Player(int id) {
        this.id = id;
        this.ships = new ArrayList<>();
        this.shipsAlive = new ArrayList<>();
    }

    public void setDead() {
        for (Referee.Ship ship : ships) {
            ship.health = 0;
        }
    }

    public int getScore() {
        int score = 0;
        for (Referee.Ship ship : ships) {
            score += ship.health;
        }
        return score;
    }

    public List<String> toViewString() {
        List<String> data = new ArrayList<>();

        data.add(String.valueOf(this.id));
        for (Referee.Ship ship : ships) {
            data.add(ship.toViewString());
        }

        return data;
    }
}
