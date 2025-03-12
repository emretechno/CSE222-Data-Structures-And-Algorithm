package Main;

import com.example.system.HWSystem;

public class Main {
    private final HWSystem sys = new HWSystem();

    public static void main(String[] args) {
        Main main = new Main(); // I created an instance of the Main class
        main.sys.loadConfig("config.txt"); //loadconfig method to read the configuration file
        //main.sys.runInteractive();
        //runFromFile method to read the commands file
        main.sys.runFromFile("commands.txt");
    }
}