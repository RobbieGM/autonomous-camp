package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain.Side;

public class DriveByTime extends Command {

    Timer timer;
    double maxTime;
    double motorPower;
    double leftEncoderStart;
    double rightEncoderStart;

    /**
     * Creates a command for driving the robot straight for a length of time
     * @param seconds
     */
    public DriveByTime(double seconds, double power) {
        requires(Robot.drivetrain);
        maxTime = seconds;
        motorPower = power;
        timer = new Timer();
        leftEncoderStart = Robot.drivetrain.getEncoder(Side.LEFT);
        rightEncoderStart = Robot.drivetrain.getEncoder(Side.RIGHT);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
        Robot.drivetrain.drive(motorPower);
    }

    @Override
    public void execute() {
        SmartDashboard.putNumber("timer.get()", timer.get());
    }

    @Override
    protected boolean isFinished() {
        return timer.get() > maxTime;
    }

    @Override
    public void end() {
        SmartDashboard.putNumber("Left encoder difference", Robot.drivetrain.getEncoder(Side.LEFT) - leftEncoderStart);
        SmartDashboard.putNumber("Right encoder difference", Robot.drivetrain.getEncoder(Side.RIGHT) - rightEncoderStart);
        Robot.drivetrain.drive(0);
    }

    @Override
    protected void interrupted() {
        end();
    }

}