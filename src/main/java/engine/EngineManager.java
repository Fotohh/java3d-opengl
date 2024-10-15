package engine;

import engine.utils.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import src.Main;

public class EngineManager {

    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;
    public static int fps;
    public static float deltaTime = 1.0f / FRAMERATE;

    private boolean isRunning;
    private ILogic gameLogic;

    private Window window;
    private GLFWErrorCallback errorCallback;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Main.getWindow();
        gameLogic = Main.getTestGame();
        window.init();
        gameLogic.init();
    }

    public void start() throws Exception {
        init();
        if (isRunning) return;

        run();
    }

    public void run() {
        isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            while (unprocessedTime > deltaTime) {
                render = true;
                unprocessedTime -= deltaTime;

                if (window.shouldClose()) stop();

                input();

                if (frameCounter >= NANOSECOND) {
                    fps = frames;
                    window.setTitle(Constants.TITLE + " | FPS: " + frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render) {
                update();
                render();
                frames++;
            }
        }
        cleanup();
    }

    private void stop() {
        if (!isRunning) return;

        isRunning = false;
    }

    private void input() {
        gameLogic.input();
    }

    private void render() {
        gameLogic.render();
        window.update();
    }

    private void update() {
        gameLogic.update();
    }

    private void cleanup() {
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
