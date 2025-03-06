package inkball;

import com.jogamp.common.net.PiggybackURLConnection;

import jogamp.graph.font.typecast.ot.table.CffTable.TopDictIndex;
import jogamp.nativewindow.windows.RECT;
import processing.core.PImage;

public class Wall {
    private int x;
    private int y;
    private int xcoordinate;
    private int ycoordinate;
    private PImage sprite;
    private double[] topleft;
    private double[] bottomleft;
    private double[] topright;
    private double[] bottomright;
    private double[][] pointcoordinate;
    private boolean acceleratedtile = false;
    private boolean hasfirstball=false;
    private boolean iswall=false;
    private boolean hasspawner = false;
    private String colour;
    private String accelerateddirection;
    /**
     * constructor of wall
     * @param x index of inner array
     * @param y index of array
     */
    public Wall(int x, int y){
        this.x = x;
        this.y = y;
        this.xcoordinate = x*App.CELLSIZE;
        this.ycoordinate = y*App.CELLSIZE + App.TOPBAR;
        this.sprite = null;
    }
    /**
     * set passed image as wall sprite
     * @param img set this wall's sprite as this PImage 
     */
    public void setSprite(PImage img){
        this.sprite = img; 
    }
    /**
     * get wall sprite
     * @return PImage
     */
    public PImage getSprite(){
        return this.sprite;
    }
    /**
     * set tile as accelerated tile
     */
    public void setAcceleratedTile(){
        acceleratedtile = true;
    }
    /**
     * get boolean if it is an acceleration tile
     * @return boolean
     */
    public boolean getAcceleratedTile(){
        return acceleratedtile;
    }
    /**
     * set the accelerating direction
     * @param s direction, up down left right
     */
    public void setAcceleratedDirection(String s){
        accelerateddirection = s;
    }
    /**
     * get its direction
     * @return direction up down left right
     */
    public String getAcceleratedDirection(){
        return accelerateddirection;
    }

    /**
     * set this tile as wall.
     */
    public void setWall(){
        iswall = true;
    }
    /**
     * get if it is wall
     * @return iswall
     */
    public boolean ifIsWall(){
        return iswall;
    }
    /**
     * if there is a ball on this tile, set this tile has the first ball
     */
    public void setFirstBall(){
        hasfirstball = true;
    }
    /**
     * get the boolean statement if there has a first ball
     * @return hasfirstball
     */
    public boolean ifHasFirstBall(){
        return hasfirstball;
    }
    /**
     *if there is a spawner, set this tile has a spawner
     */
    public void setSpawner(){
        hasspawner = true;
    }
    /**
     * get boolean if there is a spawner
     * @return boolean hasspawner
     */
    public boolean ifHasSpawner(){
        return hasspawner;
    }
    /**
     * get x index
     * @return x
     */
    public int getX(){
        return this.x;
    }
    /** 
     * get y index
     * @return y
     */
    public int getY(){
        return this.y;
    }

    /**
     * get tile coordinate on the board
     * @return xcoordinate
     */
    public int getXcoordinate(){
        return this.xcoordinate;
    }
    /**
     * get y cooridnate of this tile on board
     * @return y coordinate
     */
    public int getYcoordinate(){
        return this.ycoordinate;
    }
    /**
     * set this color
     * @param s this wall's color
     */
    public void setColour(String s){
        this.colour = s;
    }
    /**
     * get the tile color
     * @return colour
     */
    public String getColour(){
        return colour;
    }
    /**
     * get four point cooridnate of this tile
     * @param app App
     * @return double[][] four point cooridnate
     */
    public double[][] getPointCoordinate(App app){
        //coordinate of 4 point
        double wallwidth = this.sprite.width;
        double topleftx = this.getX()*App.CELLSIZE;
        double toplefty = this.getY()*App.CELLSIZE + App.TOPBAR;
        double toprightx = this.getX()*App.CELLSIZE+wallwidth;
        double toprighty = this.getY()*App.CELLSIZE + App.TOPBAR;

        double bottomleftx = this.getX()*App.CELLSIZE;
        double bottomlefty = this.getY()*App.CELLSIZE + wallwidth + App.TOPBAR;
        double bottomrightx = this.getX()*App.CELLSIZE + wallwidth;
        double bottomrighty = this.getY()*App.CELLSIZE + wallwidth + App.TOPBAR;
        topright = new double[] {toprightx,toprighty};
        topleft = new double[] {topleftx,toplefty};
        bottomleft = new double[] {bottomleftx,bottomlefty};
        bottomright = new double[] {bottomrightx,bottomrighty};
        pointcoordinate = new double[][] {topleft,topright,bottomright,bottomleft};
        return pointcoordinate;
    }
    /**
     * draw this tile
     * @param app App
     */
    public void draw(App app){
        app.image(this.sprite, x*App.CELLSIZE, y*App.CELLSIZE+App.TOPBAR);
    }
    
}
