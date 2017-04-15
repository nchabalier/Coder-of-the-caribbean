package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 14/04/2017.
 */
class Mine extends Entity {
    public Mine(int x, int y) {
        super(EntityType.MINE, x, y);
    }

    public String toPlayerString(int playerIdx) {
        return toPlayerString(0, 0, 0, 0);
    }

    public List<Damage> explode(List<Ship> ships, boolean force) {
        List<Damage> damage = new ArrayList<>();
        Ship victim = null;

        for (Ship ship : ships) {
            if (position.equals(ship.bow()) || position.equals(ship.stern()) || position.equals(ship.position)) {
                damage.add(new Damage(this.position, Referee.MINE_DAMAGE, true));
                ship.damage(Referee.MINE_DAMAGE);
                victim = ship;
            }
        }

        if (force || victim != null) {
            if (victim == null) {
                damage.add(new Damage(this.position, Referee.MINE_DAMAGE, true));
            }

            for (Ship ship : ships) {
                if (ship != victim) {
                    Coord impactPosition = null;
                    if (ship.stern().distanceTo(position) <= 1) {
                        impactPosition = ship.stern();
                    }
                    if (ship.bow().distanceTo(position) <= 1) {
                        impactPosition = ship.bow();
                    }
                    if (ship.position.distanceTo(position) <= 1) {
                        impactPosition = ship.position;
                    }

                    if (impactPosition != null) {
                        ship.damage(Referee.NEAR_MINE_DAMAGE);
                        damage.add(new Damage(impactPosition, Referee.NEAR_MINE_DAMAGE, true));
                    }
                }
            }
        }

        return damage;
    }
}
