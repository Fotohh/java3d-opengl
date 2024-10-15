package engine;


import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.ovr.OVRMatrix4f;

import javax.imageio.stream.IIOByteBuffer;
import java.nio.ByteBuffer;

public class Window {

    private final String title;
    private int width;
    private int height;
    public static boolean VSYNC = true;
    private boolean resized;
    private long window;
    private final Matrix4f projectionMatrix;
    private float fov = 60.0f, zNear = 0.01f, zFar = 1000.0f;

    public boolean isResized() {
        return resized;
    }

    public Window(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
        this.projectionMatrix = new Matrix4f();
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
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, resized ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

        boolean maximized = false;
        if(width == 0 || height == 0){
            maximized = true;
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
        }

        window = GLFW.glfwCreateWindow(width, height, title, 0, 0);

        if(window == 0){
            throw new IllegalStateException("Failed to create window");
        }

        GLFW.glfwSetFramebufferSizeCallback(window, (windowHandle, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
        });

        GLFW.glfwSetKeyCallback(window, (windowHandle, key, scancode, action, mods) -> {
            if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE){
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        if(maximized){
            GLFW.glfwMaximizeWindow(window);
        }else{
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        GLFW.glfwMakeContextCurrent(window);

        if(VSYNC){
            GLFW.glfwSwapInterval(1);
        }

        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL46.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glEnable(GL46.GL_STENCIL_TEST);
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_BACK);
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public void update(){
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void cleanup(){
        GLFW.glfwDestroyWindow(window);
    }

    public void setClearColor(float r, float g, float b, float a){
        GL46.glClearColor(r, g, b, a);
    }

    public boolean isKeyPressed(int keyCode){
        return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
    }

    public boolean shouldClose(){
        return GLFW.glfwWindowShouldClose(window);
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public String getTitle() {
        return title;
    }

    public Matrix4f updateProjectionMatrix(){
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(fov, aspectRatio, zNear, zFar);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix4f){
        float aspectRatio = (float) width / height;
        return matrix4f.setPerspective(fov, aspectRatio, zNear, zFar);
    }
}
