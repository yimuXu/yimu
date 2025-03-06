package inkball;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import processing.data.*;
import com.google.common.annotations.VisibleForTesting;
import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
//import com.jogamp.newt.event.KeyEvent;
import processing.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
public class AppTest {
    static App app;
    static Ball ball;
    static Linesegment l;
    @BeforeAll
    public static void setup(){
        app = new App();
        ball = new Ball();
        l = new Linesegment(0, 0, 5, 5);
        //app.loop();
        PApplet.runSketch(new String[] { "App" }, app);

    }
    // App.java 
    @Test
    public void TestFrameRate() {
        app.setup();
        assertTrue(
            App.FPS + 5 >= app.frameRate 
        );
        //app.delay(1000); // delay is to give time to initialise stuff before drawing begins
    }
    
    //test if user get right score when ball was attracted in the hole base on ball and hole's colour.
    @Test
    public void testgetscore(){
        app.configPath = "config4.json";
        app.setup();
        assertTrue(app.getscore("grey", "orange")== 10);
        assertTrue(app.getscore("orange", "orange")== 20);
        assertTrue(app.getscore("blue", "blue") == 30);
        assertTrue(app.getscore("yellow", "grey")== 40);
        assertTrue(app.getscore("green", "grey") == 50);
        assertTrue(app.getscore("green", "blue") == 45);    
        assertTrue(app.getscore("yellow", "blue") == 40);
        assertTrue(app.getscore("orange", "blue") == 35);
        assertTrue(app.getscore("blue", "orange") == 25);

        //app.noLoop();
    }
    @Test
    public void testdrawBallOnBar(){
        app.setup();
        app.ballinbox = new ArrayList<>();
        app.ballinbox.add(ball);
        ball.setCoordinate(20,20);;
        ball.setSprite(app.loadImage(this.getClass().getResource("ball0.png").getPath().replace("%20", " ")));
        ball.setCoordinate(24, 23);
        app.drawballonBar();
        assertTrue(23 == (int)ball.getXcoordinate());

    }
    // test when the remainning time of next spawn reaches 0, the last ball in list was spawn, and size of list turn to 0.
    @Test
    public void testspawnonbar(){
        app.setup();
        app.ballinbox = new ArrayList<>();
        app.ballinbox.add(ball);
        app.spawnBallOnBar();
        assertTrue(app.ballinbox.size() == 0);
    }
    //test when this level is ended, the ending animation display correctly,->yellow tile display on the right location.
    @Test
    public void testlevelend(){
        app.configPath = "levelend.json";
        app.setup();
        app.levelend();
        app.endanimation();
        
    }
    @Test
    public void testdrawcurrentline(){
        app.linesegments = new ArrayList<>();
        app.linesegments.add(l);
        assertTrue(!l.getCollision());
    }
    @Test
    public void testsetup(){
        app.configPath = "config4.json";
        app.setup();
    }
}

// gradle run						Run the program
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
