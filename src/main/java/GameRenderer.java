import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

class GameRenderer {
    private final ShaderProgram shaderProgram;


    private Mesh mesh;

    GameRenderer() {
        // todo load this from a obj file so that the information stored is only the filename rather than the vertices data
        mesh = new Mesh(SampleMeshData.positions, SampleMeshData.indices);
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader("./shaders/vertex.vs");
        shaderProgram.createFragmentShader("./shaders/fragment.fs");
        shaderProgram.link();
    }

    void renderGame() {

        shaderProgram.bind();

        // bind Vertex Array Object
        glBindVertexArray(mesh.getVaoId());
        glEnableVertexAttribArray(0);

        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
        if (mesh != null) {
            mesh.cleanup();
        }
    }
}
