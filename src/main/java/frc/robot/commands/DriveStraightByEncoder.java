package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.PIDController;
import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;

public class DriveStraightByEncoder extends Command {

    static final double ENCODER_TICKS_PER_INCH = 1;

    double startYaw;
    PIDController pid;
    double encoderStart;
    double speed;
    double distance;
    Timer timer;
    double timeout;

    public DriveStraightByEncoder(double inches, double speed) {
        this(inches, speed, 99999);
    }

    public DriveStraightByEncoder(double inches, double speed, double timeoutSeconds) {
        requires(Robot.drivetrain);
        this.speed = speed;
        distance = inches;
        timeout = timeoutSeconds;
        timer = new Timer();
    }

    double getAverageEncoderValue() {
        double leftWheelEncoder = Robot.drivetrain.getEncoder(Drivetrain.Side.LEFT);
        double rightWheelEncoder = Robot.drivetrain.getEncoder(Drivetrain.Side.RIGHT);
        return (leftWheelEncoder + rightWheelEncoder) / 2;
    }

    @Override
    protected void initialize() {
        startYaw = Robot.gyro.getAngle();
        pid = new PIDController(1e-2, 0, 0);
        encoderStart = getAverageEncoderValue();
        timer.reset();
        timer.start();
    }

    @Override
    protected void execute() {
        pid.input(Robot.gyro.getAngle() - startYaw);
        SmartDashboard.putNumber("Angle difference", Robot.gyro.getAngle() - startYaw);
        double correction = pid.getCorrection();
        SmartDashboard.putNumber("Correction", correction);
        SmartDashboard.putNumber("DriveStraightByEncoder executing", Math.random());
        SmartDashboard.putBoolean("PID stable (3deg threshold)", pid.isStable(3));
        SmartDashboard.putNumber("Encoder ticks left", getEncoderTicksLeft());
        Robot.drivetrain.drive(speed - correction, speed + correction);
    }

    double getEncoderTicksLeft() {
        double totalDistance = distance * ENCODER_TICKS_PER_INCH;
        double distanceGone = getAverageEncoderValue() - encoderStart;
        return totalDistance - distanceGone;
    }

    @Override
    protected boolean isFinished() {
        boolean hasGoneDistance = getEncoderTicksLeft() <= 0;
        return hasGoneDistance || timer.get() > timeout;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }
}
