import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Created by yamashitanozomiryuu on 2017/06/09.
 */
public class Vehicle extends Particle {

    private PVector sumVector;
    private int count;

    private float sepWeight;
    private float aliWeight;
    private float cohWeight;

    private float lineLength;

    private float sightAngle;

    private int networkNum;

    private boolean arcDisplayFlag;

//    private ParticleSystem ps;

    public Vehicle(PApplet p, PVector pos,boolean flag) {
        super(p, pos,flag);
        sumVector = new PVector();
        count = 0;
        sepWeight = random(2.5f,10f);
        aliWeight = random(0.5f,2.5f);
        cohWeight = random(0.5f,2.5f);
        lineLength = 50;
        sightAngle = 60;
//        networkNum = (int)random(1,3);
        networkNum = 2;
        arcDisplayFlag = flag;
//        ps = new ParticleSystem(p,pos);
    }

    private PVector seek(PVector target) {
        PVector desired = PVector.sub(target, position);
        desired.normalize();
        desired.mult(maxSpeed);
        PVector steer = PVector.sub(desired, velocity);
        steer.limit(maxForce);
        return steer;
    }


    private PVector separate(ArrayList<Vehicle> vehicles){
        float desiredSeparation = 35;
        PVector steer = new PVector();
        count = 0;
        for(Vehicle v : vehicles){
            float d = PVector.dist(this.position,v.position);
            if((d > 0) && (d < desiredSeparation)){
                PVector diff = PVector.sub(this.position,v.position);
                diff.normalize();
                diff.div(d);
                steer.add(diff);
                count++;
            }
        }

        if(count > 0){
            steer.div(count);

        }

        if(steer.mag() > 0){
            steer.normalize();
            steer.mult(maxSpeed);
            steer.sub(velocity);
            steer.limit(maxForce);
        }
        return steer;
    }

    public void applyBehaviors(ArrayList<Vehicle> vehicles){
        PVector sep = separate(vehicles);
        PVector ali = align(vehicles);
        PVector coh = cohesion(vehicles);

        sep.mult(sepWeight);
        ali.mult(aliWeight);
        coh.mult(cohWeight);

        applyForce(sep);
        applyForce(ali);
        applyForce(coh);
    }

    private PVector cohesion(ArrayList<Vehicle> vehicles){
        float desiredSeparation = 50;
        sumVector = new PVector();
        count = 0;
        for(Vehicle v : vehicles){
            float d = PVector.dist(this.position,v.position);
                if((d > 0) && (d < desiredSeparation)) {
                    sumVector.add(v.position);
                    count++;
                }
        }
        if(count > 0){
            sumVector.div(count);
            return seek(sumVector);
        }else{
            return new PVector(0,0);
        }
    }

    private PVector align(ArrayList<Vehicle> vehicles){
        float neighborDist = 50;
        count = 0;
        sumVector = new PVector(0,0);
        for(Vehicle v : vehicles){
            float d = PVector.dist(this.position,v.position);
                if((d > 0) && (d < neighborDist)) {
                    sumVector.add(v.velocity);
                    count++;
                }
        }
        if(count > 0) {
            sumVector.div(count);
            sumVector.normalize();
            sumVector.mult(maxSpeed);
            PVector steer = PVector.sub(sumVector, velocity);
            steer.limit(maxForce);
            return steer;
        }else{
            return new PVector(0,0);
        }
    }

    public void connect(ArrayList<Vehicle> vehicles){
        if(arcDisplayFlag) {
            p.fill(hu, 255, 255, 20);
            p.colorMode(HSB);
            p.stroke(hu, 255, 255, 255);
            p.pushMatrix();
            p.translate(this.position.x, this.position.y);
            p.rotate(this.velocity.heading());
            p.arc(0, 0, lineLength * 2, lineLength * 2, -radians(sightAngle / 2), radians(sightAngle / 2));
            p.popMatrix();
        }
//        p.ellipseMode(CENTER);
//        p.ellipse(this.position.x,this.position.y,lineLength,lineLength);
        if(networkNum == 0){
            groupRect(vehicles);
        }
        else if(networkNum == 1){
            groupCircle(vehicles);
        }
        else{
            groupLineAngle(vehicles);
        }
    }

    public void groupRect(ArrayList<Vehicle> vehicles){
        int number = vehicles.indexOf(this);
        for(int i = number; i < vehicles.size(); i++){
            float d = PVector.dist(this.position,vehicles.get(i).position);
            if(d <= lineLength){
                p.colorMode(HSB);
                p.stroke(255*(d/lineLength),255,255,255);
                p.rectMode(CENTER);
                p.noFill();
                p.rect((this.position.x+vehicles.get(i).position.x)/2,(this.position.y+vehicles.get(i).position.y)/2,d,d);
            }
        }
    }

    public void groupRectAngle(ArrayList<Vehicle> vehicles){
        for(Vehicle v : vehicles){
            float d = PVector.dist(this.position,v.position);
            float angle = PVector.angleBetween(PVector.sub(v.position,this.position),this.velocity);
            if(d <= lineLength && degrees(angle) <= sightAngle){
                p.colorMode(HSB);
                p.stroke(255*(d/lineLength),255,255,255);
                p.rectMode(CENTER);
                p.noFill();
                p.rect((this.position.x+v.position.x)/2,(this.position.y+v.position.y)/2,d,d);
            }
        }
    }

    public void groupLine(ArrayList<Vehicle> vehicles){
        int number = vehicles.indexOf(this);
        for(int i = number; i < vehicles.size(); i++){
            float d = PVector.dist(this.position,vehicles.get(i).position);
            float angle = PVector.angleBetween(PVector.sub(vehicles.get(i).position,this.position),this.velocity);
            if(d <= lineLength && degrees(angle) <= sightAngle){
                p.colorMode(HSB);
                p.stroke(255*(d/lineLength),255,255,255);
                p.noFill();
                p.line(this.position.x,this.position.y,vehicles.get(i).position.x,vehicles.get(i).position.y);
            }
        }
    }

    public void groupLineAngle(ArrayList<Vehicle> vehicles){
        for(Vehicle v : vehicles){
            float d = PVector.dist(this.position,v.position);
            float angle = PVector.angleBetween(PVector.sub(v.position,this.position),this.velocity);
            if(d <= lineLength && degrees(angle) <= sightAngle){
                p.colorMode(HSB);
                p.stroke(255*(d/lineLength),255,255,255);
                p.noFill();
                p.line(this.position.x,this.position.y,v.position.x,v.position.y);
            }
        }
    }

    public void groupCircle(ArrayList<Vehicle> vehicles){
        int number = vehicles.indexOf(this);
        for(int i = number; i < vehicles.size(); i++){
            float d = PVector.dist(this.position,vehicles.get(i).position);
            if(d <= lineLength){
                p.colorMode(HSB);
                p.stroke(255*(d/lineLength),255,255,255);
                p.noFill();
                p.ellipseMode(CENTER);
                p.ellipse((this.position.x+vehicles.get(i).position.x)/2,(this.position.y+vehicles.get(i).position.y)/2,d,d);
            }
        }
    }

    public void groupCircleAngle(ArrayList<Vehicle> vehicles){
        for(Vehicle v : vehicles){
            float d = PVector.dist(this.position,v.position);
            float angle = PVector.angleBetween(PVector.sub(v.position,this.position),this.velocity);
            if(d <= lineLength && degrees(angle) <= sightAngle){
                p.colorMode(HSB);
                p.stroke(255*(d/lineLength),255,255,255);
                p.noFill();
                p.ellipseMode(CENTER);
                p.ellipse((this.position.x+v.position.x)/2,(this.position.y+v.position.y)/2,d,d);
            }
        }
    }

    public void setWeight(){
        sepWeight = random(2.5f,10f);
        aliWeight = random(0.5f,8f);
        cohWeight = random(0.5f,8f);
//        networkNum = (int)random(3);
    }

    public void setSight(float sight,float len){
        lineLength = len;
        sightAngle = sight;
    }

    public void angleDebug(boolean flag){
       arcDisplayFlag = flag;
    }


}
