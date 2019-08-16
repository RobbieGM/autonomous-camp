/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class DriveStraightByTime extends Command {

    Timer timer;
    double time;

    public DriveStraightByTime(double inches) {
        this.time = inchesToSeconds(inches);
        requires(Robot.drivetrain);
    }

    static double inchesToSeconds(double inches) {
        return 0.03045 * (inches - 7.5);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        timer = new Timer();
        timer.reset();
        timer.start();
        Robot.drivetrain.drive(0.18, 0.1);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return timer.get() > time;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.drivetrain.drive(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
