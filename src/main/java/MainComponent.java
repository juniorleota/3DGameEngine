import org.lwjgl.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

class MainComponent {
  private Window window;

  private void run() {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");
    window = new Window(1000, 1000, "3D Game Engine!");
    init();
    loop();

    window.cleanup();
  }

  private void init() {
    window.init();
  }

  private void loop() {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();
    // Set the clear color
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    GameRenderer gameRenderer = new GameRenderer();
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while ( !window.isClosed() ) {
      gameRenderer.renderGame();
      window.update();
    }
  }

  public static void main(String[] args) {
    new MainComponent().run();
  }
}