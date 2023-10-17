package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
//Sun by Sunny

public class Drivetrain {

    //Declare Objects
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private Gyroscope imu;

    private final double wheelRadius = 37.5;
    private final int gearboxRatioTicks = 1120;
    //TODO Change the gearboxRatio

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

        //TODO: May need to be changed if the current wheel layout doesn't match last year's
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        //TODO @HIGHLIGHT
        /* ZeroPowerBehavior can be: BRAKE, FLOAT, HOLD
         * This behavior is applied when {motor}.setPower(0);
         * BRAKE: Stops the motor in the current position and doesn't allow to move.
         * FLOAT: Stops the motor but doesn't apply resistance and the motor moves freely.
         * HOLD : Stops the motor but actively applies power to hold in that position.*/
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void drive(double distance, double power){
        //Takes distance in Millimeters
        //Takes power in range -1 to 1
        int ticks = millisToTicks(distance);

        setAllMotorsPower(Range.clip(power, -1, 1));
        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);
    }

    public void strafe(double distance, double power){
        //Takes distance in Millimeters
        //Takes power in range -1 to 1
        int ticks = millisToTicks(distance);

        setAllMotorsPower(Range.clip(power, -1, 1));
        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(-ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(-ticks);
    }

    public void turn(double angle, double power){
        //Takes angle in Degrees || Then converts to Radians
        //Takes power in range -1 to 1
        double arcLength = Math.toRadians(angle) * wheelRadius;
        int ticks = millisToTicks(arcLength);

        setAllMotorsPower(Range.clip(power, -1, 1));
        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(-ticks);
    }

    public void stopAndBrake(){
        //This activates zeroPowerBehavior
        //Read Constructor for more info
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    private double ticksToMillis(int ticks) {
        // I made this method but currently has now uses
        // This may come in handy later tho
        return ((double)ticks/gearboxRatioTicks) * (2 * Math.PI * 37.5);
    }

    private int millisToTicks(double millis){
        // Use this method when setting motor's TargetPosition
        return (int)((millis/(2 * Math.PI * 37.5)) * gearboxRatioTicks);
    }

    private void setAllMotorsPower(double power){
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }


    
}