package RayTracing;

import java.util.ArrayList;

public class Scene {
    private Camera camera;
    private Settings settings;
    private ArrayList<Material> materials;
    private ArrayList<Sphere> spheres;
    private ArrayList<Plane> planes;
    private ArrayList<Light> lights;
    private ArrayList<Triangle> triangles;

    public Scene() {
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public ArrayList<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<Material> materials) {
        this.materials = materials;
    }

    public ArrayList<Sphere> getSpheres() {
        return spheres;
    }

    public void setSpheres(ArrayList<Sphere> spheres) {
        this.spheres = spheres;
    }

    public ArrayList<Plane> getPlanes() {
        return planes;
    }

    public void setPlanes(ArrayList<Plane> planes) {
        this.planes = planes;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(ArrayList<Triangle> triangles) {
        this.triangles = triangles;
    }
}
