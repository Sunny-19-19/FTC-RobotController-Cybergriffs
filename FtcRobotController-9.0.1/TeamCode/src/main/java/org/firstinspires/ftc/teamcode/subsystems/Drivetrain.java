package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class Drivetrain {

    //Declare Objects
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private Gyroscope imu;

    private final double wheelRadius = 37.5;
    private final int gearboxRatioTicks = 560;
    private final boolean isWaitingEnabled = false;

    /* The mechanum wheels are 75mm in diameter (37.5mm Radius)
     * The total ArcLength is ~235.619mm ( 2 * PI * 37.5)
     *
     * {motor}.getCurrentPosition() returns the current encoder reading
     * {motor}.setTargetPosition() sets the encoder target position
     *
     * || GEARBOX RATIO |   ENCODER TICKS (Per Full Revolution)||
     * || --------------|--------------------------------------||
     * ||    40: 1      |      1120 ticks                      ||
     * ||    20: 1      |      560  ticks                      ||
     */

    //  -- CONSTRUCTOR --  //
    public Drivetrain(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        imu = hardwareMap.get(Gyroscope.class, "imu");

        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        /* ZeroPowerBehavior can be: BRAKE, FLOAT, HOLD
         * This behavior is applied when [DcMotor].setPower(0);
         * BRAKE: Stops the motor in the current position and doesn't allow movement.
         * FLOAT: Stops the motor but doesn't apply resistance and the motor moves freely.
         * HOLD : Stops the motor but actively applies power to hold in that position.*/
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        resetAllEncoders(); //Just in case the encoder's reading is not at zero
    }

    public void drive(double distance, double power){
        int ticks = millisToTicks(distance);

        setPowerForAllMotors(Range.clip(power, -1, 1));
        addToTargetPosition(frontLeft, ticks);
        addToTargetPosition(frontRight, ticks);
        addToTargetPosition(backLeft, ticks);
        addToTargetPosition(backRight, ticks);

        waitUntilDone();

    }

    public void strafe(double distance, double power){

        int ticks = millisToTicks(distance);
        setPowerForAllMotors(Range.clip(power, -1, 1));
        addToTargetPosition(frontLeft, ticks);
        addToTargetPosition(frontRight, -ticks);
        addToTargetPosition(backLeft, ticks);
        addToTargetPosition(backRight, -ticks);

        waitUntilDone();
    }

    public void turn(double angle, double power){

        double arcLength = Math.toRadians(angle) * wheelRadius;
        int ticks = millisToTicks(arcLength);

        setPowerForAllMotors(Range.clip(power, -1, 1));
        addToTargetPosition(frontLeft, ticks);
        addToTargetPosition(frontRight, ticks);
        addToTargetPosition(backLeft, -ticks);
        addToTargetPosition(backRight, -ticks);

        waitUntilDone();

    }
    public void stopAndBrake(){
        //This activates the zeroPowerBehavior
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void resetAllEncoders(){
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public boolean isAnyMotorBusy(){
        if(frontRight.isBusy() || frontLeft.isBusy() ||
           backRight.isBusy()  || backLeft.isBusy()){
            return true;
        }
        return false;
    }

    private void waitUntilDone(){
        if(isWaitingEnabled){
            while(isAnyMotorBusy()){
                //Does nothing while any motor is busy
            }
        }
    }

    private double ticksToMillis(int ticks) {
        return ((double)ticks/gearboxRatioTicks) * (2 * Math.PI * wheelRadius);
    }

    private int millisToTicks(double millis){
        // Use this method when setting motor's TargetPosition
        return (int)((millis/(2 * Math.PI * wheelRadius)) * gearboxRatioTicks);
    }

    private void setPowerForAllMotors(double power){
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    private void addToTargetPosition(DcMotor motor, int ticks){
        //This method is used instead of setTargetPosition() because
        //most of the time the robot will not return to the original
        //position and it is overall more convenient.
        int currentReading = motor.getCurrentPosition();
        motor.setTargetPosition(currentReading + ticks);
    }

    public double getPowerFrom(Motors motor){
        //This is just for the telemetry
        //I made it this way to keep the motors private
        switch (motor){
            case FRONT_LEFT:
                return frontLeft.getPower();
            case FRONT_RIGHT:
                return frontRight.getPower();
            case BACK_LEFT:
                return  backLeft.getPower();
            case BACK_RIGHT:
                return backRight.getPower();
        }
        return 999; //Bro, If the code ever returns this:
                    //----------💀💀💀----------------
    }               //Something has gone terribly wrong

    enum Motors{
        FRONT_LEFT,
        FRONT_RIGHT,
        BACK_LEFT,
        BACK_RIGHT
    }
}