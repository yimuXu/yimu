package inkball;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import com.jogamp.newt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
public class SpawnerTest {
    static App app;
    static Spawner s;
    @BeforeAll
    public static void setup(){
        app = new App();
        s = new Spawner(2, 2);
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);

    }
    @Test
    public static void testgetcoordinate(){
        app.setup();
        int xc = s.getXcoordinate(app);
        int yc = s.getYcoordinate(app);
        int x1 = s.getx();
        int y1 = s.gety();
        assertEquals(xc,0);
        assertEquals(yc, 0);
        assertEquals(x1, 2);
        assertEquals(y1, 2);
    }
}
