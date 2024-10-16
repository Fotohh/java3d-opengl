package core.entity;

public class Model {

    private final int id;
    private final int vertextCount;
    private Texture texture;

    public Model(int id, int vertextCount) {
        this.id = id;
        this.vertextCount = vertextCount;
    }

    public Model(int id, int vertextCount, Texture texture) {
        this.id = id;
        this.vertextCount = vertextCount;
        this.texture = texture;
    }

    public Model(Model model, Texture texture) {
        this.id = model.getId();
        this.vertextCount = model.getVertextCount();
        this.texture = texture;
    }

    public int getId() {
        return id;
    }

    public int getVertextCount() {
        return vertextCount;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
