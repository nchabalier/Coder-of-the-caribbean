package com.company;


import java.util.List;


/**
 * Created by Nicolas on 14/04/2017.
 */
class Ship extends Entity {
    int orientation;

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    int speed;
    int health;
    int owner;
    String message;
    Action action;
    int mineCooldown;
    int cannonCooldown;
    Coord target;
    public int newOrientation;
    public Coord newPosition;
    public Coord newBowCoordinate;
    public Coord newSternCoordinate;

    public Ship(int x, int y, int orientation, int owner) {
        super(EntityType.SHIP, x, y);
        this.orientation = orientation;
        this.speed = 0;
        this.health = Referee.INITIAL_SHIP_HEALTH;
        this.owner = owner;
    }

    public String toViewString() {
        return Referee.join(id, position.y, position.x, orientation, health, speed, (action != null ? action : "WAIT"), bow().y, bow().x, stern().y,
                stern().x, " ;" + (message != null ? message : ""));
    }

    public String toPlayerString(int playerIdx) {
        return toPlayerString(orientation, speed, health, owner == playerIdx ? 1 : 0);
    }

    public void setMessage(String message) {
        if (message != null && message.length() > 50) {
            message = message.substring(0, 50) + "...";
        }
        this.message = message;
    }

    public void moveTo(int x, int y) {
        Coord currentPosition = this.position;
        Coord targetPosition = new Coord(x, y);

        if (currentPosition.equals(targetPosition)) {
            this.action = Action.SLOWER;
            return;
        }

        double targetAngle, angleStraight, anglePort, angleStarboard, centerAngle, anglePortCenter, angleStarboardCenter;

        switch (speed) {
            case 2:
                this.action = Action.SLOWER;
                break;
            case 1:
                // Suppose we've moved first
                currentPosition = currentPosition.neighbor(orientation);
                if (!currentPosition.isInsideMap()) {
                    this.action = Action.SLOWER;
                    break;
                }

                // Target reached at next turn
                if (currentPosition.equals(targetPosition)) {
                    this.action = null;
                    break;
                }

                // For each neighbor cell, find the closest to target
                targetAngle = currentPosition.angle(targetPosition);
                angleStraight = Math.min(Math.abs(orientation - targetAngle), 6 - Math.abs(orientation - targetAngle));
                anglePort = Math.min(Math.abs((orientation + 1) - targetAngle), Math.abs((orientation - 5) - targetAngle));
                angleStarboard = Math.min(Math.abs((orientation + 5) - targetAngle), Math.abs((orientation - 1) - targetAngle));

                centerAngle = currentPosition.angle(new Coord(Referee.MAP_WIDTH / 2, Referee.MAP_HEIGHT / 2));
                anglePortCenter = Math.min(Math.abs((orientation + 1) - centerAngle), Math.abs((orientation - 5) - centerAngle));
                angleStarboardCenter = Math.min(Math.abs((orientation + 5) - centerAngle), Math.abs((orientation - 1) - centerAngle));

                // Next to target with bad angle, slow down then rotate (avoid to turn around the target!)
                if (currentPosition.distanceTo(targetPosition) == 1 && angleStraight > 1.5) {
                    this.action = Action.SLOWER;
                    break;
                }

                Integer distanceMin = null;

                // Test forward
                Coord nextPosition = currentPosition.neighbor(orientation);
                if (nextPosition.isInsideMap()) {
                    distanceMin = nextPosition.distanceTo(targetPosition);
                    this.action = null;
                }

                // Test port
                nextPosition = currentPosition.neighbor((orientation + 1) % 6);
                if (nextPosition.isInsideMap()) {
                    int distance = nextPosition.distanceTo(targetPosition);
                    if (distanceMin == null || distance < distanceMin || distance == distanceMin && anglePort < angleStraight - 0.5) {
                        distanceMin = distance;
                        this.action = Action.PORT;
                    }
                }

                // Test starboard
                nextPosition = currentPosition.neighbor((orientation + 5) % 6);
                if (nextPosition.isInsideMap()) {
                    int distance = nextPosition.distanceTo(targetPosition);
                    if (distanceMin == null || distance < distanceMin
                            || (distance == distanceMin && angleStarboard < anglePort - 0.5 && this.action == Action.PORT)
                            || (distance == distanceMin && angleStarboard < angleStraight - 0.5 && this.action == null)
                            || (distance == distanceMin && this.action == Action.PORT && angleStarboard == anglePort
                            && angleStarboardCenter < anglePortCenter)
                            || (distance == distanceMin && this.action == Action.PORT && angleStarboard == anglePort
                            && angleStarboardCenter == anglePortCenter && (orientation == 1 || orientation == 4))) {
                        distanceMin = distance;
                        this.action = Action.STARBOARD;
                    }
                }
                break;
            case 0:
                // Rotate ship towards target
                targetAngle = currentPosition.angle(targetPosition);
                angleStraight = Math.min(Math.abs(orientation - targetAngle), 6 - Math.abs(orientation - targetAngle));
                anglePort = Math.min(Math.abs((orientation + 1) - targetAngle), Math.abs((orientation - 5) - targetAngle));
                angleStarboard = Math.min(Math.abs((orientation + 5) - targetAngle), Math.abs((orientation - 1) - targetAngle));

                centerAngle = currentPosition.angle(new Coord(Referee.MAP_WIDTH / 2,Referee. MAP_HEIGHT / 2));
                anglePortCenter = Math.min(Math.abs((orientation + 1) - centerAngle), Math.abs((orientation - 5) - centerAngle));
                angleStarboardCenter = Math.min(Math.abs((orientation + 5) - centerAngle), Math.abs((orientation - 1) - centerAngle));

                Coord forwardPosition = currentPosition.neighbor(orientation);

                this.action = null;

                if (anglePort <= angleStarboard) {
                    this.action = Action.PORT;
                }

                if (angleStarboard < anglePort || angleStarboard == anglePort && angleStarboardCenter < anglePortCenter
                        || angleStarboard == anglePort && angleStarboardCenter == anglePortCenter && (orientation == 1 || orientation == 4)) {
                    this.action = Action.STARBOARD;
                }

                if (forwardPosition.isInsideMap() && angleStraight <= anglePort && angleStraight <= angleStarboard) {
                    this.action = Action.FASTER;
                }
                break;
        }

    }

    public void faster() {
        this.action = Action.FASTER;
    }

    public void slower() {
        this.action = Action.SLOWER;
    }

    public void port() {
        this.action = Action.PORT;
    }

    public void starboard() {
        this.action = Action.STARBOARD;
    }

    public void placeMine() {
        if (Referee.MINES_ENABLED) {
            this.action = Action.MINE;
        }
    }

    public Coord stern() {
        return position.neighbor((orientation + 3) % 6);
    }

    public Coord bow() {
        return position.neighbor(orientation);
    }

    public Coord newStern() {
        return position.neighbor((newOrientation + 3) % 6);
    }

    public Coord newBow() {
        return position.neighbor(newOrientation);
    }

    public boolean at(Coord coord) {
        Coord stern = stern();
        Coord bow = bow();
        return stern != null && stern.equals(coord) || bow != null && bow.equals(coord) || position.equals(coord);
    }

    public boolean newBowIntersect(Ship other) {
        return newBowCoordinate != null && (newBowCoordinate.equals(other.newBowCoordinate) || newBowCoordinate.equals(other.newPosition)
                || newBowCoordinate.equals(other.newSternCoordinate));
    }

    public boolean newBowIntersect(List<Ship> ships) {
        for (Ship other : ships) {
            if (this != other && newBowIntersect(other)) {
                return true;
            }
        }
        return false;
    }

    public boolean newPositionsIntersect(Ship other) {
        boolean sternCollision = newSternCoordinate != null && (newSternCoordinate.equals(other.newBowCoordinate)
                || newSternCoordinate.equals(other.newPosition) || newSternCoordinate.equals(other.newSternCoordinate));
        boolean centerCollision = newPosition != null && (newPosition.equals(other.newBowCoordinate) || newPosition.equals(other.newPosition)
                || newPosition.equals(other.newSternCoordinate));
        return newBowIntersect(other) || sternCollision || centerCollision;
    }

    public boolean newPositionsIntersect(List<Ship> ships) {
        for (Ship other : ships) {
            if (this != other && newPositionsIntersect(other)) {
                return true;
            }
        }
        return false;
    }

    public void damage(int health) {
        this.health -= health;
        if (this.health <= 0) {
            this.health = 0;
        }
    }

    public void heal(int health) {
        this.health += health;
        if (this.health > Referee.MAX_SHIP_HEALTH) {
            this.health = Referee.MAX_SHIP_HEALTH;
        }
    }

    public void fire(int x, int y) {
        if (Referee.CANNONS_ENABLED) {
            Coord target = new Coord(x, y);
            this.target = target;
            this.action = Action.FIRE;
        }
    }
}
