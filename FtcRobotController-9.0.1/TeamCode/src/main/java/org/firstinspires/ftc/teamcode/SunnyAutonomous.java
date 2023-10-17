//Im starting to believe that Java is ignoring my comments
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Autonomous(name= "AutonBySunny")

public class SunnyAutonomous extends LinearOpMode{

    Drivetrain drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {

        drivetrain = new Drivetrain(hardwareMap);

        drivetrain.stopAndBrake();

        waitForStart();

        while(opModeIsActive()){
            //TODO Test in real world. Use ruler to measure distances accuracy
            drivetrain.drive(1000, 0.5);
            sleep(5000);
            drivetrain.drive(-1000,0.5);
            sleep(5000);
            drivetrain.strafe(1000,0.5);
            sleep(5000);
            drivetrain.strafe(-1000,0.5);
            sleep(5000);
            drivetrain.turn(90, 0.5);
            sleep(5000);
            drivetrain.turn(-90,0.5);

            drivetrain.stopAndBrake();
        }
    }
}
