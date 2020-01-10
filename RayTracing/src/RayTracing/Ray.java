package RayTracing;

public class Ray {

    private Vector centerposition;
    private Vector v;

    public Ray(Vector p0, Vector v) {
        this.centerposition = p0;
        this.v = v;
    }

    public Vector getPointOnRay(double progress) {
        return centerposition.add(v.multiplyByScalar(progress));
    }

    public Vector getCenterposition() {
        return centerposition;
    }

    public Vector getV() {
        return v;
    }

    public void setV(Vector v) {
        this.v = v.normalize();
    }
}
