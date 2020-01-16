package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

    public int imageWidth;
    public int imageHeight;
    private Scene scene;

    /**
     * Runs the ray tracer. Takes scene file, output image file and image size as input.
     */
    public static void main(String[] args) {

        try {

            RayTracer tracer = new RayTracer();

            // Default values:
            tracer.imageWidth = 200;
            tracer.imageHeight = 200;

            if (args.length < 2)
                throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

            String sceneFileName = args[0];
            String outputFileName = args[1];

            if (args.length > 3)
            {
                tracer.imageWidth = Integer.parseInt(args[2]);
                tracer.imageHeight = Integer.parseInt(args[3]);
            }

            tracer.scene = new Scene();
            // Parse scene file:
            tracer.parseScene(sceneFileName);

            // Render scene:
            tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
        } catch (RayTracerException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    /**
     * Parses the scene file and creates the scene. Change this function so it generates the required objects.
     */
    public void parseScene(String sceneFileName) throws IOException, RayTracerException
    {
        FileReader fr = new FileReader(sceneFileName);
        BufferedReader r = new BufferedReader(fr);
        String line = null;
        int lineNum = 0;
        System.out.println("Started parsing scene file " + sceneFileName);
        ArrayList<Material> materials = new ArrayList<>();
        ArrayList<Sphere> spheres = new ArrayList<>();
        ArrayList<Plane> planes = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();
        ArrayList<Light> lights = new ArrayList<>();

        while ((line = r.readLine()) != null)
        {
            line = line.trim();
            ++lineNum;

            if (line.isEmpty() || (line.charAt(0) == '#'))
            {  // This line in the scene file is a comment
                continue;
            }
            else
            {
                String code = line.substring(0, 3).toLowerCase();
                // Split according to white space characters:
                String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

                if (code.equals("cam"))
                {
                    // Add code here to parse camera parameters
                    Camera camera = createCameraFromParams(params);
                    scene.setCamera(camera);

                    System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
                }
                else if (code.equals("set"))
                {
                    // Add code here to parse general settings parameters
                    Settings settings = createSettingsFromParams(params);
                    scene.setSettings(settings);

                    System.out.println(String.format("Parsed general settings (line %d)", lineNum));
                }
                else if (code.equals("mtl"))
                {
                    // Add code here to parse material parameters
                    Material material = createMaterialFromParams(params);
                    materials.add(material);

                    System.out.println(String.format("Parsed material (line %d)", lineNum));
                }
                else if (code.equals("sph"))
                {
                    // Add code here to parse sphere parameters
                    Sphere sphere = createSphereFromParams(params);
                    spheres.add(sphere);

                    System.out.println(String.format("Parsed sphere (line %d)", lineNum));
                }
                else if (code.equals("pln"))
                {
                    // Add code here to parse plane parameters
                    Plane plane = createPlaneFromParams(params);
                    planes.add(plane);

                    System.out.println(String.format("Parsed plane (line %d)", lineNum));
                }
                else if (code.equals("lgt"))
                {
                    // Add code here to parse light parameters
                    Light light = createLightFromParams(params);
                    lights.add(light);

                    System.out.println(String.format("Parsed light (line %d)", lineNum));
                }
                else if (code.equals("trg")) {
                    Triangle triangle = createTriangleFromParams(params);
                    triangles.add(triangle);

                    System.out.println(String.format("Parsed triangle (line %d)", lineNum));
                }
                else
                {
                    System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
                }
            }
        }

        scene.setMaterials(materials);
        scene.setPlanes(planes);
        scene.setLights(lights);
        scene.setSpheres(spheres);
        scene.setTriangles(triangles);


        // It is recommended that you check here that the scene is valid,
        // for example camera settings and all necessary materials were defined.

        System.out.println("Finished parsing scene file " + sceneFileName);

    }

    /**
     * Renders the loaded scene and saves it to the specified file location.
     */
    public void renderScene(String outputFileName)
    {
        long startTime = System.currentTimeMillis();

        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

        SceneRenderer renderer = new SceneRenderer(scene);

        Color[][] image = new Color[imageWidth][imageHeight];
        try {
            image = renderer.renderScene(imageWidth, imageHeight);
        }
        catch (Exception e) {
            e.getStackTrace();
            e.printStackTrace();
        }

        try {
            for (int row = 0; row < imageHeight; row++)
            {
                for (int col = 0; col < imageWidth; col++)
                {
                    Color color = image[row][col];
                    rgbData[(row*imageWidth + col)*3] = (byte) (color.getRed() * 255);
                    rgbData[(row*imageWidth + col)*3 + 1] = (byte) (color.getGreen() * 255);
                    rgbData[(row*imageWidth + col)*3 + 2] = (byte) (color.getBlue() * 255);
                }
            }
        }
        catch (Exception e) {
            System.out.println("exceptionnnnnnnnn 2222222222222222");
        }


        // Put your ray tracing code here!
        //
        // Write pixel color values in RGB format to rgbData:
        // Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
        //            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
        //             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
        //
        // Each of the red, green and blue components should be a byte, i.e. 0-255


        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;

        // The time is measured for your own conveniece, rendering speed will not affect your score
        // unless it is exceptionally slow (more than a couple of minutes)
        System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

        // This is already implemented, and should work without adding any code.
        saveImage(this.imageWidth, rgbData, outputFileName);

        System.out.println("Saved file " + outputFileName);

    }




    //////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

    /*
     * Saves RGB data as an image in png format to the specified location.
     */
    public static void saveImage(int width, byte[] rgbData, String fileName)
    {
        try {

            BufferedImage image = bytes2RGB(width, rgbData);
            ImageIO.write(image, "png", new File(fileName));

        } catch (IOException e) {
            System.out.println("ERROR SAVING FILE: " + e.getMessage());
        }

    }

    /*
     * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
     */
    public static BufferedImage bytes2RGB(int width, byte[] buffer) {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);

        return result;
    }

    public static class RayTracerException extends Exception {
        public RayTracerException(String msg) {  super(msg); }
    }

    private Vector createVectorFromParams(String[] params, int index1, int index2, int index3) {
        return new Vector(Double.parseDouble(params[index1]),
                Double.parseDouble(params[index2]),
                Double.parseDouble(params[index3]));
    }

    private Color createColorFromParams(String[] params, int index1, int index2, int index3) {
        return new Color(Double.parseDouble(params[index1]),
                Double.parseDouble(params[index2]),
                Double.parseDouble(params[index3]));
    }

    private Camera createCameraFromParams(String[] params) {
        Vector position = createVectorFromParams(params, 0, 1, 2);
        Vector lookAt =  createVectorFromParams(params, 3, 4, 5);
        Vector upVector =  createVectorFromParams(params, 6, 7, 8);
        double screenDistance = Double.parseDouble(params[9]);
        double screenWidth = Double.parseDouble(params[10]);

        return new Camera(position, lookAt, upVector, screenDistance, screenWidth);
    }

    private Triangle createTriangleFromParams(String[] params) {
        Vector vertex1 = createVectorFromParams(params, 0, 1, 2);
        Vector vertex2 = createVectorFromParams(params, 3, 4, 5);
        Vector vertex3 = createVectorFromParams(params, 6, 7, 8);
        int materialIndex = Integer.parseInt(params[9]);

        return new Triangle(vertex1, vertex2, vertex3, materialIndex);
    }

    private Light createLightFromParams(String[] params) {
        Vector position = createVectorFromParams(params, 0, 1, 2);
        Color color = createColorFromParams(params, 3, 4, 5);
        double specularIntensity = Double.parseDouble(params[6]);
        double shadowIntensity = Double.parseDouble(params[7]);
        double width = Double.parseDouble(params[8]);

        return new Light(position, color, specularIntensity, shadowIntensity, width);
    }

    private Plane createPlaneFromParams(String[] params) {

        Vector normalVector = createVectorFromParams(params, 0, 1, 2);
        double offset = Double.parseDouble(params[3]);
        int materialIndex = Integer.parseInt(params[4]);

        return new Plane(normalVector, offset, materialIndex);
    }

    private Sphere createSphereFromParams(String[] params) {

        Vector centerPosition = createVectorFromParams(params, 0, 1, 2);
        double radius = Double.parseDouble(params[3]);
        int materialIndex = Integer.parseInt(params[4]);

        return new Sphere(centerPosition, radius, materialIndex);
    }

    private Material createMaterialFromParams(String[] params) {
        Color diffuseColor = createColorFromParams(params, 0, 1, 2);
        Color specularColor = createColorFromParams(params, 3, 4, 5);
        Color reflectionColor = createColorFromParams(params, 6, 7, 8);
        double phongSpecularityCoefficient = Double.parseDouble(params[9]);
        double transparencyValue = Double.parseDouble(params[10]);

        return new Material(diffuseColor, specularColor, reflectionColor, phongSpecularityCoefficient, transparencyValue);
    }

    private Settings createSettingsFromParams(String[] params) {
        Color backgroundColor = createColorFromParams(params, 0, 1, 2);
        int numShadowRays = Integer.parseInt(params[3]);
        int maxNumOfRecursions = Integer.parseInt(params[4]);
        int superSamplingLevel = 1;
        try {
            superSamplingLevel = Integer.parseInt(params[5]);
        }
        catch (Exception e) {
            System.out.println("didn't have super sampling as input");
        }

        return new Settings(backgroundColor, numShadowRays, maxNumOfRecursions, superSamplingLevel);
    }

}
