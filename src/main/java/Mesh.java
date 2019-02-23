import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

class Mesh {
    private final int vboId;
    private final int vertexCount;
    private final int vaoId;
    private final int idxVboId;

    Mesh(final float[] positions, final int[] indicies) {
        FloatBuffer posBuffer = null;
        IntBuffer indiciesBuffer = null;
        try {
            vertexCount = positions.length;

            //create the vertex array object
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // create the vertex buffer object
            vboId = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            // describe the data that you have just loaded to gpu
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // create indicies for vertex buffer object
            idxVboId = glGenBuffers();
            indiciesBuffer = MemoryUtil.memAllocInt(indicies.length);
            indiciesBuffer.put(indicies).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiciesBuffer, GL_STATIC_DRAW);

            // data is sent to gpu hence we can cleanup
            // what this means is that if you try to load in anymore data it will go to ptr 0
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            // acccrding to documentation you bind to 0 to break the existing array vertex binding (i.e. GL_ARRAY_BUFFER)
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (indiciesBuffer != null) {
                MemoryUtil.memFree(indiciesBuffer);
            }
        }
    }

    public int getVboId() {
        return vboId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }

    void cleanup() {
        // this seems to disable something
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_VERTEX_ARRAY, 0);
        glDeleteBuffers(vboId);
        glDeleteBuffers(idxVboId);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);

    }
}
