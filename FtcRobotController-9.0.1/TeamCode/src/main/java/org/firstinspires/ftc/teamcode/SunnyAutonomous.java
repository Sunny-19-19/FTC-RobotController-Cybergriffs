package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.subsystems.TelemetryClock;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Autonomous(name= "AutonBySunny")

public class SunnyAutonomous extends LinearOpMode{

    Drivetrain drivetrain;
    TelemetryClock telemetryClock;
    Thread telemetryThread;

    @Override
    public void runOpMode(){

        drivetrain = new Drivetrain(hardwareMap);
        drivetrain.stopAndBrake();

        //Experimental -->
        telemetryClock = new TelemetryClock(this.telemetry, this.drivetrain);
        telemetryThread = new Thread(telemetryClock);
        telemetryThread.start();
        //Experimental <--

        telemetry.addData("Status: ", "Everything Ok");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            drivetrain.drive(1000, 0.5);
            sleep(5000);
            drivetrain.stopAndBrake();
            break;
        }

        telemetryClock.stop();
    }
}
