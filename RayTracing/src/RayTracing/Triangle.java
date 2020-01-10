package RayTracing;

public class Triangle {

    private Vector vertex1Position;
    private Vector vertex2Position;
    private Vector vertex3Position;
    private int materialIndex;

    public Triangle(Vector vertex1Position, Vector vertex2Position, Vector vertex3Position, int materialIndex) {
        this.vertex1Position = vertex1Position;
        this.vertex2Position = vertex2Position;
        this.vertex3Position = vertex3Position;
        this.materialIndex = materialIndex;
    }

    public Vector getVertex1Position() {
        return vertex1Position;
    }

    public Vector getVertex2Position() {
        return vertex2Position;
    }

    public Vector getVertex3Position() {
        return vertex3Position;
    }

    public int getMaterialIndex() {
        return materialIndex;
    }
}
