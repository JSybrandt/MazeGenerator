package model;

import Util.Pair;
import Util.Vec2;
import jdk.nashorn.internal.runtime.options.Option;

import java.util.*;

/**
 * Justin Sybrandt
 *
 * The room class defines a single element in a Maze.
 * A room has some number of walls, each wall is shared with another room.
 * Walls are removed to create a maze.
 */
public abstract class Room {

    public enum RoomType{Triangle, Square, Hexagon};

    public static final double DEFAULT_ROTATION = 0;
    public static double DEFAULT_WALL_LENGTH = 20;

    //Basic Room Def
    private int numWalls;
    private RoomType roomType;
    protected Vec2 location;
    private double rotation;


    //Note: Corners are relative to location
    private List<Vec2> corners;
    protected List<Wall> walls;

    public Room(RoomType roomType, Vec2 location){
        this(roomType,location,DEFAULT_ROTATION);
    }
    public Room(RoomType roomType, Vec2 location, double rotation){

        //Set def
        this.roomType = roomType;
        this.location = location;
        this.rotation = rotation;

        //Set Walls
        double wallLength = DEFAULT_WALL_LENGTH;
        walls = new ArrayList<Wall>();
        switch (roomType){
            case Triangle:
                numWalls = 3;
                break;
            case Square:
                numWalls = 4;
                break;
            case Hexagon:
                numWalls = 6;
                break;
        }
        double angle = -2*Math.PI/numWalls;
        corners = new ArrayList<Vec2>();
        //The strat is to go in a circle building walls starting at 0,0 then translate by the center.
        corners.add(new Vec2(0,0));
        Vec2 wallVec = new Vec2(wallLength,0);
        Vec2 avgPos = new Vec2(0,0);
        for(int i=0; i<numWalls-1;i++){
            Vec2 newCorner = corners.get(i).plus(wallVec.rotate(angle * i));
            corners.add(newCorner);
            avgPos = avgPos.plus(newCorner);
        }
        avgPos = avgPos.scale(1.0/numWalls);
        //now we need to center the corners around (0,0) and rotate them.
        for(int i=0;i<corners.size();i++){
            corners.set(i,corners.get(i).minus(avgPos).rotate(rotation).plus(location));
        }

        //make new walls from the set of corners. ATM this assumes each wall is outside
        for(int i=1;i<=corners.size();i++){
            walls.add(new Wall(corners.get(i-1),corners.get(i%corners.size()),this,null));
        }
   }

    public List<Vec2>getCorners(){
       return corners;
   }
    public int getNumWalls(){return numWalls;}

    public Wall getWall(int index){return walls.get(index);}

    //searches both rooms, looking for the overlap. Returns the resulting room if exists
    public Optional<Wall> setAdjacentRoom(Room other){
        for(int i = 0 ; i < getNumWalls(); i++)
            for(int j = 0; j < other.getNumWalls(); j++){
                if(getWall(i).equals(other.getWall(j))){
                    boolean isOpen = getWall(i).isOpen || other.getWall(j).isOpen;
                    Wall newWall = new Wall(getWall(i).getLocations(),new Pair<Room>(this,other));
                    newWall.isOpen = isOpen;
                    walls.set(i,newWall);
                    other.walls.set(j,newWall);
                    return Optional.of(newWall);
                }
            }
        return Optional.empty();
    }

    //searches both rooms looking for overlap wall
    public Optional<Pair<Wall>> getAdjacentWalls(Room other){

        for(Wall thisWall : walls)
            for(Wall otherWall : other.walls)
                if(thisWall.equals(otherWall))
                    return Optional.of(new Pair<>(thisWall,otherWall));
        return Optional.empty();
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Room){
            Room o = (Room) object;
            return location.equals(o.location)
                    && this.rotation == o.rotation
                    && this.roomType == o.roomType;
        }
        return false;
    }
}
