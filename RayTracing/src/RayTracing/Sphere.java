package RayTracing;

public class Sphere {

    private Vector centerPosition;
    private double radius;
    private int materialIndex;

    public Sphere(Vector centerPosition, double radius, int materialIndex) {
        this.centerPosition = centerPosition;
        this.radius = radius;
        this.materialIndex = materialIndex;
    }
}
