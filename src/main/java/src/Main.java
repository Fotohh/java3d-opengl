package src;

import engine.EngineManager;
import engine.Window;
import engine.utils.Constants;

public class Main {

    private static Window window;
    private static TestGame testGame;

    public void main() throws Exception {
        window = new Window(Constants.TITLE, 800, 600);
        testGame = new TestGame();
        EngineManager engineManager = new EngineManager();
        engineManager.start();
    }

    public static Window getWindow() {
        return window;
    }

    public static TestGame getTestGame() {
        return testGame;
    }
}
