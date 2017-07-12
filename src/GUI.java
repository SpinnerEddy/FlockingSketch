import controlP5.*;
import processing.core.PApplet;

/**
 * Created by yamashitanozomiryuu on 2017/07/11.
 */
public class GUI extends PApplet{
    private ControlP5 gui;

    private Slider angleSlider;
    private Slider lengthSlider;

    private Flock flock;

    public GUI(PApplet p,Flock f){
        gui = new ControlP5(p);
        angleSlider = gui.addSlider("sAngle")
                         .setPosition(10,10)
                         .setSize(100,10)
                         .setRange(1,360)
                         .setValue(60.0f);
        lengthSlider = gui.addSlider("lLength")
                         .setPosition(10,30)
                         .setSize(100,10)
                         .setRange(5,200)
                         .setValue(50);
        flock = f;
    }

    public void setValue(){
        flock.setSights(angleSlider.getValue(),lengthSlider.getValue());
    }

    public Slider getAngleSlider(){
        return angleSlider;
    }

    public Slider getLengthSlider(){
        return lengthSlider;
    }
}
