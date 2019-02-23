import com.sun.prism.ps.Shader;

import java.io.*;

import static org.lwjgl.opengl.GL20.*;

class ShaderProgram {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    ShaderProgram() {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Could not create program");
        }
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
