package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

public class SunnyOpMode extends OpMode {

    Drivetrain drivetrain;
    //TODO: ARM CLASS

    @Override
    public void init() {
        drivetrain.initOpMode();
        drivetrain.stopAndBrake();

        telemetry.addData("Status: ", "Initialized");
        telemetry.update();

    }

    @Override
    public void loop() {

        double drive  = gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn   = gamepad1.right_stick_x;

        drivetrain.drive(drive, strafe, turn);

        String current = "";

        if(gamepad1.a){
            current = "A";
        }
        else if(gamepad1.b){
            current = "B";
        }
        else if(gamepad1.x){
            current = "X";
        }
        else if(gamepad1.y){
            current = "Y";
        }

        telemetry.addData("Input: ", current);
        telemetry.addData("__________________","");
        telemetry.addData("Drive : ", drive);
        telemetry.addData("Strafe: ", strafe);
        telemetry.addData("Turn  : ", turn);
        telemetry.addData("AngVel: ", drivetrain.getAngularVelocity());
        telemetry.update();
    }
}
