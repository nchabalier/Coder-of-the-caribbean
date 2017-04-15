package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 14/04/2017.
 */
class PlayerAgent {
    private int id;
    private List<Ship> ships;
    private List<Ship> shipsAlive;

    public PlayerAgent(int id) {
        this.id = id;
        this.ships = new ArrayList<>();
        this.shipsAlive = new ArrayList<>();
    }

    public void setDead() {
        for (Ship ship : ships) {
            ship.health = 0;
        }
    }

    public int getScore() {
        int score = 0;
        for (Ship ship : ships) {
            score += ship.health;
        }
        return score;
    }

    public List<String> toViewString() {
        List<String> data = new ArrayList<>();

        data.add(String.valueOf(this.id));
        for (Ship ship : ships) {
            data.add(ship.toViewString());
        }

        return data;
    }
}
