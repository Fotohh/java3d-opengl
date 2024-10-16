package src;

import core.entity.Model;
import core.entity.Texture;
import engine.ILogic;
import engine.ObjectLoader;
import engine.RenderManager;
import engine.Window;
import engine.utils.Utils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;

public class TestGame implements ILogic {

    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private Model model;
    private final Window window;

    public TestGame() {
        renderer = new RenderManager();
        window = Main.getWindow();
        loader = new ObjectLoader();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        float[] vertices = {
                -0.5f,  0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f,  0.5f, 0f,
        };

        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };

        float[] textureCoordinates = {
                0, 0,
                0, 1,
                1, 1,
                1, 0,
        };

        model = loader.loadModel(vertices, textureCoordinates, indices);
        model.setTexture(new Texture(loader.loadTexture(Utils.getResourceFile("/textures/grassblock.png"))));

    }

    @Override
    public void input() {
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update() {
        color += direction * 0.01f;
        if (color > 1) color = 1;
        if (color < 0) color = 0;
    }

    @Override
    public void render() {
        if (window.isResized()) {
            GL46.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        window.setClearColor(color, color, color, 0.0f);
        renderer.render(model);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
