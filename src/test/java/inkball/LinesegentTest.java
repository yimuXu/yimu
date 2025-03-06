package inkball;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import com.jogamp.newt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
public class LinesegentTest {
    static Linesegment l;
    static App app;
    @BeforeAll
    public static void setup(){
        app = new App();
        l = new Linesegment(0, 0, 5, 5);
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
    }
    @Test
    public void midpointTest(){
        double[] result = Calculate.getMidPoint(0,0,5,5);
        assertTrue(result[0] == 2.5 && result[1] == 2.5);
    }
    //test set normal vectors return a dimention double array
    @Test
    public void NormalsetnormalvectorTest(){
        

        double[][] result = Calculate.getNormalVectors(0,0,5,5);
        assertTrue(result[0][0] == 1/Math.sqrt(2) || result[0][0] == -1/Math.sqrt(2));
    }
    @Test
    public void EdgesetnormalvectorTest(){
        l = new Linesegment(0, 0, 0, 0);
        double[][] result = Calculate.getNormalVectors(0,0,0,0);
        assertTrue(result[0][0] == 0 || result[0][0] == 0);
    }
    @Test
    public void testgetmethod(){
        float x1 = l.getstartx();
        float y1 = l.getstarty();
        float x2 = l.getendx();
        float y2 = l.getendy();
        assertTrue(x1 == 0);
        assertTrue(y1 == 0);
        assertTrue(x2 == 5);
        assertTrue(y2 == 5);
    }

}
