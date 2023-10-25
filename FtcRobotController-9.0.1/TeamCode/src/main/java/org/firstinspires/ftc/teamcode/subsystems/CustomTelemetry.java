package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class CustomTelemetry implements Runnable{
    public CustomTelemetry(Telemetry telemetry){
        this.telemetry = telemetry;
    }

    Telemetry telemetry;
    final int updateInterval = 500;

    @Override
    public void run() {
        telemetry.update();
        try{
            Thread.sleep(updateInterval);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addDataField(String field1, String field2){

    }
}
