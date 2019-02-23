import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

class ShaderProgram {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;

    ShaderProgram() {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Could not create program");
        }
        uniforms = new HashMap<>();
    }

    void createVertexShader(final String fileName) {
        vertexShaderId = createShader(loadShader(fileName), GL_VERTEX_SHADER);
    }

    void createFragmentShader(final String fileName) {
        fragmentShaderId = createShader(loadShader(fileName), GL_FRAGMENT_SHADER);
    }

    private int createShader(final String shaderCode, final int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new RuntimeException("Could not create shader");
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Failed to compile shader: " + shaderId);
        }


        glAttachShader(programId, shaderId);

        return shaderId;
    }

    void createUniform(String uniformName) {
        // this get also seems to do write operations
        int uniformLocation = glGetUniformLocation(programId, uniformName);

        if (uniformLocation < 0) {
            throw new RuntimeException("Could not find information about uniform: " + uniformName);
        }

        uniforms.put(uniformName, uniformLocation);
    }

    void setUniform(String uniformName, Matrix4f mat) {
        // size of the buffer is small, hence we can use memory stack so that it is automanaged
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer floatBuffer = stack.mallocFloat(16);
            // weird but this is actually setting the floatbuffer in a get method
            mat.get(floatBuffer);
            glUniformMatrix4fv(uniforms.get(uniformName), false, floatBuffer);
        }
    }

    void link() {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    void bind() {
        glUseProgram(programId);
    }

    void unbind() {
        glUseProgram(0);
    }

    void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    private static String loadShader(String path) {
        StringBuilder builder = new StringBuilder();

        try (InputStream in = Class.forName(ShaderProgram.class.getName()).getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load a shader file!"
                    + System.lineSeparator() + ex.getMessage());
        }
        return builder.toString();
    }
}
