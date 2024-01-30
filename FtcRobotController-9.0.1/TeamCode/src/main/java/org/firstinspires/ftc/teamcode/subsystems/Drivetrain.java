package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;

public class Drivetrain {

    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor backLeft;
    private final DcMotor backRight;
    Gyroscope imu;

    private final double wheelRadius = 37.5;
    private final int gearboxRatioTicks = 560;

    /* The mechanum wheels are 75mm in diameter (37.5mm Radius)
     * The total ArcLength is ~235.619mm ( 2 * PI * 37.5)
     *
     * {motor}.getCurrentPosition() returns the current encoder reading
     * {motor}.setTargetPosition() sets the encoder target position
     *
     * || GEARBOX RATIO |   ENCODER TICKS (Per Full Revolution)|| If my I read the docs right lol
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

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        resetAllEncoders();
    }

    public void drive(double distance, double power){
        int ticks = millisToTicks(distance);

        setPowerForAllMotors(Range.clip(power, -1, 1));
        addToTargetPosition(frontLeft, ticks);
        addToTargetPosition(frontRight, ticks);
        addToTargetPosition(backLeft, ticks);
        addToTargetPosition(backRight, ticks);

    }

    public void drive(double drive, double strafe, double turn){

    }

    public void strafe(double distance, double power){

        int ticks = millisToTicks(distance);
        setPowerForAllMotors(Range.clip(power, -1, 1));
        addToTargetPosition(frontLeft, ticks);
        addToTargetPosition(frontRight, -ticks);
        addToTargetPosition(backLeft, ticks);
        addToTargetPosition(backRight, -ticks);

    }

    public void turn(double angle, double power){

        double arcLength = Math.toRadians(angle) * wheelRadius;
        int ticks = millisToTicks(arcLength);

        setPowerForAllMotors(Range.clip(power, -1, 1));
        addToTargetPosition(frontLeft, ticks);
        addToTargetPosition(frontRight, ticks);
        addToTargetPosition(backLeft, -ticks);
        addToTargetPosition(backRight, -ticks);

    }
    public void stopAndBrake(){
        //This activates the zeroPowerBehavior
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void initOpMode(){
        setModeForAllMotors(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        stopAndBrake();
    }

    public void init(){
        frontLeft.setTargetPosition(0);
        frontRight.setTargetPosition(0);
        backLeft.setTargetPosition(0);
        backRight.setTargetPosition(0);

        setModeForAllMotors(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void resetAllEncoders(){
        setModeForAllMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModeForAllMotors(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private int millisToTicks(double millis){
        return (int)((millis/(2 * Math.PI * wheelRadius)) * gearboxRatioTicks);
    }

    private void setPowerForAllMotors(double power){
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    private void addToTargetPosition(DcMotor motor, int ticks){
        int currentReading = motor.getCurrentPosition();
        motor.setTargetPosition(currentReading + ticks);
    }

    public boolean isAnyMotorBusy(){
        return frontRight.isBusy() || frontLeft.isBusy() ||
                backRight.isBusy() || backLeft.isBusy();}

    private void setModeForAllMotors(DcMotor.RunMode mode){
        frontLeft.setMode(mode);
        frontRight.setMode(mode);
        backLeft.setMode(mode);
        backRight.setMode(mode);
    }

    public AngularVelocity getAngularVelocity(){
        return imu.getAngularVelocity(AngleUnit.DEGREES);
    }

    public double getPowerFrom(Motors motor) {
        switch (motor) {
            case FRONT_LEFT:
                return frontLeft.getPower();
            case FRONT_RIGHT:
                return frontRight.getPower();
            case BACK_LEFT:
                return backLeft.getPower();
            case BACK_RIGHT:
                return backRight.getPower();
        }
        return 999; // :Skull Emoji:
    }

    enum Motors{
        FRONT_LEFT,
        FRONT_RIGHT,
        BACK_LEFT,
        BACK_RIGHT
    }
}