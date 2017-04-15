package com.company;

import static com.company.Referee.join;

/**
 * Created by Nicolas on 14/04/2017.
 */
public class Damage {
    private final Coord position;
    private final int health;
    private final boolean hit;

    public Damage(Coord position, int health, boolean hit) {
        this.position = position;
        this.health = health;
        this.hit = hit;
    }

    public String toViewString() {
        return join(position.y, position.x, health, (hit ? 1 : 0));
    }
}