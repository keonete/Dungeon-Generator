package byow.Core;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Engine {
    final File gameDir = new File("gameSaves.txt");
    final String filename = "currentGame.txt";
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 45;
    public Random rand;
    public long seed;
    public String moveKeys;
    public World.Avatar avatar;
    public char loadStart;
    public boolean lightToggle = true;


    public static void main(String[] args){
        Engine engine = new Engine();
        TETile[][] gameState = engine.interactWithInputString(args[0]);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(gameState);
        //createMenu();
        /*
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            Engine engine = new Engine();
            engine.interactWithInputString(args[1]);
            //System.out.println(engine.toString());
        }
         */
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {


        createMenu();
        if(loadStart == 'N'){
            solicitSeed();
        }
        //createName();
        World randomWorld = new World();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        if(loadStart == 'L'){
            loadGame();
            World.Game game = randomWorld.World(seed,moveKeys);
            ter.renderFrame(game.getGamestate());
        }

        //take controls
        long userMoveCount = 0;
        long userMoves = 10000000000000000L;
        boolean lineOfSight = false;
        while(userMoveCount < userMoves){
            if(StdDraw.hasNextKeyTyped()){
                String s = Character.toString(StdDraw.nextKeyTyped());
                char checker = s.charAt(0);
                if(checker == 'o'){
                    lightToggle = !lightToggle;
                }
                if(checker == ':'){
                    while(true){
                        if(StdDraw.hasNextKeyTyped()) {
                            char realCheck = StdDraw.nextKeyTyped();
                            if (realCheck == 'q') {
                                saveGame();
                                System.exit(0);
                            }
                        }
                    }
                }
                moveKeys += s;
                userMoveCount += 1;
                World.Game game  = randomWorld.World(seed,moveKeys);
                ter.renderFrame(game,lightToggle);
                hud(game.getGamestate());
            }

        }
    }

    private void hud(TETile[][] gamestate) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.textLeft(0, HEIGHT - 1, "");
        StdDraw.filledRectangle(0, HEIGHT, 5, 1.5);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.textRight(WIDTH, HEIGHT - 1, "");
        StdDraw.filledRectangle(WIDTH, HEIGHT-1, 8.8, .7);

        DateFormat date = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        Date time = new Date();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.textRight(WIDTH - 0.2 , HEIGHT - 1, date.format(time));


        //String ret = "";
        int posX = (int) Math.floor(StdDraw.mouseX());
        int posY = (int) Math.floor(StdDraw.mouseY());
        if (posX >= WIDTH || posY >= HEIGHT || posX < 0 || posY < 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.text(2.8, HEIGHT - 1, Tileset.MOUNTAIN.description());
       }
        else {
            TETile current = gamestate[posX][posY];
            //if (current.equals(Tileset.AVATAR)) {
            //  StdDraw.setPenColor(StdDraw.RED);
            //  StdDraw.text(2.8, HEIGHT - 1, Avatar.name);
            //else
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.text(2.8, HEIGHT - 1, current.description());
       }
        StdDraw.show();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        World randomWorld = new World();
        char [] inputC = input.toCharArray();
        int i = 1;
        if(inputC[0] == 'n'){
            while(inputC[i] != 's'){
                i++;
            }
            seed = Long.valueOf(input.substring(1,i));
            if(i == inputC.length - 1){
                World.Game game = randomWorld.World(seed,"");
                avatar = game.getAvatar();
                return game.getGamestate();
            }else {
                i += 1;
                while (i < inputC.length) {
                    if (inputC[i] == ':') {
                        if (inputC[i + 1] == 'q') {
                            World.Game game = randomWorld.World(seed, moveKeys);
                            avatar = game.getAvatar();
                            saveGame();
                            return game.getGamestate();
                        }
                    }
                    moveKeys += Character.toString(inputC[i]);
                    i++;
                }
            }
        }
        else if(inputC[0] == 'l'){
            loadGame();
            //int j = 1;
            String newMoves = "";
            for(int j = 1; j < inputC.length; j++){
                if(inputC[j] == ':'){
                    if(inputC[j+1] == 'q'){
                        moveKeys += newMoves;
                        saveGame();
                    }
                }
                newMoves += inputC[j];
            }
            moveKeys += newMoves;
            World.Game game = randomWorld.World(seed,moveKeys);
            avatar = game.getAvatar();
            return game.getGamestate();
        }else{
            return null;
        }

        //need to add case where moveKeys terminates when a :Q occurs... ie) no more char's read after the Q
        //String moveKeys = input.substring(i+1);

        World.Game game = randomWorld.World(seed,moveKeys);
        avatar = game.getAvatar();
        return game.getGamestate();

    }
    public void solicitSeed(){
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "Enter Your Seed:");
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "Press S when your done:");
        StdDraw.text(WIDTH/2, HEIGHT - 30, "After Pressing S, press a WASD key to load the game");
        int nameMover = 0;
        String seedString = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String s = Character.toString(StdDraw.nextKeyTyped());
                if (s.equals("s")) {
                    break;
                }
                ArrayList<String> numbers = new ArrayList<String>();
                Collections.addAll(numbers, "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
                if( numbers.contains(s)) {
                    seedString += s;
                    StdDraw.text(WIDTH / 2 + nameMover, HEIGHT - 40, seedString.substring(nameMover, nameMover + 1));
                    nameMover += 1;
                    StdDraw.pause(100);
                }
            }
        }
        seed = Long.parseLong(seedString);
    }

    public void createName(){
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "Name your Avatar:");
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "P.S. have some humility and don't name it after yourself");
        StdDraw.text(WIDTH / 2, HEIGHT - 30, "Press 0 When your Done");
        String name = "";
        int nameMover = 0;
        while (true) {
            if(StdDraw.hasNextKeyTyped()){
                String s = Character.toString(StdDraw.nextKeyTyped());
                if(s.equals("0")){
                    break;
                }
                name += s;
                StdDraw.text(WIDTH/2 + nameMover, HEIGHT - 40, name.substring(nameMover,nameMover+1));
                nameMover += 1;
                StdDraw.pause(100);
            }
        }

    }
    public void createMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "CS61BL: The Game");
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT - 13, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT - 16, "Quit Game (Q)");
        while (true) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
                System.exit(0);

            }
            else if ( StdDraw.isKeyPressed(KeyEvent.VK_N)){
                loadStart = 'N';
                break;
            }
            else if (StdDraw.isKeyPressed(KeyEvent.VK_L)){
                loadStart = 'L';
                break;
            }
        }
    }
    public void initializeSaveFile(){
        if(!gameDir.exists()){
            gameDir.mkdir();
            File file = new File(gameDir,filename);
        }
    }
    public void saveGame(){
        initializeSaveFile();
        try{
            FileOutputStream f = new FileOutputStream(new File(filename));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeLong(seed);
            //o.writeBytes(moveKeys);
            o.writeUTF(moveKeys);
            //o.writeObject(moveKeys);
            o.writeObject(avatar);

            o.close();
            f.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadGame(){
        try{
            FileInputStream f2 = new FileInputStream(filename);
            ObjectInputStream o2 = new ObjectInputStream(f2);

            seed = o2.readLong();
            moveKeys = o2.readUTF();
            //moveKeys = (String) o2.readObject();
            avatar = (World.Avatar) o2.readObject();
            o2.close();
            f2.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }
    }
}