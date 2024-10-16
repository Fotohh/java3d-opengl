package engine;

import core.entity.Model;
import engine.utils.Utils;
import org.lwjgl.opengl.GL46;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();

    public Model loadModel(float[] vertices) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, 3, vertices);
        unbind();
        return new Model(vaoID, vertices.length / 3);
    }

    private int createVAO() {
        int vao = GL46.glGenVertexArrays();
        vaos.add(vao);
        GL46.glBindVertexArray(vao);
        return vao;
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
    }

}
