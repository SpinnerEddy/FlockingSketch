import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by yamashitanozomiryuu on 2017/06/12.
 */
public class Particle extends PApplet {
    protected PApplet p;
    protected PVector acceleration;
    protected PVector velocity;
    protected PVector position;
    protected float maxSpeed;
    protected float maxForce;

    protected float hu;
    protected float mass;
    protected float r;

    protected boolean vehicleDisplayFlag;

    protected float lifeSpan;
    //protected ArrayList<PVector> orbits;

    public Particle(){}

    public Particle(PApplet p, PVector pos,boolean flag){
        this.p = p;
        position = pos;
        velocity = PVector.random2D();
        acceleration = new PVector(0,0);
        maxForce = 0.05f;
        maxSpeed = 3;

        //orbits = new ArrayList<PVector>();

        vehicleDisplayFlag = flag;

        hu = random(255);
        mass = 1;
        r = 4;
        lifeSpan = 255f;
    }

    public void run(){
        update();
        if(vehicleDisplayFlag) {
            display();
        }
        checkEdges();
    }

    public void update(){
        velocity.add(acceleration);
        position.add(velocity);
        acceleration.mult(0);
        //recordingPosition();
    }

    public void display(){
        float theta = velocity.heading() + PI/2;
        p.colorMode(HSB);
        p.fill(hu,255,255,255);
        p.stroke(0);
//        p.ellipse(position.x,position.y,r*2,r*2);
        p.pushMatrix();
        p.translate(position.x,position.y);
        p.rotate(theta);
        p.beginShape();
        p.vertex(0,-r*2);
        p.vertex(-r,r*2);
        p.vertex(r,r*2);
        p.endShape(CLOSE);
        p.popMatrix();
    }


    public void applyForce(PVector force){
        PVector f = PVector.div(force,mass);
        acceleration.add(f);
    }

    private void checkEdges(){
        if(this.position.x > p.width + r){
            this.position.x = -r;
        }
        if(this.position.x < -r){
            this.position.x = p.width + r;
        }
        if(this.position.y > p.height + r){
            this.position.y = -r;
        }
        if(this.position.y < -r){
            this.position.y = p.height + r;
        }
    }

    public void vehicleDisplayChange(boolean flag){
        vehicleDisplayFlag = flag;
    }

    public boolean isDead(){
        if(lifeSpan <= 0){
            return true;
        }else{
            return false;
        }
    }
}
