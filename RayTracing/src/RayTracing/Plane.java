package RayTracing;

public class Plane {

    private Vector normalVector;
    private double offset;
    private int materialIndex;

    public Plane(Vector normalVector, double offset, int materialIndex) {
        this.normalVector = normalVector;
        this.offset = offset;
        this.materialIndex = materialIndex;
    }
}
