package inkball;

public class Spawner{
    private int x;
    private int y;
    /**
     * constructor of Spawner
     * @param x index of spawner in the board array
     * @param y index of spawner in the board array
     */
    public Spawner(int x, int y){
        this.x = x;
        this.y = y;
    }
    /**
     * get x
     * @return int x 
     */
    public int getx(){
        return this.x;
    }
    /**
     * get y
     * @return int y
     */
    public int gety(){
        return this.y;

    }
    /**
     * get x coordinate
     * @param app App
     * @return int x coordinate
     */
    public int getXcoordinate(App app){
        int X = x*App.CELLSIZE;
        return X;
    }
    /**
     * get y coordinate
     * @param app App
     * @return y coordinate
     */
    public int getYcoordinate(App app){
        int Y = y*App.CELLSIZE;
        return Y;
    }
}