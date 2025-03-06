package inkball;

import java.net.NoRouteToHostException;

public class Linesegment implements Calculate{
    private float startx;
    private float starty;
    private float endx;
    private float endy;
    private boolean iscollided = false;
    /**
     * constructor of line segment
     * create a line segment with start coordinate and end coordinate
     * @param sx start x
     * @param sy start y
     * @param ex end x
     * @param ey end y
     */
    public Linesegment(float sx,float sy, float ex, float ey){
        startx = sx;
        starty = sy;
        endx = ex;
        endy = ey;

    }
    /**
     * return start x
     * @return startx
     */
    public float getstartx(){
        return startx;
    }
    /**
     * return start y
     * @return starty
     */ 
    public float getstarty(){
        return starty;
    }
    /**
     * return end x
     * @return endx
     */
    public float getendx(){
        return endx;
    }
    /**
     * return end y
     * @return endy
     */
    public float getendy(){
        return endy;
    }
    /**
     * set it is collided
     */
    public void collide(){
        iscollided = true;
    }
    /**
     * get state of collision
     * @return iscollided
     */
    public boolean getCollision(){
        return iscollided;
    }
}
