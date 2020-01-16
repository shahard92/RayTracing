package RayTracing;

public abstract class Surface {

    protected int materialIndex;

    public int getMaterialIndex() {
        return materialIndex;
    }

    public abstract Intersection findIntersectionWithRay(Ray ray);
}
