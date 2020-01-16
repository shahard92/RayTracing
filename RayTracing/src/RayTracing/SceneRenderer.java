package RayTracing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class SceneRenderer {

    private static final double EPSILON = Math.pow(10.0, -4);
    private Scene scene;
    private double epsilon = Double.MIN_VALUE;
    private Random random = new Random();

    public SceneRenderer(Scene scene) {
        this.scene = scene;
    }

    /**
     * Renders the scene and returns the rendered scene - color for each pixel in the image
     */
    public Color[][] renderScene(int imageWidth, int imageHeight) {

        Color[][] image = new Color[imageWidth][imageHeight];
        double windowWidth = scene.getCamera().getScreenWidth();
        double windowHeight = windowWidth  * 1.0 * imageHeight / imageWidth;

        Vector upVector = scene.getCamera().getUpVector();

        Vector normalizedTowards = scene.getCamera().getLookAt().sub(scene.getCamera().getPosition()).normalize();
        Vector normalizeVz = normalizedTowards;
        Vector normalizeVx = upVector.crossProduct(normalizedTowards).normalize();
        Vector normalizeVy = normalizeVx.crossProduct(normalizeVz).normalize();
        Vector p = scene.getCamera().getPosition().add(normalizeVz.multiplyByScalar(scene.getCamera().getScreenDistance()));
        Vector p0 = p.sub(normalizeVx.multiplyByScalar(windowWidth  * 1.0 / 2)).sub(normalizeVy.multiplyByScalar(windowHeight  * 1.0 /2));

        Vector vxStep = normalizeVx.multiplyByScalar(windowWidth  * 1.0 / imageWidth);
        Vector vyStep = normalizeVy.multiplyByScalar(windowHeight  * 1.0 / imageHeight);
        Vector superSampleVxStep = vxStep.multiplyByScalar(1.0 / scene.getSettings().getSuperSamplingLevel());
        Vector superSampleVyStep = vyStep.multiplyByScalar(1.0 / scene.getSettings().getSuperSamplingLevel());

        Ray ray = new Ray(scene.getCamera().getPosition(), p0.sub(scene.getCamera().getPosition()));
        Vector currentPixel;
        Vector startOfRow = p0;

// The main loop:
        for (int i = 0; i < imageHeight; i++)
        {
            currentPixel = startOfRow;
            for (int j = 0; j < imageWidth; j++)
            {
                Color sumOfColors = new Color(0.0, 0.0, 0.0);
                for (int k = 0; k < scene.getSettings().getSuperSamplingLevel(); k++) {
                    for (int l = 0; l < scene.getSettings().getSuperSamplingLevel(); l++) {
                        Vector corner = currentPixel.add(superSampleVxStep.multiplyByScalar(l)).add(superSampleVyStep.multiplyByScalar(k));
                        double xNoise = epsilon + (Math.abs(superSampleVxStep.l2Norm() - 2 * epsilon)) * random.nextDouble();
                        double yNoise = epsilon + (Math.abs(superSampleVyStep.l2Norm() - 2 * epsilon)) * random.nextDouble();
                        Vector currentPoint = corner.add(normalizeVx.multiplyByScalar(xNoise)).add(normalizeVy.multiplyByScalar(yNoise));

                        ray.setV(currentPoint.sub(scene.getCamera().getPosition()));

                        ArrayList<Intersection> intersections = getIntersections(ray);
                        intersections.sort(Comparator.comparing(Intersection::getT));

                        Color current = getColor(ray, intersections, 0);
                        sumOfColors.addColor(current);
                    }
                }

                Color interpulatedColor = sumOfColors.multiply(1.0 / (scene.getSettings().getSuperSamplingLevel() * scene.getSettings().getSuperSamplingLevel()));
                image[i][j] = interpulatedColor;
                currentPixel = currentPixel.add(vxStep);
            }
            startOfRow = startOfRow.add(vyStep);
        }

        return image;
    }


    private ArrayList<Intersection> getIntersections(Ray ray) {

        ArrayList<Intersection> intersections = new ArrayList<>();
        Intersection currentIntersection = null;

        for (Surface surface: scene.getAllSurfaces()) {

            currentIntersection = surface.findIntersectionWithRay(ray);
            if (currentIntersection != null) {
                intersections.add(currentIntersection);
            }
        }

        return intersections;
    }

    private Color getColor(Ray ray, ArrayList<Intersection> intersections, int currentRecurtionLevel) {

        if (intersections.size() == 0)
        {
            return scene.getSettings().getBackgroundColor();
        }

        Intersection firstIntersection = intersections.get(0);
        Material material = scene.getMaterials().get(firstIntersection.getSurface().getMaterialIndex() - 1);

        Color color = new Color(0.0, 0.0, 0.0);

        // Add background color (ambient) - to get same result as exampled we needed to remove this ambient factor
//        color = color.add(scene.getSettings().getBackgroundColor()).multiply(material.getTransparencyValue());

        // Add color from each light (specular and diffuse)
        for (Light light : scene.getLights())
        {
            Color lightColor = getSelfColorFromLight(firstIntersection, light, material, ray).multiply(1 - material.getTransparencyValue());
            color = color.add(lightColor);
        }

        // Add transparency factor before the reflection
        if (material.getTransparencyValue() != 0) {
            Color transperancyColor = getTransperancyColor(intersections, currentRecurtionLevel, material, ray);
            color = color.add(transperancyColor);
        }

        // Add reflection factor
        Color reflectionColor = scene.getSettings().getBackgroundColor().multiplyEachColor(material.getReflectionColor());

        if (currentRecurtionLevel < scene.getSettings().getMaxNumOfRecursions())
        {
            reflectionColor = getReflectionsColor(firstIntersection, currentRecurtionLevel, material, ray);
        }

        color = color.add(reflectionColor);

        return color;
    }

    private Color getSelfColorFromLight(Intersection intersection, Light light, Material material, Ray ray)
    {
        Vector normal = intersection.getNormal();
        Vector l = light.getPosition().sub(intersection.getIntersectionPoint()).normalize();
        double cosAlpha = normal.dotProduct(l);

        // if the light ray is perpendicular to the normal
        if (cosAlpha  < EPSILON)
        {
            return new Color(0.0,0.0,0.0);
        }

        // Calculate diffuse light:
        // Diffuse color (RGB). This is the "regular" color of a surface. This value is multiplied by the light
        // received by the object to find the base color of the object.
        Color diffuseColor = material.getDiffuseColor().multiplyEachColor(light.getColor()).multiply(cosAlpha);


        // calculate specular light:
        Vector r = (normal.multiplyByScalar (2 * cosAlpha).sub(l)).normalize();
        double cosPhi = r.dotProduct(ray.getV().multiplyByScalar(-1));

        Color specularColor = material.getSpecularColor();
        if (cosPhi > 0)
        {
            double cosPhiPowered = Math.pow(cosPhi,material.getPhongSpecularityCoefficient());
            specularColor = specularColor.multiplyEachColor(light.getColor()).multiply(light.getSpecularIntensity() * cosPhiPowered);
        } else {
            specularColor = new Color(0.0,0.0,0.0);
        }

        // Adding shadows calculation:
        double remainingLightFraction = getFractionOfLightAfterReducingShadows(l.multiplyByScalar(-1), intersection, light);

        return diffuseColor.add(specularColor).multiply(remainingLightFraction);
    }

    // Return the fraction of the light that the object receive by the light source (using light radius and average)
    public double getFractionOfLightAfterReducingShadows(Vector lightVector, Intersection intersection, Light light)
    {
        // if n=1, this produce a hard shadow
        if (scene.getSettings().getNumShadowRays() == 1)
        {
            return 1.0 - (light.getShadowIntensity() *
                    (1.0 - extentOfRayHit(light.getPosition(), intersection.getIntersectionPoint(), intersection.getSurface())));
        }

        // Create a rectangle on the plane and divide it into a grid of N*N shadow rays
        Vector normalizedTowards = light.getPosition().sub(intersection.getIntersectionPoint()).normalize();
        Vector normalizeVz = normalizedTowards;
        Vector normalizeVx = scene.getCamera().getUpVector().crossProduct(normalizedTowards).normalize();
        Vector normalizeVy = normalizeVx.crossProduct(normalizeVz).normalize();

        double halfRectangle = light.getWidth() * 0.5;
        Vector startPoint = light.getPosition().sub(normalizeVx.multiplyByScalar(halfRectangle)).sub(normalizeVy.multiplyByScalar(halfRectangle));

        double stepSize = light.getWidth()  * 1.0 / scene.getSettings().getNumShadowRays();
        Vector vxStep = normalizeVx.multiplyByScalar(stepSize);
        Vector vyStep = normalizeVy.multiplyByScalar(stepSize);

        Vector rowPosition = startPoint;
        double totalHitsIntensity = 0.0;
        int numOfShadowRays = scene.getSettings().getNumShadowRays();

        for (int i = 0; i < numOfShadowRays; i++)
        {
            Vector currentPosition = rowPosition;
            for (int j = 0; j < numOfShadowRays; j++)
            {
                double xNoise = epsilon + (Math.abs(vxStep.l2Norm() - 2 * epsilon)) * random.nextDouble();
                double yNoise = epsilon + (Math.abs(vyStep.l2Norm() - 2 * epsilon)) * random.nextDouble();

                Vector sourceOfLight = currentPosition.add(vxStep.multiplyByScalar(xNoise)).add(vyStep.multiplyByScalar(yNoise));

                totalHitsIntensity += extentOfRayHit(sourceOfLight, intersection.getIntersectionPoint(), intersection.getSurface());
                currentPosition = currentPosition.add(vxStep);
            }
            rowPosition = rowPosition.add(vyStep);
        }

        double averagedHitsIntensity = totalHitsIntensity / (numOfShadowRays * numOfShadowRays * 1.0);
        // The percentage of light intensity that doesn't arrive to the object
        double nonHitPrecentage = 1 - averagedHitsIntensity;

        return 1.0 - (nonHitPrecentage * light.getShadowIntensity());
    }

    private double extentOfRayHit(Vector lightLocation, Vector intersectionPoint, Surface surface)
    {
        Ray ray = new Ray(lightLocation, intersectionPoint.sub(lightLocation));

        ArrayList<Intersection> intersections = getIntersections(ray);
        intersections.sort(Comparator.comparing(Intersection::getT));

        if (intersections.size() == 0)
        {
            return 1.0;
        }

        Intersection firstIntersection = intersections.get(0);

        if (firstIntersection.getSurface() == surface) {
            return 1.0;
        }

        //There are objects in between
        double accumulatedTransperancy = 1.0;
        for (Intersection intersection: intersections)
        {
            if (intersection.getSurface() == surface)
            {
                break;
            }

            Material material = scene.getMaterials().get(intersection.getSurface().getMaterialIndex() - 1);
            accumulatedTransperancy *= material.getTransparencyValue();
        }

        return accumulatedTransperancy;
    }

    private Color getReflectionsColor(Intersection intersection, int recursionLevel, Material material, Ray ray)
    {
        Vector v = ray.getV();
        Vector normal = intersection.getNormal();
        // From lecture number 9: R = V-2 * (V dotproduct N) * N
        Vector r = (v.sub(normal.multiplyByScalar(2*(v.dotProduct(normal))))).normalize();
        ///////////////////////////////////////////////////////////////////////////////////////////////
        Ray reflectionRay = new Ray(intersection.getIntersectionPoint().add(r.multiplyByScalar(EPSILON)), r);
        ArrayList<Intersection> rayIntersections = getIntersections(reflectionRay);
        rayIntersections.sort(Comparator.comparing(Intersection::getT));

        Color reflectionColor = new Color(0.0, 0.0, 0.0);
        Color materialReflectionColor = material.getReflectionColor();

        if (materialReflectionColor.getBlue() != 0.0
                || materialReflectionColor.getGreen() != 0.0
                || materialReflectionColor.getRed() != 0.0)
        {
            reflectionColor = getColor(reflectionRay, rayIntersections, recursionLevel + 1);
            reflectionColor = reflectionColor.multiplyEachColor(materialReflectionColor);
        }

        return reflectionColor;
    }

    private Color getTransperancyColor(ArrayList<Intersection> intersections, int recursionLevel, Material material, Ray ray)
    {
        ArrayList<Intersection> transperencyIntersections = new ArrayList<>(intersections);
        transperencyIntersections.remove(0);
        Color transparencyColor = getColor(ray, transperencyIntersections, recursionLevel);

        return transparencyColor.multiply(material.getTransparencyValue());
    }

}
