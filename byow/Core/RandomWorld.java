package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
public class RandomWorld {

    private static Random rand;
    private static long SEED;
    private static final int WIDTH = Engine.WIDTH;
    private static final int HEIGHT = Engine.HEIGHT;

    public TETile[][] RandomWorld(long seed){
        SEED = seed;
        rand = new Random(SEED);
        TETile[][] randomTiles = new TETile[WIDTH][HEIGHT];
        randomRoomGenerator(randomTiles);
        return randomTiles;
    }
    public static TETile[][] hallwayGenerator(ArrayList<Integer> path, ArrayList<Point> rooms, TETile[][] gamestate){
        //just do it for one room for now
        //for(int i = 0; i < rooms.size()-1; i++) {
            int i = 0;

            Point currC = rooms.get(i);
            Point nextC = rooms.get(i + 1);

            int currW = rooms.get(i).pointW;
            int currH = rooms.get(i).pointH;
            int nextW = rooms.get(i + 1).pointW;
            int nextH = rooms.get(i + 1).pointH;

            //random*(upperbound-lowerbound) + lowerbound
            int randomStartingPoint = rand.nextInt(4);

            //test
            randomStartingPoint = 0;
            //int left off used for signifying where we left off for a needed turn on our hallways
            Point leftOff;
            int leftOffX = 0;
            int leftOffY = 0;
            //start left
            if(randomStartingPoint == 0){
                Point hallwayStart = new Point(currC.x - currW, rand.nextInt(2*currH) + currC.y-currH, 0,0);
                //need to move arbritary amount out to the left
                int movesLeft = rand.nextInt(4);
                for(int x = hallwayStart.x; x > hallwayStart.x - movesLeft - 1; x --){
                    gamestate[x][hallwayStart.y] = Tileset.GRASS;
                    leftOffX = x;
                }
                leftOff = new Point(leftOffX, hallwayStart.y, 0,0);
                //choose between up or down (0 is down 1 is up)
                int upDown = rand.nextInt(2);
                if(upDown == 0){
                    for(int y = leftOff.y; y > currC.y - currH - rand.nextInt(4); y--){
                        gamestate[leftOff.x][y] = Tileset.GRASS;
                        leftOffY = y;
                    }
                }else{
                    for(int y = leftOff.y; y < currC.y + currH + rand.nextInt(4); y--){
                        gamestate[leftOff.x][y] = Tileset.GRASS;
                        leftOffY = y;
                    }
                }
                leftOff = new Point(leftOffX,leftOffY,0,0);

                //move right until reach new boundary
                for(int x = leftOff.x; x < nextC.x; i++){
                    if(gamestate[x][leftOff.y].description() == "wall"){
                        break;
                    }
                    gamestate[x][leftOff.y] = Tileset.GRASS;
                    leftOffX = x;
                }
                leftOff = new Point(leftOffX, leftOffY,0,0);
                //if we went up, now we need ot go down and vice verse
                if(upDown == 0) {
                    for (int y = leftOff.y; y < nextC.y; y++) {
                        if (gamestate[leftOff.x][y].description() == "wall") {
                            break;
                        }
                        gamestate[leftOff.x][y] = Tileset.GRASS;
                        leftOffY = y;
                    }
                } else {
                    for (int y = leftOff.y; y > nextC.y; y--) {
                        if (gamestate[leftOff.x][y].description() == "wall") {
                            break;
                        }
                        gamestate[leftOff.x][y] = Tileset.GRASS;
                        leftOffY = y;
                    }
                }


            }
            //start right
            if(randomStartingPoint == 1){
                Point hallwayStart = new Point(currC.x + currW, rand.nextInt(2*currH) + currC.y-currH, 0,0);
            }
            //start top
            if(randomStartingPoint == 2){
                Point hallwayStart = new Point(rand.nextInt(2*currW) + currC.x - currW, currC.y + currH,0,0);
            }
            //start bottom
            if(randomStartingPoint == 3){
                Point hallwayStart = new Point(rand.nextInt(2*currW) + currC.x - currW, currC.y - currH,0,0);
            }

            //take random hallwayStart

            //old method of just going from center to center
            /*
            for (int x = currC.x; x < nextC.x; x++) {
                gamestate[x][currC.y] = Tileset.GRASS;
            }
            if(nextC.y >= currC.y){
                for (int y = currC.y; y < nextC.y; y++) {
                    gamestate[nextC.x][y] = Tileset.GRASS;
                }
            } else {
                for (int y = currC.y; y > nextC.y; y--) {
                    gamestate[nextC.x][y] = Tileset.GRASS;
                }
            }

             */

       // }
        return gamestate;
    }
    public static ArrayList<Integer> roomsPath2(ArrayList<Point> rooms){

        double minDistance = 1000;
        int closestNode = 0;

        int startRoom = 0;
        int endRoom;

        ArrayList<Integer> path = new ArrayList<Integer>(rooms.size());

        //assign -1 value to each index of path

        for(int i = 0; i < rooms.size(); i++){
            path.add(i,-1);
        }

        //for this maze type path, we need n-1 edges
        int edges = 0;
        while(edges < rooms.size()-1){
            int indexIterate = 0;
            while(indexIterate < rooms.size()) {
                Point starter = new Point(rooms.get(startRoom).x, rooms.get(startRoom).y,rooms.get(startRoom).pointW, rooms.get(startRoom).pointH);
                if (indexIterate != startRoom && path.get(indexIterate) == -1) {
                    Point checker = new Point(rooms.get(indexIterate).x, rooms.get(indexIterate).y, rooms.get(indexIterate).pointW, rooms.get(indexIterate).pointH);
                    double distance = distanceFormula(starter, checker);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestNode = indexIterate;
                        indexIterate += 1;
                    }else{
                        indexIterate += 1;
                    }
                } else {
                    indexIterate += 1;
                }
            }
            path.set(startRoom, closestNode);
            startRoom = closestNode;
            edges += 1;
            indexIterate = 0;
            minDistance = 1000;

        }
        return path;

    }
    /*
    public static void roomPaths(ArrayList<Point> rooms){
        double minDistance = 1000;
        int closestNode;
        ArrayList<Integer> path = new ArrayList<>(rooms.size());
        for(int i = 0; i < rooms.size(); i ++){
            Point starter = new Point(rooms.get(i).x, rooms.get(i).y);
            //once you find the closest node, you then need to find the next closest node to the new node we just found
            //ie if room 1 points to room 4, then we need to find the next closest room to 4
            for(int j = i+1; j < rooms.size(); j++){
                Point checker = new Point(rooms.get(j).x,rooms.get(j).y);
                double distance = distanceFormula(starter,checker);
                if(distance < minDistance){
                    minDistance = distance;
                    closestNode = j;
                }
            }
            path.add(i,closestNode);

        }
    }

     */
    public static void randomRoomGenerator(TETile[][] tiles){
        //number of rooms
        int rooms = 4;
        //width and height of room, this if from the center, so if you imagine the center of a square,
        // then (center + roomW) reaches the right edge of the room and (center - roomW) reaches the left edge

        int roomW = 4;
        int roomH = 4;
        //range for each room to be created (for the x axis)
        int range = 10;
        //random x,y coordinates for the center
        int randoX;
        int randoY;
        //store a list of points corresponding to room centers
        ArrayList<Point> centerRoomCoordinates = new ArrayList<Point>(rooms);
        int index = 0;
        //create 4 random centers for our rooms
        for(int i = 0; i < rooms; i ++){
            randoX = (int) (Math.random()*(10) + range);
            randoY = (int) (Math.random()*7 + 17);
            tiles[randoX][randoY] = Tileset.MIDROOM;
            Point point = new Point(randoX,randoY,roomW, roomH);
            centerRoomCoordinates.add(index, point);
            index += 1;
            range += 15;
        }
        ArrayList<Integer> paths = roomsPath2(centerRoomCoordinates);
        tiles = hallwayGenerator(paths, centerRoomCoordinates, tiles);
        //using center room coordinates, create walls around room
        for(int i = 0; i < centerRoomCoordinates.size(); i++ ){
            int centerX = centerRoomCoordinates.get(i).x;
            int centerY = centerRoomCoordinates.get(i).y;
            int roomHeight = centerRoomCoordinates.get(i).pointH;
            int roomWidth = centerRoomCoordinates.get(i).pointW;

            Point topLeftCorner = new Point(centerX - roomWidth, centerY + roomHeight, roomWidth, roomHeight);
            Point bottomRightCorner = new Point(centerX + roomWidth, centerY - roomHeight, roomWidth, roomHeight);
            //Make top border using topLeftCorner and moves Left
            for(int x = topLeftCorner.x; x < topLeftCorner.x + 2*roomWidth + 1; x ++){
                tiles[x][topLeftCorner.y] = Tileset.WALL;
            }
            //Make right border using bottomRightCorner and move up
            for(int y = bottomRightCorner.y; y < bottomRightCorner.y + 2*roomHeight + 1; y ++){
                tiles[bottomRightCorner.x][y] = Tileset.WALL;
            }
            //Make bottom border using bottomRightCorner and moving left
            for(int x = bottomRightCorner.x; x > bottomRightCorner.x - 2*roomWidth - 1; x--){
                tiles[x][bottomRightCorner.y] = Tileset.WALL;
            }
            //Make left border using topLeftCorner and moving down
            for(int y = topLeftCorner.y; y > topLeftCorner.y - 2*roomHeight - 1; y--){
                tiles[topLeftCorner.x][y] = Tileset.WALL;
            }
            //fill in room
            for(int w = centerX - roomWidth + 1; w < centerX + roomWidth; w ++){
                for(int z = centerY - roomHeight + 1; z < centerY + roomHeight; z++){
                    tiles[w][z] = Tileset.WATER;
                }
            }
        }
        //create outer border
        for(int i = 0; i < HEIGHT; i ++){
            tiles[0][i] = Tileset.MOUNTAIN;
            tiles[WIDTH-1][i] = Tileset.MOUNTAIN;
        }
        for(int j = 0; j < WIDTH; j++){
            tiles[j][0] = Tileset.MOUNTAIN;
            tiles[j][HEIGHT-1] = Tileset.MOUNTAIN;
        }
        //fills in rest of map with flowers
        for(int i = 0; i < WIDTH; i ++){
            for(int j = 0; j< HEIGHT; j++){
                if(tiles[i][j] == null){
                    tiles[i][j] = Tileset.FLOWER;
                }
            }
        }
    }
    public static void fillWithRandomTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = randomTile();
            }
        }
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = rand.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.NOTHING;
            default: return Tileset.NOTHING;
        }
    }

    public static double distanceFormula(Point a, Point b){
        double d = Math.pow(Math.pow(a.x-b.x,2) + Math.pow(a.y-b.y,2),.5);
        return d;
    }

    private static class Point{
        private int x;
        private int y;
        private int pointH;
        private int pointW;
        Point(int x, int y, int w, int h){
            this.x = x;
            this.y =y;
            pointH = h;
            pointW = w;
        }
    }
}
