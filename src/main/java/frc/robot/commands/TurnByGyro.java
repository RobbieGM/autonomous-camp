package frc.robot.commands;

import java.util.function.UnaryOperator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.PIDController;
import frc.robot.Robot;

public class TurnByGyro extends Command {

  static final double DEGREES_PER_SECOND = 70;
  static final double DEGREE_THRESHOLD = 1;

  double goalAngle;
  double relativeGoal;
  double currentGoal;
  PIDController pid;
  double gyroStart;
  Timer timer;

  public TurnByGyro(double degrees) {
    relativeGoal = degrees;
    requires(Robot.drivetrain);
  }

  @Override
  protected void initialize() {
    System.out.println("TurnByGyro init");
    pid = new PIDController(0, 0, 0);
    // pid = new PIDController(2.5e-2, 0, 1.5e-1);
    gyroStart = Robot.gyro.getAngle();
    goalAngle = gyroStart + relativeGoal;
    timer = new Timer();
    timer.reset();
    timer.start();
  }

  @Override
  protected void execute() {
    pid.P = SmartDashboard.getNumber("P", 0.015);
    pid.I = SmartDashboard.getNumber("I", 4e-6);
    pid.D = SmartDashboard.getNumber("D", 0);

    double currentRelativeGoal = relativeGoal < 0 ? -timer.get() * DEGREES_PER_SECOND : timer.get() * DEGREES_PER_SECOND;
    double absRelativeGoal = Math.abs(relativeGoal);
    currentRelativeGoal = Math.max(-absRelativeGoal, Math.min(absRelativeGoal, currentRelativeGoal));
    double currentGoal = gyroStart + currentRelativeGoal;
    pid.input(Robot.gyro.getAngle() - currentGoal);
    // double correction = Math.min(pid.getCorrection(), MAX_SPEED);
    double correction = pid.getCorrection();
    Robot.drivetrain.drive(-correction, correction);
    SmartDashboard.putBoolean("TurnByGyro done", false);
    SmartDashboard.putNumber("Actual P", pid.P);
    SmartDashboard.putNumber("Actual I", pid.I);
    SmartDashboard.putNumber("Actual D", pid.D);
    UnaryOperator<Double> twoDecimalPlaces = x -> Math.round(x * 100) / 100.0;
    SmartDashboard.putNumber("P correction", twoDecimalPlaces.apply(pid.getP()));
    SmartDashboard.putNumber("I correction", twoDecimalPlaces.apply(pid.getI()));
    SmartDashboard.putNumber("D correction", twoDecimalPlaces.apply(pid.getD()));
  }

  @Override
  protected boolean isFinished() {
    return pid.isStable(DEGREE_THRESHOLD, 1000); // If changing to gradual (changing goal) instead of instant/limited speed PID, also check if the current goal = the actual goal
  }

  @Override
  protected void end() {
    SmartDashboard.putBoolean("TurnByGyro done", true);
    Robot.drivetrain.drive(0);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
