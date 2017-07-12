import processing.core.*;

import java.util.*;

/**
 * Created by yamashitanozomiryuu on 2017/06/30.
 */
public class Flock extends PApplet{

    PApplet p;
    private ArrayList<Vehicle> boids;

    private GUI g;


    public Flock(PApplet p){
        this.p = p;
        boids = new ArrayList<Vehicle>();
    }

    public void run(){
        for(Vehicle v : boids){
            v.applyBehaviors(boids);
            v.run();
            v.connect(boids);
        }
    }

    public void addVehicle(PApplet p,PVector pos,boolean flag){
        Vehicle v = new Vehicle(p,pos,flag);
        boids.add(v);
    }

    public void deleteVehicle(){
        if(boids.size() > 0) {
            Vehicle v = boids.get(0);
            boids.remove(v);
        }
    }

    public void setWeights(){
        for(Vehicle v : boids){
            v.setWeight();
        }
    }

    public void debug(boolean flag){
        for(Vehicle v : boids){
            v.vehicleDisplayChange(flag);
            v.angleDebug(flag);
        }
    }

    public void setSights(float sight,float len){
        for(Vehicle v : boids){
            v.setSight(sight,len);
        }
    }

}
