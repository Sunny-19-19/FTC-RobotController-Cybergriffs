package org.firstinspires.ftc.teamcode.subsystems;
import static org.firstinspires.ftc.teamcode.subsystems.Drivetrain.Motors.*;
import org.firstinspires.ftc.robotcore.external.Telemetry;

//THIS CLASS IS EXPERIMENTAL
public class TelemetryClock implements Runnable{
    public TelemetryClock(Telemetry telemetry, Drivetrain drivetrain){
        this.telemetry = telemetry;
        this.drivetrain = drivetrain;
    }

    Drivetrain drivetrain;
    Telemetry telemetry;
    final private int updateInterval = 500;

    private void addDrivetrainData(){
        telemetry.addData("FrontLeft: ", "%.2f", drivetrain.getPowerFrom(FRONT_LEFT));
        telemetry.addData("FrontRight: ", "%.2f", drivetrain.getPowerFrom(FRONT_LEFT));
        telemetry.addData("BackLeft", "%.2f", drivetrain.getPowerFrom(BACK_LEFT));
        telemetry.addData("BackRight", "%.2f", drivetrain.getPowerFrom(BACK_RIGHT));
    }

    @Override
    public void run() {
        addDrivetrainData();
        telemetry.update();
        try{
            Thread.sleep(updateInterval);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}