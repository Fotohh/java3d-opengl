package engine;


import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class Window {

    private final String title;
    private int width;
    private int height;

    public Window(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        boolean maximized = false;
        if(width == 0 || height == 0){
            maximized = true;
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
        }

        long window = GLFW.glfwCreateWindow(width, height, title, 0, 0);

        if(window == 0){
            throw new IllegalStateException("Failed to create window");
        }

        GLFW.glfwSetFramebufferSizeCallback(window, (windowHandle, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
        });

        GLFW.glfwSetKeyCallback(window, (windowHandle, key, scancode, action, mods) -> {
            if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE){
                GLFW.glfwSetWindowShouldClose(windowHandle, true);
            }
        });
    }
}
