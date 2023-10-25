//Im starting to believe that Java is ignoring my comments
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.CustomTelemetry;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Autonomous(name= "AutonBySunny")

public class SunnyAutonomous extends LinearOpMode{

    Drivetrain drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {

        drivetrain = new Drivetrain(hardwareMap);
        drivetrain.stopAndBrake();



        /* EXPERIMENTAL: Independent Telemetry Updater
        CustomTelemetry customTelemetry = new CustomTelemetry(this.telemetry);
        Thread telemetryThread = new Thread(customTelemetry);
        telemetryThread.start();
         */

        waitForStart();

        while(opModeIsActive()){
            //TODO Test in real world. Use ruler to measure distances precision
            drivetrain.drive(1000, 0.5);
            sleep(5000);
            drivetrain.stopAndBrake();
            break;
        }
    }
}
