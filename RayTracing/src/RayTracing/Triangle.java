package RayTracing;

public class Triangle extends Surface {

    private Vector vertex1Position;
    private Vector vertex2Position;
    private Vector vertex3Position;
    private Plane plane;
    private Vector v1v2;
    private Vector v1v3;

    public Triangle(Vector vertex1Position, Vector vertex2Position, Vector vertex3Position, int materialIndex) {
        this.vertex1Position = vertex1Position;
        this.vertex2Position = vertex2Position;
        this.vertex3Position = vertex3Position;
        this.materialIndex = materialIndex;

        Vector firstVectorInPlane = vertex1Position.sub(vertex2Position);
        Vector secondVectorInPlane = vertex1Position.sub(vertex3Position);

        Vector normal = firstVectorInPlane.crossProduct(secondVectorInPlane).normalize();
        double c = vertex1Position.dotProduct(normal);
        plane = new Plane(normal, c, materialIndex);

        v1v2 = vertex2Position.sub(vertex1Position);
        v1v3 = vertex3Position.sub(vertex1Position);
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


    @Override
    public Intersection findIntersectionWithRay(Ray ray) {

        Intersection intersection = plane.findIntersectionWithRay(ray);

        if (intersection == null) {
            return null;
        }

        double v1v2Norm, v1v2Dotv1v3, v1v3Norm, pDotV1Dotv1v2, pDotV1Dptv1v3, D;

        v1v2Norm = v1v2.dotProduct(v1v2);
        v1v2Dotv1v3 = v1v2.dotProduct(v1v3);
        v1v3Norm = v1v3.dotProduct(v1v3);
        Vector pDotV1 = intersection.getIntersectionPoint().sub(vertex1Position);

        pDotV1Dotv1v2 = pDotV1.dotProduct(v1v2);
        pDotV1Dptv1v3 = pDotV1.dotProduct(v1v3);
        D = v1v2Dotv1v3 * v1v2Dotv1v3  - v1v2Norm * v1v3Norm;

        double alpha, betha;
        alpha = (v1v2Dotv1v3 * pDotV1Dptv1v3 - v1v3Norm * pDotV1Dotv1v2) / D;

        if (alpha < 0) {
            return null;
        }

        betha = (v1v2Dotv1v3 * pDotV1Dotv1v2 - v1v2Norm * pDotV1Dptv1v3) / D;
        if (betha < 0 || (alpha + betha) > 1) {
            return null;
        }

        return intersection;
    }
}
