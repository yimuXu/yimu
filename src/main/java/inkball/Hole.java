package inkball;

import com.jogamp.common.net.PiggybackURLConnection;

import processing.core.PImage;

public class Hole {
    private int x;
    private int y;
    private String color;
    private PImage sprite;
    /**
     * constructor of Ball 
     * @param x cooridnate
     * @param y coordinate
     * @param c color
     */
    public Hole(int x, int y, String c){
        this.x = x;
        this.y = y;
        color = c;
    }
    /**
     * get center X coordinate
     * @param app App
     * @return center x coordinate
     */
    public int getcenterXcoordinate(App app){
        int X = (x)*App.CELLSIZE + app.getSprite("hole0").width/2;//
        return X;
    }
    /** 
    * get center Y coordinate
    * @param app App
    * @return center Y coordinate
    */
    public int getcenterYcoordinate(App app){
        int Y = (y)*App.CELLSIZE  + app.getSprite("hole0").width/2 + App.TOPBAR;//
        return Y;
    }
    /**
     * set a PImage as hole's sprite
     * @param img PImage of this hole
     */
    public void setSprite(PImage img){
        this.sprite = img;
    }
    /**
     * get color of the hole
     * @return color
     */
    public String getColor(){
        return this.color;
    }
    /**
     * draw the hole
     * @param app App
     */
    public void draw(App app){
        app.image(sprite,(float)x*App.CELLSIZE,(float)(y*App.CELLSIZE + App.TOPBAR));
    }

}
