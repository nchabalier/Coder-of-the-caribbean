package com.company;

import static com.company.Referee.join;

/**
 * Created by Nicolas on 14/04/2017.
 */
public class RumBarrel extends Entity {
    private int health;

    public RumBarrel(int x, int y, int health) {
        super(EntityType.BARREL, x, y);
        this.health = health;
    }

    public String toViewString() {
        return join(id, position.y, position.x, health);
    }

    public String toPlayerString(int playerIdx) {
        return toPlayerString(health, 0, 0, 0);
    }
}