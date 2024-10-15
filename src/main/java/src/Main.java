package src;

import engine.EngineManager;
import engine.Window;
import engine.utils.Constants;

public class Main {

    private static Window window;
    private EngineManager engineManager;

    public void main() throws Exception {
        window = new Window(Constants.TITLE, 800, 600);


        window.init();
        engineManager = new EngineManager();
        engineManager.start();
    }

    public static Window getWindow() {
        return window;
    }
}
