package inkball;

import processing.core.PImage;
import processing.core.PConstants;

import java.util.*;

import com.google.common.collect.Streams.DoubleFunctionWithIndex;
import com.jogamp.common.net.PiggybackURLConnection;

public class Ball implements Calculate{
    private String colour;
    private PImage sprite;
    private int x;
    private int y;
    private double xvel;
    private double yvel;
    private double xcoordinate;
    private double ycoordinate;
    private boolean onboard=false;
    private boolean absorbed = false;
    private int ballradius;
    private double xcentralcoordinate;
    private double ycentralcoordinate;

    /**
     * constructor of the ball, onboard, iscollided, absorbed is defaulted false.
     */
    public Ball(){
        onboard=false;
        absorbed = false; 
    }
    /**
     * set velocity of this ball.
     */
    public void initializeVel(){
        this.xvel = App.random.nextBoolean() ? 2 : -2;
        this.yvel = App.random.nextBoolean() ? 2 : -2;
    }
    /**
     * set passing Pimage as the sprite of this ball 
     * @param img a PImage
     */
    public void setSprite(PImage img){
        this.sprite = img;
        ballradius = this.sprite.width/2;

    }
    /**
     * get this ball's sprite
     * @return sprite
     */
    public PImage getSprite(){
        return this.sprite;
    }
    /**
     * set the color of this ball
     * @param s the color
     */
    public void setColour(String s){
        this.colour = s;
    }
    /**
     * get this ball colour
     * @return the colour of ball
     */
    public String getColour(){
        return this.colour;
    }
    /**
     * set the coordinate of the ball on the board and the index in the array.
     * @param x x coordinate or index of this tile in an array
     * @param y y coordinate or index of this tile in an array
     */
    public void setCoordinate(int x, int y){ 
        if(this.onboard){
            this.x = x;
            this.y = y;
            xcoordinate = x*App.CELLSIZE;
            ycoordinate = y*App.CELLSIZE+App.TOPBAR;            
        }else{
            xcoordinate = x;
            ycoordinate = y;
        }
        xcentralcoordinate = xcoordinate + ballradius;
        
        ycentralcoordinate = ycoordinate + ballradius;

    }
    /**
     * change the state of if it is displayed on the board,
     */
    public void setOnBoard(){
        if(onboard){
            onboard = false;
        }else{
            onboard = true;  
        }
        
    }
    /**
     * get boolean 'onboard'
     * @return onboard
     */
    public boolean getOnBoard(){
        return onboard;
    }
    /**
     * mark this ball is absorbed
     */

    public void absorb(){
        absorbed = true;
    }
    /**
     * get state of absorbed
     * @return absorbed
     */
    public boolean absorption(){
        return absorbed;
    }
    /**
     * pass app and 4 double value, to calculate which normal vector is we need then set the velocity.
     * @param app App
     * @param a the first point x coordinate
     * @param b the first point y coordinate
     * @param c the second point x coordinate
     * @param d the second point y coordinate
     */
    public void calculateVel(App app, double a,double b, double c, double d){
        double[][] vecs = Calculate.getNormalVectors(a, b, c, d);
        double[] midpoint = Calculate.getMidPoint(a, b, c, d);
        double distancelineball1 = Calculate.getDistance(xcentralcoordinate, ycentralcoordinate, midpoint[0]+vecs[1][0], midpoint[1]+vecs[1][1]);
        double distancelineball2 = Calculate.getDistance(xcentralcoordinate, ycentralcoordinate, midpoint[0]+vecs[1][0], midpoint[1]+vecs[1][1]);
        if(distancelineball1<distancelineball2){
            vec = vecs[0];
        }else{

            vec = vecs[1];
        }
        this.setvel(vec);

    }
    /**
     * pass the app in it. check if the ball is collided with wall by get four point of the wall check the nearest points
     * then calculate the velocity.same logic with line.
     * @param app  App
     */
    double[] vec;
    public void checkCollision(App app){

        
        for(Wall[] w : app.board){
            for(Wall w1: w){
                if(w1.ifIsWall()){
                    double ballradius = this.sprite.width/2;
                    double xc = this.getXcoordinate() + ballradius + xvel;
                    double yc = this.getYcoordinate() + ballradius + yvel;
                    double[][] fourpointslist = w1.getPointCoordinate(app);
                    double dtopleft = Calculate.getDistance(fourpointslist[0][0], fourpointslist[0][1], xc,yc);
                    double dtopright = Calculate.getDistance(fourpointslist[1][0], fourpointslist[1][1], xc,yc);
                    double dbottomright = Calculate.getDistance(fourpointslist[2][0], fourpointslist[2][1], xc,yc);
                    double dbottomleft = Calculate.getDistance(fourpointslist[3][0], fourpointslist[3][1], xc,yc);
                    boolean top = dtopleft + dtopright <= App.CELLSIZE + ballradius;
                    boolean right = dtopright + dbottomright <= App.CELLSIZE + ballradius;
                    boolean bottom = dbottomleft + dbottomright <= App.CELLSIZE + ballradius;
                    boolean left = dtopleft + dbottomleft <= App.CELLSIZE + ballradius;
                    //final vector;
                    if(top){
                        calculateVel(app, fourpointslist[0][0], fourpointslist[0][1], fourpointslist[1][0], fourpointslist[1][1]);
            
                    }else if(right){
                        calculateVel(app, fourpointslist[2][0], fourpointslist[2][1], fourpointslist[1][0], fourpointslist[1][1]);
                    
                    }else if(bottom){
                        calculateVel(app, fourpointslist[2][0], fourpointslist[2][1], fourpointslist[3][0], fourpointslist[3][1]);
                        
                    }else if(left){
                        calculateVel(app, fourpointslist[0][0], fourpointslist[0][1], fourpointslist[3][0], fourpointslist[3][1]);
            
                    }

                    if(top || right || bottom || left){
                        if(w1.getColour().equals("orange")){
                            this.setColour("orange");
                            this.setSprite(app.getSprite("ball1"));
                        }else if(w1.getColour().equals("blue")){
                            this.setColour("blue");
                            this.setSprite(app.getSprite("ball2"));
                        }else if(w1.getColour().equals("green")){
                            this.setColour("green");
                            this.setSprite(app.getSprite("ball3"));
                        }else if(w1.getColour().equals("yellow")){
                            this.setColour("yellow");
                            this.setSprite(app.getSprite("ball4"));
                        }
                        
                    }                    
                }
            }
        }
        
        //line has been drawn but not be collided.
        for(ArrayList<Linesegment> line : app.lines){
            boolean eraseline = false;
            for(Linesegment l : line){
                
                if(!l.getCollision()){
                    double lsx =l.getstartx();
                    double lsy =l.getstarty();
                    double lex =l.getendx();
                    double ley =l.getendy();
                    double xc = this.getXcoordinate() + ballradius + xvel;
                    double yc = this.getYcoordinate() + ballradius + yvel;
                    
                    double d1 = Calculate.getDistance(xc,yc,lsx,lsy);
                    double d2 = Calculate.getDistance(xc,yc,lex,ley);
                    double d3 = Calculate.getDistance(lsx, lsy, lex, ley) + ballradius;

                    if((d1+d2)<=d3){
                        eraseline = true;
                        calculateVel(app,l.getstartx(), l.getstarty(), l.getendx(), l.getendy());
                    }
                    if(app.mousePressed && (app.mouseButton == App.RIGHT)){
                        double d4 = Calculate.getDistance(app.mouseX, app.mouseY, lsx, lsy);
                        double d5 = Calculate.getDistance(app.mouseX, app.mouseY, lex, ley);                        
                        if((d4+d5)<= d3+20){
                            eraseline = true;
                        }   
                    }
   
                }
                
            }
            if(eraseline){
                for(Linesegment l : line){
                    l.collide();
                }
            
            }
        }
        // line that is being drawn.
        boolean eraseCurrentLine = false;
        for(Linesegment l : app.linesegments){
            if(!l.getCollision()){
                ballradius = this.sprite.width/2;
                double xc = this.getXcoordinate() + ballradius + xvel;//central coordinate of ball of next frame
                double yc = this.getYcoordinate() + ballradius + yvel;                
                
                double d1 = Calculate.getDistance(xc,yc,l.getstartx(), l.getstarty());
                double d2 = Calculate.getDistance(xc,yc,l.getendx(), l.getendy());
                double d3 = Calculate.getDistance(l.getstartx(), l.getstarty(), l.getendx(), l.getendy()) + ballradius;
                if((d1+d2)<=d3){
                    //this.collide();
                    eraseCurrentLine = true;
                    calculateVel(app,l.getstartx(), l.getstarty(), l.getendx(), l.getendy());
                }                
            }
        }
        if(eraseCurrentLine){
            for(Linesegment l : app.linesegments){
                l.collide();
            }
        }   
    }
    /**
     * pass the app in this method, check whether the location of the ball is close to a hole and which hole
     * then calculate the socre 
     * @param app App
     */
    public void checkAbsorption(App app){
        int ballradius = this.sprite.width/2;
        double posx = this.xcoordinate +ballradius;
        double posy = this.ycoordinate + ballradius +App.TOPBAR;
        double targetx,targety;
        PImage img = app.getSprite("ball"+app.colorsprites.get(this.colour)).copy();
        int holewidth = app.getSprite("hole1").width/2;
        for(Hole h : app.holes){
            targetx = h.getcenterXcoordinate(app);
            targety = h.getcenterYcoordinate(app)+64;
            double forcex = (targetx - posx)*0.03;
            double forcey = (targety - posy)*0.03;
            double d = Calculate.getDistance(posx,posy,targetx,targety);
            if (d<=holewidth && d>0){
                xvel += forcex;
                yvel += forcey;

                if(d<10){
                    if(h.getColor().equals(this.colour) || h.getColor().equals("grey") || this.colour.equals("grey")){
                        app.getscore(this.colour, h.getColor());
                        absorb();
                    }else{
                        app.getscore(this.colour, h.getColor());
                        setOnBoard();
                        setSprite(app.getSprite("ball"+app.colorsprites.get(colour)));
                        initializeVel();
                        if(app.ballinbox.size()==0){
                            setCoordinate(23, 20);
                        }else{
                           setCoordinate((int)app.ballinbox.get(app.ballinbox.size()-1).getXcoordinate()+27, 23); 
                        }
                        app.ballinbox.add(this);
                        // put the ball back to the list, wait to be spawned again.
                    }   
                }else{
                    img.resize((int)(d/holewidth*img.width),(int) (d/holewidth*img.height));
                    setSprite(img);                    
                }                
            }
                

        }

    }
    /**
     * check if the ball is on accelerated tile, if it is, accelerate ball's velosity
     * @param app App
     */
    public void acceleration(App app){
        for(Wall w : app.acceleratedtile){
            int topline = w.getYcoordinate();
            int bottomline = w.getYcoordinate() + w.getSprite().height;
            int leftline = w.getXcoordinate();
            int rightline = w.getXcoordinate() + w.getSprite().height;

            if(this.getXcoordinate() + ballradius >= leftline && 
            this.getXcoordinate() + ballradius <= rightline &&
            this.getYcoordinate() + ballradius >= topline &&
            this.getYcoordinate() + ballradius <= bottomline){
                
                if(w.getAcceleratedDirection() == "up"){
                    yvel -= 0.2;
                }else if(w.getAcceleratedDirection() == "down"){
                    yvel +=0.2;
                }else if(w.getAcceleratedDirection() == "left"){
                    xvel -=0.2;
                }else if(w.getAcceleratedDirection() == "right"){
                    xvel +=0.2;
                }
            }
        }
    }
    /**
     * pass the normal vector of the line segment been collided with, calculate the velocity of the ball
     * @param n normal vector of the line
     */
   public void setvel(double[] n){ 
        double vn = xvel*n[0]+yvel*n[1];
        xvel = xvel - 2*vn*n[0];
        yvel = yvel - 2*vn*n[1];
    }
    /**
     * the movement of the ball on black bar on top when a ball was spawned.
     */
    public void setvelonbar(){
        xcoordinate -=1;
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
     * get X coordinate
     * @return x coordinate
     */
    public double getXcoordinate(){
        return this.xcoordinate;
    }
    /**
     * get Y coordinate
     * @return y coordinate
     */
    public double getYcoordinate(){
        return this.ycoordinate;
    }
    /**
     * get velocity of x 
     * @return velocity of x 
     */
    public double getxvel(){
        return this.xvel;
    }
    /**
     * get velocity of y
     * @return velocity of y
     */
    public double getyvel(){
        return this.yvel;
    }
    /**
     * check absorption and collision them move the ball
     * @param app App
     */
    public void tick(App app){
        checkAbsorption(app);
        checkCollision(app);
        acceleration(app);
        xcoordinate +=xvel;
        ycoordinate +=yvel;            
    }
    /**
     * draw the ball on board
     * @param app App
     */
    public void draw(App app){
        tick(app);
        app.image(this.sprite, (float)xcoordinate,(float)ycoordinate);


    }
    
}
