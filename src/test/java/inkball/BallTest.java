package inkball;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import com.jogamp.newt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
public class BallTest {
    static App app;
    static Ball ball;
    static Hole h;
    @BeforeAll
    public static void setup(){
        app = new App();
        ball = new Ball(); 
        h = new Hole(0, 0, "orange");
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
      
    }
    // test basic method
    @Test
    public void setcolourTest(){
        ball.setColour("green");
        assertEquals(ball.getColour(), "green");
    }
    @Test
    public void setcoordinateTest(){
        ball.getOnBoard();
        ball.setCoordinate(20,30);
        assertTrue(ball.getX() == 20);
        assertTrue(ball.getY() == 30);
    }

    @Test
    public void absorptionTest(){
        app.configPath = "absorption.json";
        app.setup();
        double x = ball.getxvel();
        double y = ball.getyvel();
        
        app.draw();
        assertTrue(x != ball.getxvel() || y != ball.getyvel());
        assertTrue(ball.absorption());

    }
    @Test
    public void getdistanceTest(){
        assertEquals(Calculate.getDistance(0,0,3,4), 5);
    }
    //test collision method
    @Test
    public void checkcollisionwithwallTest(){
        app.configPath = "collision.json";
        app.setup();
        app.lines = new ArrayList<>();
        app.linesegments = new ArrayList<>();
        double x = ball.getxvel();
        double y = ball.getyvel();
        ball.checkCollision(app);
        assertTrue(x != ball.getxvel() || y != ball.getyvel()); 
    }
    //test collision with the line.
    @Test
    public void checkcollisionwithline(){
        app.configPath = "collision.json";
        app.setup();
        Linesegment l1 = new Linesegment(40, 0, 40, 10);
        Linesegment l2 = new Linesegment(40, 10, 40, 20);
        app.linesegments.add(l1);
        app.linesegments.add(l2);
        app.lines.add(app.linesegments);
        ball.checkCollision(app);
    }
    //test the mathod calculatevel if it set velocity currectly
    @Test
    public void Testcalculatevel(){

        ball.setCoordinate(3,10);
        ball.initializeVel();
        double xvel = ball.getxvel();
        double yvel = ball.getyvel();
        ball.calculateVel(app, 0, 0, 0, 40);
        assertTrue(xvel != ball.getxvel() || yvel != ball.getyvel());
    }
    
    


}
