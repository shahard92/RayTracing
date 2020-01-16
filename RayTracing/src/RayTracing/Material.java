package RayTracing;

public class Material {

    private Color diffuseColor;
    private Color specularColor;
    private Color reflectionColor;
    private double phongSpecularityCoefficient;
    private double transparencyValue;

    public Material(Color diffuseColor, Color specularColor, Color reflectionColor, double phongSpecularityCoefficient, double transparencyValue) {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.reflectionColor = reflectionColor;
        this.phongSpecularityCoefficient = phongSpecularityCoefficient;
        this.transparencyValue = transparencyValue;
    }

    public Color getDiffuseColor() {
        return diffuseColor;
    }

    public Color getSpecularColor() {
        return specularColor;
    }

    public Color getReflectionColor() {
        return reflectionColor;
    }

    public double getPhongSpecularityCoefficient() {
        return phongSpecularityCoefficient;
    }

    public double getTransparencyValue() {
        return transparencyValue;
    }
}
