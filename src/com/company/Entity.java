package com.company;


/**
 * Created by Nicolas on 14/04/2017.
 */
abstract class Entity {
    private static int UNIQUE_ENTITY_ID = 0;

    protected final int id;
    protected final EntityType type;
    protected Coord position;

    public Entity(EntityType type, int x, int y) {
        this.id = UNIQUE_ENTITY_ID++;
        this.type = type;
        this.position = new Coord(x, y);
    }

    public String toViewString() {
        return Referee.join(id, position.y, position.x);
    }

    protected String toPlayerString(int arg1, int arg2, int arg3, int arg4) {
        return Referee.join(id, type.name(), position.x, position.y, arg1, arg2, arg3, arg4);
    }
}
