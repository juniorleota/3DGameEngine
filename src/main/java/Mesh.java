import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

class Mesh {
    private final FloatBuffer floatBuffer;

    private final int vaoId;

    Mesh(float[] vertices) {
        floatBuffer = MemoryUtil.memAllocFloat(vertices.length);
        floatBuffer.put(vertices).flip();
        vaoId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vaoId);
        glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }

    public int getVaoId() {
        return vaoId;
    }

    public void cleanup() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        if (floatBuffer != null) {
            MemoryUtil.memFree(floatBuffer);
        }
    }
}
