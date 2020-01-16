package RayTracing;

public class Plane extends Surface {

    private Vector normalVector;
    private double offset;

    public Plane(Vector normalVector, double offset, int materialIndex) {
        this.normalVector = normalVector;
        this.offset = offset;
        this.materialIndex = materialIndex;
    }

    @Override
    public Intersection findIntersectionWithRay(Ray ray) {

        Intersection intersection;
        double dotProductBetweenRayDirectionAndNormal = ray.getV().dotProduct(normalVector);
        if (dotProductBetweenRayDirectionAndNormal == 0) {
            return null;
        }

        double t = (-ray.getCenterposition().dotProduct(normalVector) + offset) / dotProductBetweenRayDirectionAndNormal;

        if (t < 0) {
            return null;
        }

        Vector intersectionPoint = ray.getCenterposition().add(ray.getV().multiplyByScalar(t));

        intersection = new Intersection(intersectionPoint, t, normalVector, this);
        return intersection;
    }
}
