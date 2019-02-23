import com.sun.prism.ps.Shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GameRenderer {
    private final ShaderProgram shaderProgram;
    private float[] cubeVertices = new float[]{
            0.0f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    private Mesh cube;

    GameRenderer() {
        // todo load this from a obj file so that the information stored is only the filename rather than the vertices data
        cube = new Mesh(cubeVertices);
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader("./shaders/vertex.vs");
        shaderProgram.createFragmentShader("./shaders/fragment.fs");
        shaderProgram.link();
    }

    void renderGame() {
        shaderProgram.bind();

        glBindVertexArray(cube.getVaoId());
        glEnableVertexAttribArray(0);

        glColor4f(1f, 1f, 1f, 1f);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

    }

    private void drawCube() {
        glBegin(GL_TRIANGLES);
        glEnd();
    }

    private void drawTriangle() {
        glBegin(GL_TRIANGLES);
        glEnd();
    }
}
