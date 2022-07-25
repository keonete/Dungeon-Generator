package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
/** References:
 * Save and Load features
    * https://www.tutorialspoint.com/java/java_serialization.htm
    * https://mkyong.com/java/how-to-read-and-write-java-object-to-a-file/
 * Rectangle collision detection
    * https://stackoverflow.com/questions/306316/determine-if-two-rectangles-overlap-each-other
 */

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            Engine engine = new Engine();
            engine.interactWithInputString(args[1]);
            System.out.println(engine.toString());
        // DO NOT CHANGE THESE LINES YET ;)
        } else if (args.length == 2 && args[0].equals("-p")) { System.out.println("Coming soon."); }
        // DO NOT CHANGE THESE LINES YET ;)
        else {
            Engine engine = new Engine();
            engine.interactWithKeyboard();
        }
    }
}
