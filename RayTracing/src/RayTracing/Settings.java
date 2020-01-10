package RayTracing;

public class Settings {

    private Color backgroundColor;
    private int numShadowRays;
    private int maxNumOfRecursions;
    private int superSamplingLevel;

    public Settings(Color backgroundColor, int numShadowRays, int maxNumOfRecursions, int superSamplingLevel) {
        this.backgroundColor = backgroundColor;
        this.numShadowRays = numShadowRays;
        this.maxNumOfRecursions = maxNumOfRecursions;
        this.superSamplingLevel = superSamplingLevel;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public int getNumShadowRays() {
        return numShadowRays;
    }

    public int getMaxNumOfRecursions() {
        return maxNumOfRecursions;
    }

    public int getSuperSamplingLevel() {
        return superSamplingLevel;
    }
}
