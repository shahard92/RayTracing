package RayTracing;

public class Sphere extends Surface {

    private Vector centerPosition;
    private double radius;

    public Sphere(Vector centerPosition, double radius, int materialIndex) {
        this.centerPosition = centerPosition;
        this.radius = radius;
        this.materialIndex = materialIndex;
    }

    @Override
    public Intersection findIntersectionWithRay(Ray ray) {
        Vector L = centerPosition.sub(ray.getCenterposition());
        double tca = L.dotProduct(ray.getV());

        if (tca < 0) {
            return null;
        }

        double dSquared = L.dotProduct(L) - tca * tca;
        double rSquared = radius * radius;

        if (dSquared > rSquared) {
            return null;
        }

        double thc = Math.sqrt(rSquared - dSquared);
        double t = Math.min(tca - thc, tca + thc);
        Vector intersectionPoint = ray.getCenterposition().add(ray.getV().multiplyByScalar(t));
        Vector normalInIntersection = intersectionPoint.sub(centerPosition);

        return new Intersection(intersectionPoint, t, normalInIntersection, this);
    }
}
