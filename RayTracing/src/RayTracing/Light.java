package RayTracing;

public class Light {

    private Vector position;
    private Color color;
    private double specularIntensity;
    private double shadowIntensity;
    private double width;

    public Light(Vector position, Color color, double specularIntensity, double shadowIntensity, double width) {
        this.position = position;
        this.color = color;
        this.specularIntensity = specularIntensity;
        this.shadowIntensity = shadowIntensity;
        this.width = width;
    }

    public Vector getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public double getSpecularIntensity() {
        return specularIntensity;
    }

    public double getShadowIntensity() {
        return shadowIntensity;
    }

    public double getWidth() {
        return width;
    }
}
