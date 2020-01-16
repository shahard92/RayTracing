package RayTracing;

public class Color {

    private double red;
    private double green;
    private double blue;

    public Color() {
    }

    public Color(double red, double green, double blue) {
        this.red = trimColorValue(red);
        this.green = trimColorValue(green);
        this.blue = trimColorValue(blue);
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    public Color multiply(double factor) {
        return new Color(red * factor, green * factor, blue * factor);
    }

    public Color add(Color other) {
        return new Color(this.red + other.red, this.green + other.green, this.blue + other.blue);
    }

    public Color multiplyEachColor(Color factors) {
        return new Color(this.red * factors.getRed(), this.green * factors.getGreen(), this.blue * factors.getBlue());
    }

    private double trimColorValue(double value) {
        return Math.max(0d, Math.min(1.0, value));
    }

    public void addColor(Color color) {
        red += color.getRed();
        green += color.getGreen();
        blue += color.getBlue();
    }
}
