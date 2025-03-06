package inkball;
/**
 * store several method to calculate
 */
public interface Calculate {
    /**
     * given 4 double value, calculate distance of two points, in order of x1, y1, x2, y2
     * @param x1 first point x
     * @param y1 first point y
     * @param x2 second point x
     * @param y2 second point y
     * @return double distance
     */
    public static double getDistance(double x1,double y1, double x2,double y2){
        double d = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
        return d;
    }
    /**
     * get a double array of middle point
     * @param x1  first point x
     * @param y1 first point y
     * @param x2 second point x
     * @param y2 second point y
     * @return double array of middle point x, y coordinate
     */
    public static double[] getMidPoint(double x1,double y1,double x2,double y2){
        double[] p = new double[] {(x1+x2)/2,(y1+y2)/2};
        return p;
    }
    /**
     * get an array of normal vectors
     * @param x1 first point x
     * @param y1 first point y
     * @param x2 second point x
     * @param y2 second point y
     * @return double array, two vectores coordinate
     */
    public static double[][] getNormalVectors(double x1,double y1,double x2,double y2){
        double dx = x1 - x2;
        double dy = y1 - y2;
        double nordx;
        double nordy;        
        if(dx == 0){
            nordx = 0;
        }else{
            nordx = dx/Math.sqrt(dx*dx+dy*dy); 
        }
        if(dy == 0){
            nordy = 0;
        }else{
            nordy = dy/Math.sqrt(dx*dx+dy*dy); 
        }
        double[] normalvector1 = new double[] {-nordy,nordx};
        double[] normalvector2 = new double[] {nordy,-nordx};
        double[][] normalvectors = new double[][] {normalvector1, normalvector2};
        return normalvectors;
    }
}
