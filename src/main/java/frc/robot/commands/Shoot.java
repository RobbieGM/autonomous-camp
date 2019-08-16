package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Shoot extends Command {

    Timer timer;
    double speed;
    double time;

    public Shoot(double seconds) {
        this(seconds, 1);
    }

    public Shoot(double seconds, double speed) {
        requires(Robot.shooter);
        this.speed = speed;
        time = seconds;
    }

    @Override
    protected void initialize() {
        timer = new Timer();
        timer.reset();
        timer.start();
        Robot.shooter.setShooterPower(speed);
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return timer.get() > time;
    }

    @Override
    protected void end() {
        Robot.shooter.setShooterPower(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
