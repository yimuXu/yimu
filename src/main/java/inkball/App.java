package inkball;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 64;
    public static int WIDTH = 576; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 1;

    public static final int FPS = 30;

    public String configPath;
    public JSONObject json;
    public JSONArray configs;
    public JSONObject configs1;
    public JSONObject configs2;
    

    public static Random random = new Random();
    public Wall[][] board;
    public char[][] boardlayout;
    public HashMap<String, PImage> sprites = new HashMap<>();
    public HashMap<String, String> colorsprites = new HashMap<>();
    
    public ArrayList<Ball> balls;
    public ArrayList<Ball> ballinbox;
    public ArrayList<Spawner> spawners;
    public ArrayList<Hole> holes;
    public ArrayList<Wall> acceleratedtile;

    public ArrayList<ArrayList<Linesegment>> lines; 
    public ArrayList<Linesegment> linesegments;
    
    private int starttime;
    private int time;
    private int spawnstarttime;
    

    private  int currenttime;// show the time at this frame;
    private  int pauseat;// time of space is pressed.
    private  int remaintime; // left time
    private  int remaintimeofspawn;
    private  int finaltime;// used to record the time number when this level is passed.

    private int score = 0;
    private  int thislevelscore; // record the socre gained in this level, using when user restarted the game.
    private  int greyball_rise;
    private int orangeball_rise;
    private int blueball_rise;
    private int greenball_rise;
    private int yellowball_rise;
    private int greyball_down;
    private int orangeball_down;
    private int blueball_down;
    private int greenball_down;
    private int yellowball_down;
    private int spawn_interval;
    private float score_increase_from_hole_capture_modifier;
    private float score_decrease_from_wrong_hole_modifier;

    private boolean pause;// for pause the game
    private boolean gameover;// if time is up


    private boolean pass; // if all the balls were absorbed, pass this level successfully, move to next level.
    private int a;// count the localion of the yellow tile at the end of this level.
    private int b;

    private int level = 0; // store the current level of board
    /**
     * this method use a hashmap to store the image name and image location, user can easily load image by using the name of image.
     * @param s the name of image.
     * @return return a PImage of corresponding key word.
     */
    public PImage getSprite(String s) {
        PImage result = sprites.get(s);
        if (result == null) {
            result = loadImage(this.getClass().getResource(s+".png").getPath().replace("%20", " "));
            sprites.put(s, result);
        }
        return result;
    }
	
	/**
     * 
     */

    public App() {
        this.configPath = "config.json";
        
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     * initialize all the array list that we need to use in the programme.
     * put color and corresponding number in a map.
     * read config json file, get all the data, create new ball, hole,spawner, wall and linesegment instances.
     */
	@Override
    public void setup() {
        frameRate(FPS);
        stroke(0);
        strokeWeight(10);
        lines = new ArrayList<>();
        balls = new ArrayList<>();
        ballinbox = new ArrayList<>();
        spawners = new ArrayList<>();
        holes = new ArrayList<>();
        acceleratedtile = new ArrayList<>();
        pause = false;
        gameover = false;
        pass = false;
        thislevelscore = 0;

        colorsprites.put("grey","0");
        colorsprites.put("orange","1");
        colorsprites.put("blue","2");
        colorsprites.put("green","3");
        colorsprites.put("yellow","4");
        //read config.json file
		json = loadJSONObject(configPath);
        configs = json.getJSONArray("levels");
        JSONObject config = configs.getJSONObject(level);
            //read layout of the board
        String filename = config.getString("layout");
        spawn_interval = config.getInt("spawn_interval");
        score_increase_from_hole_capture_modifier = config.getInt("score_increase_from_hole_capture_modifier");
        score_decrease_from_wrong_hole_modifier = config.getInt("score_decrease_from_wrong_hole_modifier");

        // get timer
        time = config.getInt("time");
        starttime = millis();
        spawnstarttime = millis();
        textAlign(CENTER, CENTER);
        textSize(20);
        fill(0);

        // initailze score and get score;
        
        configs1 = json.getJSONObject("score_increase_from_hole_capture");
        greyball_rise = configs1.getInt("grey");
        orangeball_rise = configs1.getInt("orange"); 
        blueball_rise = configs1.getInt("blue");
        greenball_rise = configs1.getInt("green");
        yellowball_rise = configs1.getInt("yellow");
        configs2 = json.getJSONObject("score_decrease_from_wrong_hole");
        greyball_down = configs1.getInt("grey");
        orangeball_down = configs1.getInt("orange");
        blueball_down = configs1.getInt("blue");
        greenball_down = configs1.getInt("green");
        yellowball_down = configs1.getInt("yellow");
        //initialize an array, store character from the layout in it.
        int rowOfLayout = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            line = reader.readLine();
            reader.close();
            BufferedReader reader1 = new BufferedReader(new FileReader(filename));
            boardlayout = new char[line.length()][line.length()];
            this.board = new Wall[line.length()][line.length()];            
            while ((line = reader1.readLine()) != null&&rowOfLayout<18) {
                boardlayout[rowOfLayout]= line.toCharArray();
                rowOfLayout++;  
            }
            reader1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    
        //load all the images.
        for (int i = 0; i < 5; i++) {
            getSprite("wall"+String.valueOf(i));
        }
        for (int i = 0; i < 5; i++) {
            getSprite("ball"+String.valueOf(i));
        }
        for (int i = 0; i < 5; i++) {
            getSprite("hole"+String.valueOf(i));
        }
        getSprite("tile");
        getSprite("entrypoint");
        for (int i =0; i<this.board.length; i++){
            for (int i1=0;i1<this.board[i].length;i1++) {
                this.board[i][i1]= new Wall(i1,i);
            }
        }
       //create new tiles and put them in an array.

        for (int i=0; i<boardlayout.length; i++) {
            for (int i1 = 0; i1 < boardlayout[i].length; i1++) {
                if (boardlayout[i][i1]=='X') {
                    this.board[i][i1].setSprite(getSprite("wall0"));
                    this.board[i][i1].setWall();
                    this.board[i][i1].setColour("grey");

                } else if (boardlayout[i][i1]=='1') {
                    if (i1 == 0 || i1 == boardlayout[i].length-1) {
                        this.board[i][i1].setSprite(getSprite("wall1"));
                        this.board[i][i1].setWall();
                        this.board[i][i1].setColour("orange");
                    } else if (boardlayout[i][i1-1]!='H'&&boardlayout[i][i1-1]!='B'){
                        this.board[i][i1].setSprite(getSprite("wall1"));
                        this.board[i][i1].setWall();
                        this.board[i][i1].setColour("orange");
                    } else {
                        this.board[i][i1].setSprite(getSprite("tile"));

                    }
                } else if (boardlayout[i][i1]=='2') {
                    if (i1 == 0 || i1 == boardlayout[i].length-1) {
                        this.board[i][i1].setSprite(getSprite("wall2"));
                        this.board[i][i1].setWall();
                        this.board[i][i1].setColour("blue");
                    } else if (boardlayout[i][i1-1] !='H'&&boardlayout[i][i1-1]!='B'){
                        this.board[i][i1].setSprite(getSprite("wall2"));
                        this.board[i][i1].setWall();
                        this.board[i][i1].setColour("blue");
                    } else {
                        this.board[i][i1].setSprite(getSprite("tile"));
                    }
                } else if (boardlayout[i][i1 ]== '3') {
                    if (i1 == 0 || i1 == boardlayout[i].length-1) {
                        this.board[i][i1].setSprite(getSprite("wall3"));
                        this.board[i][i1].setColour("green");
                        this.board[i][i1].setWall();
                    } else if (boardlayout[i][i1-1]!='H'&&boardlayout[i][i1-1]!='B'){
                        this.board[i][i1].setSprite(getSprite("wall3"));
                        this.board[i][i1].setWall();
                        this.board[i][i1].setColour("green");
                    } else {
                        this.board[i][i1].setSprite(getSprite("tile"));
                    }
                } else if (boardlayout[i][i1]=='4') {
                    if (i1==0 || i1 ==boardlayout[i].length-1) {
                        this.board[i][i1].setSprite(getSprite("wall4"));
                        this.board[i][i1].setWall();
                        this.board[i][i1].setColour("yellow");
                    } else if (boardlayout[i][i1-1]!='H'&&boardlayout[i][i1-1]!='B'){
                        this.board[i][i1].setSprite(getSprite("wall4"));
                        this.board[i][i1].setWall();
                        this.board[i][i1].setColour("yellow");
                    } else {
                        this.board[i][i1].setSprite(getSprite("tile"));
                    }
                    
                } else if (boardlayout[i][i1]=='S') {
                    this.board[i][i1].setSprite(getSprite("entrypoint"));
                    if(!this.board[i][i1].ifHasSpawner()){
                        Spawner s = new Spawner(i1, i);
                        this.board[i][i1].setSpawner();
                        spawners.add(s);                        
                    }
                } else if (boardlayout[i][i1]=='U') {
                    this.board[i][i1].setSprite(getSprite("up"));
                    this.board[i][i1].setAcceleratedTile();
                    this.board[i][i1].setAcceleratedDirection("up");   
                    acceleratedtile.add(this.board[i][i1]);   
                    
                }else if (boardlayout[i][i1]=='D') {
                    this.board[i][i1].setSprite(getSprite("down"));
                    this.board[i][i1].setAcceleratedTile();
                    this.board[i][i1].setAcceleratedDirection("down"); 
                    acceleratedtile.add(this.board[i][i1]);   
                } else if (boardlayout[i][i1]=='L') {
                    this.board[i][i1].setSprite(getSprite("left"));
                    this.board[i][i1].setAcceleratedTile();        
                    this.board[i][i1].setAcceleratedDirection("left");                  
                    acceleratedtile.add(this.board[i][i1]);   
                } else if (boardlayout[i][i1]=='R') {
                    this.board[i][i1].setSprite(getSprite("right"));
                    this.board[i][i1].setAcceleratedTile(); 
                    this.board[i][i1].setAcceleratedDirection("right"); 
                    acceleratedtile.add(this.board[i][i1]);   
                } else {
                    this.board[i][i1].setSprite(getSprite("tile"));
                }
                this.board[i][i1].draw(this);
            }
        }
        // draw holes
        for (int i=0; i<boardlayout.length; i++ ) {
            for (int i1=0; i1<boardlayout[i].length; i1++) {
                if (boardlayout[i][i1]=='H') {
                    char colnum = boardlayout[i][i1+1];//number of color;

                    this.board[i][i1].setSprite(getSprite("hole"+String.valueOf(boardlayout[i][i1+1])));
                    Hole h;
                    if (colnum == '0') {
                        h = new Hole(i1, i,"grey");
                        h.setSprite(getSprite("hole"+String.valueOf(boardlayout[i][i1+1])));
                        holes.add(h);
                        this.board[i][i1].draw(this);                        
                    } else if (colnum == '1') {
                        h = new Hole(i1, i,"orange");
                        h.setSprite(getSprite("hole"+String.valueOf(boardlayout[i][i1+1])));
                        holes.add(h);
                        this.board[i][i1].draw(this); 
                    } else if (colnum == '2') {
                        h = new Hole(i1, i,"blue");
                        h.setSprite(getSprite("hole"+String.valueOf(boardlayout[i][i1+1])));
                        holes.add(h);
                        this.board[i][i1].draw(this); 
                    } else if (colnum == '3') {
                        h = new Hole(i1, i,"green");
                        h.setSprite(getSprite("hole"+String.valueOf(boardlayout[i][i1+1])));
                        holes.add(h);
                        this.board[i][i1].draw(this); 
                    } else if (colnum == '4') {
                        h = new Hole(i1, i,"yellow");
                        h.setSprite(getSprite("hole"+String.valueOf(boardlayout[i][i1+1])));
                        holes.add(h);
                        this.board[i][i1].draw(this); 
                    }
                }
            }
        }
        //create initial ball.        
        for (int i=0; i<boardlayout.length; i++) {
            for (int i1=0; i1<boardlayout[i].length; i1++) {
                if (boardlayout[i][i1]=='B'&&!this.board[i][i1].ifHasFirstBall()) {
                    this.board[i][i1].setFirstBall();
                    Ball ball = new Ball();
                    ball.initializeVel();
                    ball.setOnBoard();
                    ball.setCoordinate(i1, i);
                    balls.add(ball);
                    if (boardlayout[i][i1+1]=='0'){
                        ball.setColour("grey");
                    } else if (boardlayout[i][i1+1]=='1') {
                        ball.setColour("orange");
                    } else if (boardlayout[i][i1+1]=='2') {
                        ball.setColour("blue");
                    } else if (boardlayout[i][i1+1]=='3') {
                        ball.setColour("green");
                    } else if (boardlayout[i][i1+1]=='4') {
                        ball.setColour("yellow");
                    }
                    ball.setSprite(getSprite("ball"+String.valueOf(boardlayout[i][i1+1])));
                    

                }
                
            }
        }
        // put the ball from json file into a new list that store the ball wait to be spawned.
        JSONArray colours = config.getJSONArray("balls");
        for (int i = 0; i < colours.size();i++) {
            Ball b = new Ball();
            b.initializeVel();
            ballinbox.add(b);

            if (colours.get(i).equals("grey")) {
                b.setSprite(getSprite("ball0"));
            } else if (colours.get(i).equals("orange")) {
                b.setSprite(getSprite("ball1"));
            } else if (colours.get(i).equals("blue")) {
                b.setSprite(getSprite("ball2"));
            } else if (colours.get(i).equals("green")) {
                b.setSprite(getSprite("ball3"));
            } else if (colours.get(i).equals("yellow")) {
                b.setSprite(getSprite("ball4"));
            }
            b.setColour(colours.getString(i));
            balls.add(b);
        }
        linesegments = new ArrayList<>();
        //display the ball that are about to be spawned.
        if (ballinbox.size()!=0) {
            for ( int i = 0; i<ballinbox.size();i++) {
                ballinbox.get(i).setCoordinate(23+i*30,23);
            }            
        }

    }
    /**
     * implenment ending animation.
     */
    public void endanimation() {
        image(getSprite("wall4"),board[a][b].getXcoordinate(),board[a][b].getYcoordinate());
        image(getSprite("wall4"),board[17-a][17-b].getXcoordinate(),board[17-a][17-b].getYcoordinate());
    }
    /**
     * Receive key pressed signal from the keyboard.
     * if key is space, change the statement of "pause",if key is "r", restart the game.
     */
    char lastpressedkey;
    @Override
    public void keyPressed(KeyEvent event) {
        
        if (key == ' ') {
            if (pause) {
                pause = false;
                starttime = millis()-(time*1000 - pauseat);
                spawnstarttime = millis() - (spawn_interval * 1000 - remaintimeofspawn);

            } else {
                pause = true;
                textSize(30);
                text("***PAUSED***",300,30);
                pauseat = remaintime;
            }
        }
        if (key == 'r'||key == 'R') {
            if (gameover) {
                level = 0;
                setup();
                score = 0;                
            } else {
                score -= thislevelscore;
                setup();
    
            }

        }
        lastpressedkey = key;
    }

    public char getLastPressedKey() {
        return lastpressedkey;
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased() {

    }
    private int startx;
    private int starty;
    /**
     * receive mouse pressed signal from the mouse.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // create a new player-drawn line object
        if (mouseButton == LEFT) {
            startx = mouseX;
            starty = mouseY;
            linesegments = new ArrayList<>();            
        }

    }
    /**
     * receive mouse dragging signal from the mouse.
     * keep creating new linesegment instances.
     */
	@Override
    public void mouseDragged(MouseEvent e) {
        // add line segments to player-drawn line object if left mouse button is held
        if (mouseButton == LEFT) {
            Linesegment line = new Linesegment(startx,starty,mouseX,mouseY);
            linesegments.add(line);
            //line(startx,starty,mouseX,mouseY);
            startx = mouseX;
            starty = mouseY;            
        }
    }
    /**
     * receive mouse release signal from the mouse.
     * add new linesegment instances into list.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        lines.add(linesegments);

    }


    
    /**
     * calculate the socre based on the color of the hole and the ball.
     * @param ballcolor the color of the ball
     * @param holecolor the color of the hole that the ball get in.
     * @return score
     */
    public int getscore(String ballcolor,String holecolor){

        if (ballcolor.equals("grey")) {
            score += greyball_rise * score_increase_from_hole_capture_modifier;
            thislevelscore += greyball_rise * score_increase_from_hole_capture_modifier;
        } else if (ballcolor.equals("orange")) {
            if (ballcolor.equals(holecolor) || holecolor.equals("grey")) {
            score += orangeball_rise * score_increase_from_hole_capture_modifier;
            thislevelscore += orangeball_rise * score_increase_from_hole_capture_modifier;
            } else {
                score -= orangeball_down * score_decrease_from_wrong_hole_modifier;
                thislevelscore -= orangeball_down * score_decrease_from_wrong_hole_modifier;
            }
        } else if (ballcolor.equals("blue")) {
            if (ballcolor.equals(holecolor) || holecolor.equals("grey")) {
            score += blueball_rise * score_increase_from_hole_capture_modifier;
            thislevelscore += blueball_rise * score_increase_from_hole_capture_modifier;
            } else {
                score -= blueball_down * score_decrease_from_wrong_hole_modifier;
                thislevelscore -= blueball_down * score_decrease_from_wrong_hole_modifier;
            }
        } else if (ballcolor.equals("green")) {
            if (ballcolor.equals(holecolor) || holecolor.equals("grey")) {
            score += greenball_rise * score_increase_from_hole_capture_modifier;
            thislevelscore += greenball_rise * score_increase_from_hole_capture_modifier;

            } else {
                score -= greenball_down * score_decrease_from_wrong_hole_modifier;
                thislevelscore -= greenball_down * score_decrease_from_wrong_hole_modifier;
            }
        } else if (ballcolor.equals("yellow")) {
            if (ballcolor.equals(holecolor) || holecolor.equals("grey")) {
            score += yellowball_rise * score_increase_from_hole_capture_modifier;
            thislevelscore += yellowball_rise * score_increase_from_hole_capture_modifier;
            } else {
                score -= yellowball_down * score_decrease_from_wrong_hole_modifier;
                thislevelscore -= yellowball_down * score_decrease_from_wrong_hole_modifier;
            }
        }
        return score;
    }
    /**
     * use for loop to draw all the lines that not be collided
     */
    public void drawAllline() {
        if(lines.size() !=0 ){
            for (ArrayList<Linesegment> line : lines) {//entire line
                if(linesegments.size() != 0){
                    for (Linesegment l : line) {//line segment   
                    
                        if (!l.getCollision()) {
                            stroke(0);
                            strokeWeight(10);
                            line(l.getstartx(),l.getstarty(),l.getendx(),l.getendy());

                        }
                        
                    }                    
                }
            
            }            
        }

    }
    /**
     * draw current line that haven been added into lines list.
     */
    public void drawCurrentline(){
        if(linesegments.size() != 0){
            for (Linesegment l : linesegments) {//line segment 
                if (!l.getCollision()) {
                    stroke(0);
                    strokeWeight(10);
                    line(l.getstartx(),l.getstarty(),l.getendx(),l.getendy());
                }  
                
            }            
        }

    }
    /**
     * check if there are balls on bar, and balls on board, 
     * if there are no ball on bar and on board
     * display ending animation.
     */
    public void levelend(){
        if (ballinbox.size() == 0) {
            int countabsorbedball = 0;
            for (Ball b: balls) {
                if (b.absorption()) {
                    countabsorbedball ++;
                }
            }
            // display the ending animation by check if there is no ball on board and on bar.
            if (countabsorbedball == balls.size()) {
                pass = true;
                if ( frameCount%2==0) {
                    if (finaltime != 0) {
                        finaltime--;
                        score++;
                        if (a==0 && b<17) {
                            b++;
                        } else if (b==17 && a<17) {
                            a++;
                        } else if (b>0 && a==17 ) {
                            b--;
                        } else if (b==0 && a>0) {
                            a--;
                        }                      
                    }                  
                }
                endanimation();
                if (finaltime == 0) {
                    if (level == configs.size()-1) {
                        gameover = true;
                        fill(0);
                        textSize(40);
                        text("==ENDED==", 300 , 50);
                    } else {
                        level++;
                        setup();
                    }
                    

                }
            }
        }
    }
    /**
     * draw balls on bar and handling movement of balls on bar
     */
    public void drawballonBar() {
        fill(0);
                noStroke();
                rect(20,20,150,30);
                //display the ball on board
                if(balls.size() != 0){
                    for (Ball b: balls) {
                        if (b.getOnBoard()&&!b.absorption()) {
                            b.draw(this);
                        }
                    }                    
                }

                
                //control the ball in the topbar moving leftward when the first ball was spawned.
                if (ballinbox.size()!=0) {
                    int x = 0;
                    if (ballinbox.get(0).getXcoordinate()>23) {
                        for (Ball b : ballinbox) {         
                            b.setvelonbar();
                            image(b.getSprite(), (float)b.getXcoordinate()-x,(float) b.getYcoordinate());
                            x++;
                        }
                    } else {
                        for (Ball b : ballinbox) { 
                            image(b.getSprite(), (float)b.getXcoordinate(),(float) b.getYcoordinate());
                        }
                    }                      
                }
                //draw a rectangle to cover the ball outside of black bar.
                fill(255);
                noStroke();
                rect(170,20,150,30);
    }
    /**
     * display the score
     */
    public void drawScore() {
        fill(0);
        textSize(20);
        text("Score : "+score,500,30);

    }
    /**
     * when spawning time is up, randomly choose a spawner to spawn the ball on board.
     */
    public void spawnBallOnBar() {
        if (ballinbox.size()!=0) {
            if (remaintimeofspawn <=0) {
                spawnstarttime = millis();
                if (ballinbox.size()!=0) {
                    ballinbox.get(0).setOnBoard(); 
                    int spawnlocation = random.nextInt(spawners.size());
                    ballinbox.get(0).setCoordinate(spawners.get(spawnlocation).getx(), spawners.get(spawnlocation).gety());
                    ballinbox.remove(0);

                }
            }
        }
    }
    /**
     * display the interval of next spawn
     */
    public void drawspawntime(){
        if (!pause) {
            int currenttime1 = millis();
            int interval = currenttime1 - spawnstarttime;
            remaintimeofspawn = spawn_interval*1000 - interval;
        }
        if (ballinbox.size() != 0) {
            fill(0);
            textSize(20);
            String timeString = nf((float)remaintimeofspawn/1000, 0, 1);
            text(timeString,210,35);                    
        }        
    }
    /**
     * display the remainning game time
     */
    public void drawgametime(){
        currenttime = millis();            
        int passingtime = currenttime-starttime;
        remaintime = time*1000 - passingtime;
        if (!pass) {
            finaltime = remaintime/1000;
        }
        if (!pass) {
            fill(0);
            textSize(20);
            text("Time : " + remaintime/1000, 500,50);                    
        } else {
            fill(0);
            textSize(20);
            text("Time : " + finaltime, 500,50);
        }
        if (remaintime/1000 == 0) {
            textSize(50);
            text("===TIME'S UP===",300,30);
            gameover = true;
        } 
    }
    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        if (!gameover) {
            if (!pause) {
                background(255,255,255);//white background
                //display Board for current level:
                for(int i=0; i<boardlayout.length; i++){
                    for(int i1=0; i1<boardlayout[i].length; i1++){
                        this.board[i][i1].draw(this); 
                    }
                }
                //print the hole
                for (Hole h: holes) {
                    h.draw(this);
                }
                drawAllline();//draw all the line that have not be collided with               
                drawballonBar();// display the ball in the bar.            
                drawgametime();//calculate remainning time
                drawspawntime(); //calculate the sprawn interval and display remainning time.           
                spawnBallOnBar();//spawn the first ball on bar when countdown time reach 0.
                drawScore();//draw the score
                levelend();//display game end message
            }
            drawCurrentline(); //print the current line that is being draw
  
        }
    }


    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }

}
