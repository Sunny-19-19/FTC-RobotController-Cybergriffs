package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Tester extends LinearOpMode {

    DcMotor testMotor;
    Servo   testServo;

    @Override
    public void runOpMode() {
        testMotor = hardwareMap.get(DcMotor.class, "DcMotor1");
        testServo = hardwareMap.get(Servo.class, "Servo1");

        testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();

        testMotor.setPower(0.1);
        testMotor.setTargetPosition(500);
    }
}
