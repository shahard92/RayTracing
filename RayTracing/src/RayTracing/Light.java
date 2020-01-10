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
}
