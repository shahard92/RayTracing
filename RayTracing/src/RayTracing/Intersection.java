package RayTracing;

public class Intersection {

    private Vector intersectionPoint;
    private double t;
    private Vector normal;
    private Surface surface;

    public Intersection() {
    }

    public Intersection(Vector intersectionPoint, double t, Vector normal, Surface surface) {
        this.intersectionPoint = intersectionPoint;
        this.t = t;
        this.normal = normal.normalize();
        this.surface = surface;
    }

    public Double getT() {
        return t;
    }

    public Vector getIntersectionPoint() {
        return intersectionPoint;
    }

    public Surface getSurface() {
        return surface;
    }

    public Vector getNormal() {
        return normal;
    }
}
