package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


import byow.Core.Engine;
import com.sun.jdi.VMOutOfMemoryException;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
public class World {

    private static Random rand;
    private static long SEED;
    private static final int WIDTH = Engine.WIDTH;
    private static final int HEIGHT = Engine.HEIGHT;
    private static int numberOfRooms;
    public static ArrayList<Room> roomCoordinates;

    public Game World(long seed, String movements){
        //SEED = 2456848;
        SEED = seed;
        rand = new Random(SEED);
        TETile[][] randomTiles = new TETile[WIDTH][HEIGHT];
        initializeWorld(randomTiles);
        int numberOfRooms = rand.nextInt(15) + 20;
        //numberOfRooms = 5;
        roomCoordinates = new ArrayList<>(numberOfRooms);
        for(int i = 0; i < numberOfRooms; i++){
            randomRoomGenerator(randomTiles);
        }
        randomTiles = fillRooms(randomTiles, roomCoordinates);
        randomTiles = fillInHallwayGaps(randomTiles);
        Room startingRoom = roomCoordinates.get(0);
        Room endRoom = roomCoordinates.get(roomCoordinates.size()-1);
//        for(int i = 1; i < numberOfRooms; i++){
//            Enemy enemy = new Enemy(roomCoordinates.get(i).center.x, roomCoordinates.get(i).center.y);
//        }
        Treasure FinalTreasure = new Treasure(endRoom.center.x, endRoom.center.y);
        Avatar a = new Avatar(startingRoom.center.x, startingRoom.center.y, movements);

        //Pair<TETile [][], long> p = avatarMove(a, randomTiles, FinalTreasure);
        //if(p.getValue()
        Enemy enemy = new Enemy(roomCoordinates.get(5).center.x, roomCoordinates.get(5).center.y);
        randomTiles = avatarMove(a,randomTiles, FinalTreasure, enemy);
        Game currentGame = new Game(randomTiles, a);
        return currentGame;
    }
    public static TETile[][] enemyMove(Enemy e, TETile[][] gamestate, Treasure treasure){
        while(gamestate[e.xCoor][e.yCoor+1].description() != "wall"){
            e.yCoor += 1;
            //gamestate[e.xCoor][e.yCoor] = Tileset.AVATAR;
        }
        while(gamestate[e.xCoor][e.yCoor-1].description() != "wall"){
            e.yCoor -= 1;
            //gamestate[e.xCoor][e.yCoor] = Tileset.AVATAR;
        }
        return gamestate;

    }

    public static TETile[][] avatarMove(Avatar a, TETile[][] gamestate, Treasure treasure, Enemy e){
        char[] moves = a.movements.toCharArray();
        //gamestate[e.xCoor][e.yCoor] = Tileset.AVATAR;
        for(int i = 0; i < moves.length; i ++){
            char move = Character.toLowerCase(moves[i]);
            if(move == 'w'){
                a.yCoor += 1;
                if(gamestate[a.xCoor][a.yCoor].description() == "wall"){
                    a.yCoor -= 1;
                }
            }
            if(move == 's'){
                a.yCoor -= 1;
                if(gamestate[a.xCoor][a.yCoor].description() == "wall"){
                    a.yCoor += 1;
                }
            }
            if(move == 'a'){
                a.xCoor -= 1;
                if(gamestate[a.xCoor][a.yCoor].description() == "wall"){
                    a.xCoor += 1;
                }
            }
            if(move == 'd'){
                a.xCoor += 1;
                if(gamestate[a.xCoor][a.yCoor].description() == "wall"){
                    a.xCoor -= 1;
                }
            }
        }
        if(gamestate[e.xCoor][e.yCoor+1].description() != "wall"){
            gamestate[e.xCoor][e.yCoor] = Tileset.GRASS;
            e.yCoor += 1;
            //gamestate[e.xCoor][e.yCoor] = Tileset.AVATAR;
        }
        else{
            gamestate[e.xCoor][e.yCoor] = Tileset.GRASS;
            e.yCoor -= 1;
            //gamestate[e.xCoor][e.yCoor] = Tileset.AVATAR;
        }
        gamestate[a.xCoor][a.yCoor] = Tileset.AVATAR;
        gamestate[treasure.xCoor][treasure.yCoor] = Tileset.LOCKED_DOOR;
        if(a.xCoor == treasure.xCoor && a.yCoor == treasure.yCoor){
              //String[] input = {"n666s"};
              String input = "n666s";
              Long seed = 666L;
              String moveKeys = "";
              World randomWorld = new World();
              World.Game game = randomWorld.World(seed,moveKeys);
              //avatar = game.getAvatar();
              return game.getGamestate();


//              Engine engine = new Engine();
//              return engine.interactWithInputString(input);


//            World newWorld = new World();
//            TERenderer newTer = new TERenderer();
//            newTer.initialize(WIDTH, HEIGHT);
//            SEED = 666L;
//            //Long newSeed = 666L;
//            String moveKeys = "";
//            World.Game newGame = newWorld.World(SEED, moveKeys);
//            newTer.renderFrame(newGame.getGamestate());
//            gamestate = newGame.getGamestate();
//            //return new Pair<TETile [][], Long>(gamestate, newSeed);
//            return gamestate;
//            World randomWorld = new World();
//            TERenderer ter = new TERenderer();
//            ter.initialize(WIDTH, HEIGHT);
//            Long seed = 666L;
//            String moveKeys = "";
//            World.Game game = randomWorld.World(seed,moveKeys);
//            ter.renderFrame(game.getGamestate());
//            gamestate = game.getGamestate();
//            return gamestate;
//            Engine engine = new Engine();
//            String [] newGame = {"n12s"};
//            engine.main(newGame);

        }
        return gamestate;
//        else{
//            //return new Pair<TETile[][], Long>(gamestate, SEED);
//            return gamestate;
//        }
    }

    public static TETile[][] fillInHallwayGaps(TETile[][] gamestate){
        for(int i = 0; i < WIDTH; i ++){
            for(int j = 0; j < HEIGHT; j++){
                if(gamestate[i][j].description() == "nothing"){
                    int up = j + 1;
                    int down = j - 1;
                    int right = i + 1;
                    int left = i - 1;
                    if(gamestate[i][up].description() == "grass" || gamestate[i][down].description() == "grass"
                            || gamestate[left][j].description() == "grass" || gamestate[right][j].description() == "grass"){
                        gamestate[i][j] = Tileset.WALL;
                    }
                    if(gamestate[i][j].description() == "grass" && gamestate[i][up].description() == "wall" && gamestate[i][down].description() == "wall"
                            && gamestate[left][j].description() == "wall" && gamestate[right][j].description() == "wall"){
                        gamestate[i][j] = Tileset.WALL;
                    }
                }
                else if(gamestate[i][j].description() == "grass"){
                    int up = j + 1;
                    int down = j - 1;
                    int right = i + 1;
                    int left = i - 1;
                    if(gamestate[i][up].description() == "wall" && gamestate[i][down].description() == "wall"
                            && gamestate[left][j].description() == "wall" && gamestate[right][j].description() == "wall"){
                        gamestate[i][j] = Tileset.WALL;
                    }
                }
            }
        }
        return gamestate;
    }


    public static TETile[][] verticalHallway(Room r, Room l, TETile[][] gamestate, int wDiff){
        int edgeStart = rand.nextInt(l.bottomRight.x - r.topLeft.x) + r.topLeft.x;
        int startY = 0;
        int doorY = 0;
        boolean rightIsAboveLeft;
        if(r.center.y >= l.center.y){
            rightIsAboveLeft = true;
        } else {
            rightIsAboveLeft = false;
        }
        if(rightIsAboveLeft){
            startY = r.center.y - r.roomHeight + 1;
            for(int i = startY; i >= l.center.y + l.roomHeight; i --){
                Random rn = new Random();
                int answer = rn.nextInt(4) + 1;
                if(answer == 1){
                    gamestate[edgeStart][i] = Tileset.GRASS;
                }
                gamestate[edgeStart][i] = Tileset.GRASS;
                gamestate[edgeStart - 1][i] = Tileset.WALL;
                gamestate[edgeStart + 1][i] = Tileset.WALL;
                doorY = i;
            }
        } else {
            startY = r.center.y + r.roomHeight + 1;
            for(int i = startY; i <= l.center.y - l.roomHeight; i ++){
                gamestate[edgeStart][i] = Tileset.GRASS;
                gamestate[edgeStart-1][i] = Tileset.WALL;
                gamestate[edgeStart+1][i] = Tileset.WALL;
                doorY = i;
            }
        }
        //gamestate[edgeStart][startY] = Tileset.LOCKED_DOOR;
        //gamestate[edgeStart][doorY] = Tileset.LOCKED_DOOR;
        return gamestate;
    }

    public static TETile[][] trickyHallway(Room a, Room b, TETile[][] gamestate){
        //got to determine what the ordinance is, is a above and to the right of b ect.
        boolean aRightOfB;
        boolean aAboveB;
        int bX;
        int bY;
        int aX;
        int aY;

        if(a.center.x >= b.center.x){
            aRightOfB = true;
        } else {
            aRightOfB = false;
        }

        if(a.center.y >= b.center.y){
            aAboveB = true;
        } else {
            aAboveB = false;
        }
        if(aRightOfB && aAboveB){
            bX = b.bottomRight.x;
            bY = rand.nextInt(2*b.roomHeight - 2) + b.bottomRight.y + 1;
            aX = rand.nextInt(2*a.roomWidth - 2) + a.topLeft.x + 1;
            aY = a.bottomRight.y;
            for(int i = bX; i <= aX; i ++){
                gamestate[i][bY] = Tileset.GRASS;
                if(gamestate[i][bY+1].description() == "nothing") {
                    gamestate[i][bY + 1] = Tileset.WALL;
                }
                if(gamestate[i][bY-1].description() == "nothing"){
                    gamestate[i][bY + 1] = Tileset.WALL;
                }
            }
            for(int i = bY; i <= aY; i++){
                gamestate[aX][i] = Tileset.GRASS;
                if(gamestate[aX+1][i].description() == "nothing") {
                    gamestate[aX+1][i] = Tileset.WALL;
                    gamestate[aX-1][i] = Tileset.WALL;
                }
                if(gamestate[aX-1][i].description() == "nothing"){
                    gamestate[aX-1][i] = Tileset.WALL;
                }
            }
        }
        else if (aRightOfB && !aAboveB){
            bX = b.bottomRight.x;
            bY = rand.nextInt(2*b.roomHeight - 2) + b.bottomRight.y + 1;
            aX = rand.nextInt(2*a.roomWidth - 2) + a.topLeft.x + 1;
            aY = a.topLeft.y;
            for(int i = bX; i <= aX; i++){
                gamestate[i][bY] = Tileset.GRASS;
                if(gamestate[i][bY+1].description() == "nothing" && gamestate[i][bY-1].description() == "nothing") {
                    gamestate[i][bY+1] = Tileset.WALL;
                }
                if(gamestate[i][bY-1].description() == "nothing"){
                    gamestate[i][bY-1] = Tileset.WALL;
                }
            }
            for(int i = bY; i >= aY; i--){
                gamestate[aX][i] = Tileset.GRASS;
                if(gamestate[aX+1][i].description() == "nothing") {
                    gamestate[aX+1][i] = Tileset.WALL;
                }
                if(gamestate[aX-1][i].description() == "nothing"){
                    gamestate[aX-1][i] = Tileset.WALL;
                }

            }
        }
        else if (!aRightOfB && aAboveB){
            aX = a.bottomRight.x;
            aY = rand.nextInt(2*a.roomHeight - 2) + a.bottomRight.y + 1;
            bX = rand.nextInt(2*b.roomWidth - 2) + b.topLeft.x + 1;
            bY = b.topLeft.y;
            for(int i = aX; i <= bX; i ++){
                gamestate[i][aY] = Tileset.GRASS;
                if(gamestate[i][aY+1].description() == "nothing" && gamestate[i][aY-1].description() == "nothing") {
                    gamestate[i][aY+1] = Tileset.WALL;
                }
                if(gamestate[i][aY-1].description() == "nothing"){
                    gamestate[i][aY-1] = Tileset.WALL;
                }
            }
            for(int i = aY; i >= bY; i--){
                gamestate[bX][i] = Tileset.GRASS;
                if(gamestate[bX+1][i].description() == "nothing" && gamestate[bX-1][i].description() == "nothing") {
                    gamestate[bX+1][i] = Tileset.WALL;
                }
                if(gamestate[bX-1][i].description() == "nothing"){
                    gamestate[bX-1][i] = Tileset.WALL;
                }
            }
        } else {
            //case where a left or b and below b
            aX = a.bottomRight.x;
            aY = rand.nextInt(2*a.roomHeight - 2) + a.bottomRight.y + 1;
            bX = rand.nextInt(2*b.roomWidth - 2) + b.topLeft.x + 1;
            bY = b.topLeft.y;
            for(int i = aX; i <= bX; i ++){
                gamestate[i][aY] = Tileset.GRASS;
                if(gamestate[i][aY+1].description() == "nothing" && gamestate[i][aY-1].description() == "nothing") {
                    gamestate[i][aY+1] = Tileset.WALL;
                }
                if(gamestate[i][aY-1].description() == "nothing"){
                    gamestate[i][aY-1] = Tileset.WALL;
                }
            }
            for(int i = aY; i >= bY; i++){
                gamestate[bX][i] = Tileset.GRASS;
                if(gamestate[bX+1][i].description() == "nothing") {
                    gamestate[bX+1][i] = Tileset.WALL;
                }
                if(gamestate[bX-1][i].description() == "nothing"){
                    gamestate[bX-1][i] = Tileset.WALL;
                }
            }
        }
        //gamestate[aX][aY] = Tileset.LOCKED_DOOR;
        //gamestate[bX][bY] = Tileset.LOCKED_DOOR;
        return gamestate;
    }


    public static TETile[][] horizantalHallway(Room T, Room B, TETile[][] gamestate, int hDiff){
        int edgeStart = rand.nextInt( T.topLeft.y - hDiff-T.bottomRight.y-1) + T.bottomRight.y + 1;
        int doorX = 0;
        int startX = 0;
        boolean bottomIsLeftToTop;
        if(T.center.x >= B.center.x){
            bottomIsLeftToTop = true;
        } else {
            bottomIsLeftToTop = false;
        }
        if(bottomIsLeftToTop){
            startX = T.center.x - T.roomWidth + 1;
            for(int i = startX; i >= B.center.x + B.roomWidth; i--){
                gamestate[i][edgeStart] = Tileset.GRASS;
                gamestate[i][edgeStart+1] = Tileset.WALL;
                gamestate[i][edgeStart-1] = Tileset.WALL;
                doorX = i;
            }
        } else {
            startX = T.center.x + T.roomWidth + 1;
            for(int i = startX; i <= B.center.x - B.roomWidth; i++){
                gamestate[i][edgeStart] = Tileset.GRASS;
                gamestate[i][edgeStart+1] = Tileset.WALL;
                gamestate[i][edgeStart-1] = Tileset.WALL;
                doorX = i;
            }
        }
        //gamestate[doorX][edgeStart] = Tileset.LOCKED_DOOR;
        //gamestate[startX][edgeStart] = Tileset.LOCKED_DOOR;
        return gamestate;
    }

    public static TETile[][] connectRooms(Room a, Room b, TETile[][] gamestate) {
        Room topRoom;
        Room bottomRoom;

        Room rightRoom;
        Room leftRoom;
        int heightDiff;
        int widthDiff;
        boolean edgeHallway = false;
        //determine which room is higher
        if (a.topLeft.y >= b.topLeft.y) {
            topRoom = a;
            bottomRoom = b;
        } else {
            topRoom = b;
            bottomRoom = a;
        }
        //determine which room is to the right
        if(a.bottomRight.x > b.bottomRight.x) {
            rightRoom = a;
            leftRoom = b;
        }else{
            rightRoom = b;
            leftRoom = a;
        }
        heightDiff = topRoom.topLeft.y - bottomRoom.topLeft.y;
        widthDiff = rightRoom.bottomRight.x - leftRoom.bottomRight.x;
        //indicates that they share y values for their edge
        if(heightDiff < topRoom.roomHeight * 2 - 1 && heightDiff != 0){
            gamestate = horizantalHallway(topRoom, bottomRoom, gamestate, heightDiff);
        }
        else if(widthDiff < 2* rightRoom.roomWidth - 1 && widthDiff != 0){
            gamestate = verticalHallway(rightRoom, leftRoom, gamestate, widthDiff);
        }
        /*
        if (topRoom.topLeft.y - bottomRoom.topLeft.y < topRoom.roomHeight * 2 - 1) {
            //heightDiff = topRoom.topLeft.y - bottomRoom.topLeft.y;
            gamestate = horizantalHallway(topRoom, bottomRoom, gamestate, heightDiff);
        }
        else if (rightRoom.bottomRight.x - leftRoom.bottomRight.x < 2*rightRoom.roomWidth - 2){
            //widthDiff = rightRoom.bottomRight.x - leftRoom.bottomRight.x;
            gamestate = verticalHallway(rightRoom, leftRoom, gamestate, widthDiff);
        }
         */
        else {
            // 2 different states, either
            gamestate = trickyHallway(a,b, gamestate);
        }
        return gamestate;
    }

    public static void initializeWorld(TETile[][] tiles){
        for(int i = 0; i < WIDTH; i++){
            for(int j = 0; j < HEIGHT; j++){
                tiles[i][j] = Tileset.NOTHING;
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
    }

    public static boolean Overlap(Room newRoom, ArrayList<Room> roomCords){
        for(int i = 0; i < roomCords.size(); i ++){
            Room testRoom = roomCords.get(i);
            if(testRoom.topLeft.x < newRoom.bottomRight.x && testRoom.bottomRight.x > newRoom.topLeft.x
                    && testRoom.topLeft.y > newRoom.bottomRight.y && testRoom.bottomRight.y < newRoom.topLeft.y){
                return true;
            }

        }
        return false;
    }

    public static void randomRoomGenerator(TETile[][] tiles) {
        boolean roomChecks = false;
        while(!roomChecks) {
            if(roomCoordinates.size() == 0) {
                int roomW = rand.nextInt(4) + 2;
                int roomH = rand.nextInt(4) + 2;
                Point roomCenter = new Point(rand.nextInt(WIDTH - 2 * roomW - 2) + roomW + 1,
                        rand.nextInt(HEIGHT - 2 * roomH - 2) + roomH + 1);
                Room newRoom = new Room(roomCenter, roomH, roomW);
                roomCoordinates.add(newRoom);
                roomChecks = true;
            } else {
                int roomW = rand.nextInt(4) + 2;
                int roomH = rand.nextInt(4) + 2;
                Point roomCenter = new Point(rand.nextInt(WIDTH - 2 * roomW - 2) + roomW + 1,
                        rand.nextInt(HEIGHT - 2 * roomH - 2) + roomH + 1);
                Room newRoom = new Room(roomCenter, roomH, roomW);
                if (!Overlap(newRoom, roomCoordinates)) {
                    connectRooms(roomCoordinates.get(roomCoordinates.size() - 1), newRoom, tiles);
                    roomCoordinates.add(newRoom);
                    roomChecks = true;
                    //connectRooms(roomCoordinates.get(roomCoordinates.size() - 2), roomCoordinates.get(roomCoordinates.size() - 1), tiles);
                }
            }
        }

    }
    public TETile[][] fillRooms(TETile[][] tiles, ArrayList<Room> roomCoordinates){
        int lightSourceInRoom = rand.nextInt(7);
        boolean lightSourcePresent = false;
        if(lightSourceInRoom == 2){
            //lightSourcePresent = true;
        }

        for(int i = 0; i < roomCoordinates.size(); i ++){
            Room currRoom = roomCoordinates.get(i);
            //change if statements to if block == nothing then make a wall
            //Make top border using topLeftCorner and moves Left
            for(int x = currRoom.topLeft.x; x < currRoom.topLeft.x + 2*currRoom.roomWidth + 1; x++){
                if(tiles[x][currRoom.topLeft.y].description() == "nothing"){
                    tiles[x][currRoom.topLeft.y] = Tileset.WALL;
                }
            }
            //Make right border using bottomRightCorner and move up
            for(int y = currRoom.bottomRight.y; y < currRoom.bottomRight.y + 2*currRoom.roomHeight + 1; y++){
                if(tiles[currRoom.bottomRight.x][y].description() == "nothing"){
                    tiles[currRoom.bottomRight.x][y] = Tileset.WALL;
                }

                /*
                if(tiles[currRoom.bottomRight.x][y].description() != "locked door") {
                    tiles[currRoom.bottomRight.x][y] = Tileset.WALL;
                }
                 */
            }
            if(lightSourcePresent){
                int wallLoco = rand.nextInt(2*currRoom.roomHeight) + currRoom.bottomRight.y;
                tiles[currRoom.bottomRight.x][wallLoco] = Tileset.LIGHT;
            }
            //Make bottom border using bottomRightCorner and moving left
            for(int x = currRoom.bottomRight.x; x > currRoom.bottomRight.x - 2*currRoom.roomWidth - 1; x--){
                if(tiles[x][currRoom.bottomRight.y].description() == "nothing"){
                    tiles[x][currRoom.bottomRight.y] = Tileset.WALL;
                }
                /*
                if(tiles[x][currRoom.bottomRight.y].description() != "locked door"){
                    tiles[x][currRoom.bottomRight.y] = Tileset.WALL;
                }
                 */
            }
            //Make left border using topLeftCorner and moving down
            for(int y = currRoom.topLeft.y; y > currRoom.topLeft.y - 2* currRoom.roomHeight - 1; y--){
                if(tiles[currRoom.topLeft.x][y].description() == "nothing"){
                    tiles[currRoom.topLeft.x][y] = Tileset.WALL;
                }
                /*
                if(tiles[currRoom.topLeft.x][y].description() != "locked door") {
                    tiles[currRoom.topLeft.x][y] = Tileset.WALL;
                }
                 */
            }
            //fill in room
            for(int w = currRoom.center.x - currRoom.roomWidth + 1; w < currRoom.center.x  + currRoom.roomWidth; w++){
                for(int z = currRoom.center.y - currRoom.roomHeight + 1; z < currRoom.center.y + currRoom.roomHeight; z++){
                    tiles[w][z] = Tileset.GRASS;
                }
            }
        }
        return tiles;
    }
    public static class Avatar implements Serializable {
        private int xCoor;
        private int yCoor;
        private String movements;

        Avatar(){
            xCoor = 0;
            yCoor = 0;
            movements = "";
        }
        Avatar(int x, int y, String movements){
            xCoor = x;
            yCoor = y;
            this.movements = movements;
        }
    }
    public static class Treasure implements Serializable{
        private int xCoor;
        private int yCoor;

        Treasure(int x, int y){
            xCoor = x;
            yCoor = y;

        }
    }
    public static class Enemy implements Serializable{
        private int xCoor;
        private int yCoor;
        //private String movements;

        Enemy(int x, int y) {
            xCoor = x;
            yCoor = y;
            //movements = mov;
        }

    }
    private static class Room{
        private Point center;
        private Point topLeft;
        private Point bottomRight;
        private int roomHeight;
        private int roomWidth;
        Room(Point center, int height, int width){
            roomWidth = width;
            roomHeight = height;

            this.center = center;

            topLeft = new Point(center.x - width, center.y + height);
            bottomRight = new Point(center.x + width, center.y - height);
        }
    }
    private static class Point{
        private int x;
        private int y;

        Point(int x, int y){
            this.x = x;
            this.y =y;

        }
    }
    public static class Game{
        private TETile[][] gamestate;
        private Avatar avatar;
        private boolean lightToggle;


        Game(TETile[][] game, Avatar a){
            gamestate = game;
            avatar = a;

        }


        public TETile[][] getGamestate(){
            return gamestate;
        }
        public Avatar getAvatar(){
            return avatar;
        }
        public int getAvatarX(){return avatar.xCoor;}
        public int getAvatarY(){return avatar.yCoor;}
        public boolean getLight(){return lightToggle;}
    }

}
