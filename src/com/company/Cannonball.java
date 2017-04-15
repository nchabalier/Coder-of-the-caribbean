package com.company;


/**
 * Created by Nicolas on 14/04/2017.
 */
class Cannonball extends Entity {
    final int ownerEntityId;
    final int srcX;
    final int srcY;
    final int initialRemainingTurns;
    int remainingTurns;

    public Cannonball(int row, int col, int ownerEntityId, int srcX, int srcY, int remainingTurns) {
        super(EntityType.CANNONBALL, row, col);
        this.ownerEntityId = ownerEntityId;
        this.srcX = srcX;
        this.srcY = srcY;
        this.initialRemainingTurns = this.remainingTurns = remainingTurns;
    }

    public String toViewString() {
        return Referee.join(id, position.y, position.x, srcY, srcX, initialRemainingTurns, remainingTurns, ownerEntityId);
    }

    public String toPlayerString(int playerIdx) {
        return toPlayerString(ownerEntityId, remainingTurns, 0, 0);
    }
}
