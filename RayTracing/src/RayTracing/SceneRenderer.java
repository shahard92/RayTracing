package RayTracing;

import java.util.ArrayList;
import java.util.Random;

public class SceneRenderer {

    private Scene scene;

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
        // asdkljfhasdjofgjklasdghfjklasdhfklj
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
        Vector current_pixel;
        Vector startOfRow = p0;

        double epsilon = Double.MIN_VALUE;
        Random random = new Random();

// The main loop:
        for (int i = 0; i < imageHeight; i++)
        {
            current_pixel = startOfRow;
            for (int j = 0; j < imageWidth; j++)
            {
                ArrayList<Color> interpulationColors = new ArrayList<>();
                for (int k = 0; k < scene.getSettings().getSuperSamplingLevel(); k++) {
                    for (int l = 0; l < scene.getSettings().getSuperSamplingLevel(); l++) {
                        Vector corner = current_pixel.add(superSampleVxStep.multiplyByScalar(l)).add(superSampleVyStep.multiplyByScalar(k));
                        double xNoise = epsilon + (Math.abs(superSampleVxStep.l2Norm() - 2 * epsilon)) * random.nextDouble();
                        double yNoise = epsilon + (Math.abs(superSampleVyStep.l2Norm() - 2 * epsilon)) * random.nextDouble();
                        Vector currentPoint = corner.add(normalizeVx.multiplyByScalar(xNoise)).add(normalizeVy.multiplyByScalar(yNoise));

                        ray.setV(currentPoint.sub(scene.getCamera().getPosition()));

                        ArrayList<Intersection> intersections = getIntersectionsSorted(ray);
                        Color currentColor = getColor(intersections, 0);
                        interpulationColors.add(currentColor)
                    }
                }

                Color interpulationColor; // calculate some interpolation over the color array
                image[i][j] = interpulationColor;
                current_pixel = current_pixel.add(vxStep);
            }
            startOfRow = startOfRow.add(vyStep);
        }

        return imageData;


        double v_x = windowWidth * 1.0 / imageWidth;



    }
}
