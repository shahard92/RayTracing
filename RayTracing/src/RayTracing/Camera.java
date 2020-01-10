package RayTracing;

public class Camera {

    private Vector position;
    private Vector lookAt;
    private Vector upVector;
    private double screenDistance;
    private double screenWidth;

    public Camera(Vector position, Vector lookAt, Vector upVector, double screenDistance, double screenWidth)
    {
        this.position = position;
        this.lookAt = lookAt;
        this.upVector = upVector;
        this.screenDistance = screenDistance;
        this.screenWidth = screenWidth;
    }

    public Vector getPosition()
    {
        return position;
    }

    public Vector getLookAt()
    {
        return lookAt;
    }

    public Vector getUpVector()
    {
        return upVector;
    }

    public double getScreenDistance()
    {
        return screenDistance;
    }

    public double getScreenWidth()
    {
        return screenWidth;
    }
}
