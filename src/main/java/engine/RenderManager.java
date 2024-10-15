package engine;

import core.entity.Model;
import org.lwjgl.opengl.GL46;
import src.Main;

public class RenderManager {

    private final Window window;

    public RenderManager() {
        window = Main.getWindow();
    }

    public void init() {

    }

    public void render(Model model) {
        clear();
        GL46.glBindVertexArray(model.getId());
        GL46.glEnableVertexAttribArray(0);
        GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, model.getVertextCount());
        GL46.glDisableVertexAttribArray(0);
        GL46.glBindVertexArray(0);

    }

    public void clear() {
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {

    }

}
