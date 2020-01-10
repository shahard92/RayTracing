package RayTracing;

public class Vector {

    private double x;
    private double y;
    private double z;

    public Vector() {
    }

    public Vector(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Subtracts a given vector from this vector
     */
    public Vector sub(Vector other)
    {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Adds a given vector to this vector
     */
    public Vector add(Vector other)
    {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }


    /**
     * Calculates the dot product of this vector and the given vector
     */
    public double dotProduct(Vector other)
    {
        return x * other.x + y * other.y + other.z * z;
    }

    /**
     * Performs a cross product between this vector and a given vector
     */
    public Vector crossProduct(Vector other)
    {
        return new Vector(y * other.z - z * other.y,
                          z * other.x - x * other.z,
                          x*other.y - y * other.x);
    }

    /**
     * return the size of this vector
     */
    public double l2Norm()
    {
        double normSquared = dotProduct(this);
        return Math.sqrt(normSquared);
    }

    /**
     * return the current vector normalized
     */
    public Vector normalize()
    {
        double norm = l2Norm();

        return new Vector(x / norm, y / norm, z/norm);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }


    public String toString()
    {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    /**
     * Multiplies this vector by a given scalar
     */
    public Vector multiplyByScalar(double scalar)
    {
        return new Vector(x * scalar, y * scalar, z * scalar);
    }
}
