package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ShooterAndElevator extends Subsystem {
    static TalonSRX shooter = new TalonSRX(5);
    public static TalonSRX elevator1 = new TalonSRX(13);
    static TalonSRX elevator2 = new TalonSRX(9);

    public ShooterAndElevator() {
        // TalonSRXPIDSetConfiguration pidConf = new TalonSRXPIDSetConfiguration();
        elevator2.setInverted(true);
    }

    public void setShooterPower(double power) {
        shooter.set(ControlMode.PercentOutput, power);
    }

    public void setElevatorPower(double power) {
        elevator1.set(ControlMode.PercentOutput, power);
        elevator2.follow(elevator1);
    }

    public int getElevatorEncoderTicks() {
        return elevator1.getSensorCollection().getQuadraturePosition();
        //return elevator1.getSelectedSensorPosition();
    }

    @Override
    public void initDefaultCommand() {

    }
}
