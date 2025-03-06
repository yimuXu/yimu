package inkball;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import com.jogamp.newt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
public class WallTest {
    static App app;
    static Wall wall;
    @BeforeAll
    public static void setup(){
        app = new App();
        wall = new Wall(0, 0);
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);

    }
    @Test
    public void midpointTest(){
        double[] result = Calculate.getMidPoint(0, 0, 0, 2);
        assertEquals(result[0],0);
        assertEquals(result[1],1.0);
    }
    @Test
    public void normalvectorTest(){
        double[][] result = Calculate.getNormalVectors(0, 0, 0, 5.0);
        assertTrue(result[0][0] == -1.0 || result[1][0] == 1.0 );
    }
}
