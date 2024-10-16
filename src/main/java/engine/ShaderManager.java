package engine;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class ShaderManager {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, Integer> uniforms;

    public ShaderManager() throws Exception {
        programId = GL46.glCreateProgram();
        if (programId == 0) throw new Exception("Could not create Shader");

        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = GL46.glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) throw new Exception("Could not find uniform: " + uniformName);
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniforms(String uniformName, Matrix4f value) {
        try (var stack = MemoryStack.stackPush()) {
            GL46.glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniforms(String uniformName, int value){
        GL46.glUniform1i(uniforms.get(uniformName), value);
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL46.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL46.GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int type) throws Exception {
        int shaderId = GL46.glCreateShader(type);
        if (shaderId == 0) throw new Exception("Error creating shader. Type: " + type);

        GL46.glShaderSource(shaderId, shaderCode);
        GL46.glCompileShader(shaderId);

        if (GL46.glGetShaderi(shaderId, GL46.GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + GL46.glGetShaderInfoLog(shaderId, 1024));
        }

        GL46.glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        GL46.glLinkProgram(programId);
        if (GL46.glGetProgrami(programId, GL46.GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + GL46.glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            GL46.glDetachShader(programId, vertexShaderId);
        }

        if (fragmentShaderId != 0) {
            GL46.glDetachShader(programId, fragmentShaderId);
        }

        GL46.glValidateProgram(programId);
        if (GL46.glGetProgrami(programId, GL46.GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + GL46.glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        GL46.glUseProgram(programId);
    }

    public void unbind() {
        GL46.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            GL46.glDeleteProgram(programId);
        }
    }

    public void cleanupShaders() {
        unbind();
        if (vertexShaderId != 0) {
            GL46.glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            GL46.glDetachShader(programId, fragmentShaderId);
        }
        if (vertexShaderId != 0) {
            GL46.glDeleteShader(vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            GL46.glDeleteShader(fragmentShaderId);
        }
    }

    public void setUniform(String name, int value) {
        int location = GL46.glGetUniformLocation(programId, name);
        if (location != -1) {
            GL46.glUniform1i(location, value);
        }
    }

    public void setUniform(String name, float value) {
        int location = GL46.glGetUniformLocation(programId, name);
        if (location != -1) {
            GL46.glUniform1f(location, value);
        }
    }

    public void setUniform(String name, float x, float y, float z) {
        int location = GL46.glGetUniformLocation(programId, name);
        if (location != -1) {
            GL46.glUniform3f(location, x, y, z);
        }
    }

    public void setUniform(String name, float x, float y, float z, float w) {
        int location = GL46.glGetUniformLocation(programId, name);
        if (location != -1) {
            GL46.glUniform4f(location, x, y, z, w);
        }
    }


}
