package engine;

import core.entity.Model;
import engine.utils.Utils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public Model loadModel(float[] vertices, float[] textureCoords, int[] indices) {
        int vaoID = createVAO();
        storeIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 2, textureCoords);
        unbind();
        return new Model(vaoID, indices.length);
    }

    public int loadTexture(File file) {
        return loadTexture(file.getAbsolutePath());
    }

    public int loadTexture(String fileName) {
        int width, height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(fileName, w, h, c, 4);
            if (buffer == null) {
                throw new RuntimeException("Failed to load a texture file! " + fileName + " " + STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();
        }

        int id = GL46.glGenTextures();
        textures.add(id);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, id);
        GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buffer);
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }

    private int createVAO() {
        int vao = GL46.glGenVertexArrays();
        vaos.add(vao);
        GL46.glBindVertexArray(vao);
        return vao;
    }

    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL46.glGenBuffers();
        vbos.add(vbo);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, buffer, GL46.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attributeNumber, int vertextCount, float[] data) {
        int vbo = GL46.glGenBuffers();
        vbos.add(vbo);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, buffer, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(attributeNumber, vertextCount, GL46.GL_FLOAT, false, 0, 0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
    }

    private void unbind() {
        GL46.glBindVertexArray(0);
    }

    public void cleanup() {
        for (int vao : vaos) {
            GL46.glDeleteVertexArrays(vao);
        }

        for (int vbo : vbos) {
            GL46.glDeleteBuffers(vbo);
        }

        for (int texture : textures) {
            GL46.glDeleteTextures(texture);
        }
    }

}
