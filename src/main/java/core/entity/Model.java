package core.entity;

public class Model {

    private final int id;
    private final int vertextCount;

    public Model(int id, int vertextCount){
        this.id = id;
        this.vertextCount = vertextCount;
    }

    public int getId() {
        return id;
    }

    public int getVertextCount() {
        return vertextCount;
    }
}
