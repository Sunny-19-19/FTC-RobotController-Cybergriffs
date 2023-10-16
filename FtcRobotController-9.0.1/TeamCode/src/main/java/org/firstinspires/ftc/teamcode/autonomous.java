package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Small Pole on Right")

public class autonomous extends LinearOpMode {
    //declare hardware variables
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    public DcMotor slide;
    public Servo clawLeft;
    public Servo clawRight;
    public Gyroscope imu;
    
    //delcare common variables
    public int pauseTime = 200; //pause between each movement
    public double slideAloft = -.1; //power needed for slide to be held up
    public double slideIncrement = .1; //how fast the slide goes up or down
    public double regPower = .5; //speed of mechanum movements [NOTE] Increased speed means increased stress on the bot
    public int noPower = 0; //DO NOT CHANGE LIGHTLY -- the resting power for ALL motor methods and commands

    @Override
    public void runOpMode() {
        //initialize variables
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        slide = hardwareMap.get(DcMotor.class, "slide");
        clawLeft = hardwareMap.get(Servo.class, "clawLeft");
        clawRight = hardwareMap.get(Servo.class,"clawRight");
        imu = hardwareMap.get(Gyroscope.class, "imu");
        
        //switch directions for flipped motors
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        //during INIT all bot hardware is stopped
        frontLeft.setPower(noPower);
        backLeft.setPower(noPower);
        frontRight.setPower(noPower);
        backRight.setPower(noPower);
        slide.setPower(noPower);
        clawLeft.setPosition(0); //0 for close, .1 for open
        clawRight.setPosition(0.1); //0.1 for close, 0 for open
        
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        //runs until the driver presses STOP
        while (opModeIsActive()) {
            
            //see Competition Autonomous Guide for more info
            
            moveSlide(true, 100);//up
            move(.5,.4,1000); //right
            move(.4,0,500); //right
            moveSlide(true, 350); //up
            north(150); //forward
            moveSlide(false, 300); //down
            moveClaw(false); //open
            south(245); //backward
            //moveSlide(false, 300); //down
            clockWise(1450);//turn
            //north(100);//forward
        
            break;
        }
        
        //at STOP, all bot hardware is stopped
        frontLeft.setPower(noPower);
        backLeft.setPower(noPower);
        frontRight.setPower(noPower);
        backRight.setPower(noPower);
        slide.setPower(noPower);
        clawLeft.setPosition(0);
        clawRight.setPosition(0.1);

        telemetry.addData("Status", "Stopped");
        telemetry.update();
        
    }
    

    //MOVEMENT METHODS


    public void move(double left, double right, int sl) {
        
        String motion;
        
        //calculates Motion message
        if (left > right) {
            motion = "Right";
        } else if (right > left) {
            motion = "Left";
        } else if (left == right && left<0) {
            motion = "Forward";
        } else {
            motion = "Backward";
        }
        
        //update 
        updateDS(motion);
        frontLeft.setPower(left);
        backLeft.setPower(left);
        frontRight.setPower(right);
        backRight.setPower(right);
        sleep(sl);
        
        //between each motion, the bot pauses to cut down on drifting
        pauseBot();
    }


    public void move(double fleft, double fright, double bleft, double bright, int sl) {       
        
        //update 
        updateDS("Custom");
        frontLeft.setPower(fleft);
        backLeft.setPower(bleft);
        frontRight.setPower(fright);
        backRight.setPower(bright);
        sleep(sl);
        
        pauseBot();    
    }
    

    public void moveSlide(boolean up, int sl) { //true is neg (up), false is pos (down)

        double slidePow = slideAloft;

        if (up) {
            for (int i=0; i<sl; i++) {
                slidePow-=slideIncrement;

                //update
                updateDS("Slide Up");
                slide.setPower(slidePow);
                sleep(1);
            }
            slide.setPower(slideAloft);

        } else { //down
            for (int i=0; i<sl; i++) {
                slidePow+=slideIncrement;
                
                //update
                updateDS("Slide Down");
                slide.setPower(slidePow);
                sleep(1);
            }
            slide.setPower(noPower);
        }

        pauseBot();
    }
    

    public void moveClaw(boolean close) { 

        if (close) {
            clawLeft.setPosition(0);
            clawRight.setPosition(0.1);
        } else {
            clawLeft.setPosition(0.1);
            clawRight.setPosition(0);
        }
        
        pauseBot();
    }


    //GENERAL METHODS


    public void updateDS(String motion) {

        telemetry.addData("Status", "Running");
        telemetry.addData("Motion", motion);
        telemetry.addData("frontLeft", "%.2f", frontLeft.getPower());
        telemetry.addData("frontRight", "%.2f", frontRight.getPower());
        telemetry.addData("backLeft","%.2f", backLeft.getPower());
        telemetry.addData("backRight", "%.2f", backRight.getPower());
        telemetry.addData("slide", "%.2f", slide.getPower());
        telemetry.update();
    }


    public void pauseBot() {

        updateDS("Paused");
        frontLeft.setPower(noPower);
        backLeft.setPower(noPower);
        frontRight.setPower(noPower);
        backRight.setPower(noPower);
        sleep(pauseTime);
    }


    //MECHANUM METHODS
    //The cardinal directions are relative to the bot's initial position


    public void clockWise(int sl) {
        
        updateDS("Clockwise");
        frontLeft.setPower(regPower);
        backLeft.setPower(regPower);
        frontRight.setPower(-regPower);
        backRight.setPower(-regPower);
        sleep(sl);
        
        pauseBot();
    }


    public void counterClockWise(int sl) {

        updateDS("Counter Clockwise");
        frontLeft.setPower(-regPower);
        backLeft.setPower(-regPower);
        frontRight.setPower(regPower);
        backRight.setPower(regPower);
        sleep(sl);
         
        pauseBot();
    }


    public void north(int sl) {

        updateDS("North");
        frontLeft.setPower(regPower);
        backLeft.setPower(regPower);
        frontRight.setPower(regPower);
        backRight.setPower(regPower);
        sleep(sl);
        
        pauseBot(); 
    }


    public void northEast(int sl) { 
 
        updateDS("NorthEast");
        frontLeft.setPower(regPower);
        backLeft.setPower(noPower);
        frontRight.setPower(noPower);
        backRight.setPower(regPower);
        sleep(sl);
        
        pauseBot();
    }


    public void east(int sl) {
     
        updateDS("East");
        frontLeft.setPower(regPower);
        backLeft.setPower(-regPower);
        frontRight.setPower(-regPower);
        backRight.setPower(regPower);
        sleep(sl);
        
        pauseBot(); 
    }


    public void southEast(int sl) { 
     
        updateDS("SouthEast");
        frontLeft.setPower(noPower);
        backLeft.setPower(-regPower);
        frontRight.setPower(-regPower);
        backRight.setPower(noPower);
        sleep(sl);
        
        pauseBot();
    }


    public void south(int sl) {

        updateDS("South");
        frontLeft.setPower(-regPower);
        backLeft.setPower(-regPower);
        frontRight.setPower(-regPower);
        backRight.setPower(-regPower);
        sleep(sl);
        
        pauseBot(); 
    }


    public void southWest(int sl) { 
     
        updateDS("SouthWest");
        frontLeft.setPower(-regPower);
        backLeft.setPower(noPower);
        frontRight.setPower(noPower);
        backRight.setPower(-regPower);
        sleep(sl);
        
        pauseBot();
    }


    public void west(int sl) {
    
        updateDS("West");
        frontLeft.setPower(-regPower);
        backLeft.setPower(regPower);
        frontRight.setPower(regPower);
        backRight.setPower(-regPower);
        sleep(sl);
        
        pauseBot(); 
    }


    public void northWest(int sl) {
      
        updateDS("NorthWest");
        frontLeft.setPower(noPower);
        backLeft.setPower(regPower);
        frontRight.setPower(regPower);
        backRight.setPower(noPower);
        sleep(sl);
        
        pauseBot();
    }
}
