import controlP5.ControlP5;
import processing.core.*;
import java.util.ArrayList;

/**
 * Created by yamashitanozomiryuu on 2017/06/30.
 */

public class MainSketch extends PApplet{

    private Flock flock;

    private GUI gui;

    private boolean debugFlag;

    public void settings(){
        size(600,600);
    }

    public void setup(){
        background(255);
        flock = new Flock(this);
        gui = new GUI(this,flock);
        debugFlag = true;
        for(int i = 0; i < 5; i++){
            flock.addVehicle(this,new PVector(random(width),random(height)),debugFlag);
        }
    }

    public void draw(){
        //background(255);
        fadeBackground();
        flock.run();
        gui.setValue();
        if(mousePressed && (mouseX > gui.getAngleSlider().getWidth() && mouseY > gui.getLengthSlider().getHeight())){
            flock.addVehicle(this,new PVector(mouseX,mouseY),debugFlag);
        }
        if(frameCount % 300 == 0){
            //flock.setWeights();
            System.out.println("Change");
        }
    }

    public void keyPressed(){
        if(key == ENTER){
            debugFlag = !debugFlag;
            flock.debug(debugFlag);
        }else if(key == BACKSPACE){
            println("delete");
            flock.deleteVehicle();
        }else if(key == TAB){
            saveFrame("SaveData");
        }

    }

    public void fadeBackground(){
        rectMode(CORNER);
        noStroke();
        colorMode(RGB);
        fill(255,90);
        rect(0,0,width,height);
    }

    public static void main(String[] args){
        PApplet.main("MainSketch");
    }

}
