package byow.TileEngine;

import java.awt.Color;
import java.util.Random;
/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    //added this
    public static final TETile MIDROOM = new TETile('*', Color.white, Color.black, "center room");
    public static final TETile LIGHT = new TETile('$', Color.YELLOW,Color.YELLOW, "light");
    //
    public static final TETile AVATAR = new TETile('▒', new Color(50, 50, 50), Color.darkGray,
            "AVATAR", "/Users/keonetebari/Desktop/cs61bl/su21-p179/proj3/byow/TileEngine/Images/avatar.png");
    public static final TETile WALL = new TETile('▒', new Color(50, 50, 50), Color.darkGray,
            "wall", "/Users/keonetebari/Desktop/cs61bl/su21-p179/proj3/byow/TileEngine/Images/wall.png");

//    public static final TETile GRASS = new TETile('▒', new Color(50, 50, 50), Color.darkGray,
//            "GRASS", "/Users/keonetebari/Desktop/cs61bl/su21-p179/proj3/byow/TileEngine/Images/forest_tiles.png");
    //public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
//    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
//            //"wall");
//    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
//            "floor");
    public static final TETile NOTHING = new TETile('▒', new Color(50, 50, 50), Color.darkGray,
        "nothing", "/Users/keonetebari/Desktop/cs61bl/su21-p179/proj3/byow/TileEngine/Images/sewer_0.png");

    //public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
}


