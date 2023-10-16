package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drivetrain {

    //Declare Objects
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    public Gyroscope imu;

    //  -- Constructor --  //
    Drivetrain(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        imu = hardwareMap.get(Gyroscope.class, "imu");

        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
    }



    // JUST TESTING SOMETHING
    /*
     * The mechanum wheels are 75mm in diameter (37.5mm Radius)
     * The total ArcLength is ~235.619mm ( 2 * PI * 37.5)
     *
     * motor.getCurrentPosition() returns the current encoder reading
     * motor.setTargetPosition() sets the encoder target position
     *
     * || GEARBOX RATIO |   ENCODER TICKS (Per Full Revolution)||
     * || --------------|--------------------------------------||
     * ||    40: 1      |      1120 ticks                      ||
     * ||    20: 1      |      560  ticks                      ||
     *
     */

    public void move(double distance, double power){
        setAllMotorsPower(0.5); //50% Full Power
        frontLeft.setTargetPosition(millisToTicks(distance));
        frontRight.setTargetPosition(millisToTicks(distance));
        backLeft.setTargetPosition(millisToTicks(distance));
        backRight.setTargetPosition(millisToTicks(distance));
    }

    public void strafe(double distance, double power){

    }

    public void turn(double angle){
        
    }

    public double ticksToMillis(int ticks) {
        //TODO Change the "1120" to whatever the actual Gearbox ratio is.
        return (ticks/1120.0) * (2 * Math.PI * 37.5);
    }

    public int millisToTicks(double millis){
        //TODO Change the "1120" to whatever the actual Gearbox ratio is.
        return (int)((millis/(2 * Math.PI * 37.5)) * 1120);
    }

    public void setAllMotorsPower(double power){
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }
}
