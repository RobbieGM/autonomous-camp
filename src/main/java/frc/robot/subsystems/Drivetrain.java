package frc.robot.subsystems;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Represents the robot's drivetrain.
 */
public class Drivetrain extends Subsystem {

    public enum Side {
        LEFT,
        RIGHT
    };

    static List<CANSparkMax> leftWheels;
    static List<CANSparkMax> rightWheels;

    public Drivetrain() {
        Function<? super Integer, ? extends CANSparkMax> createSparkMax = id -> new CANSparkMax(id, MotorType.kBrushless);
		leftWheels = Arrays.asList(3, 4, 7).stream().map(createSparkMax).collect(Collectors.toList());
        rightWheels = Arrays.asList(1, 2, 8).stream().map(createSparkMax).collect(Collectors.toList());
        leftWheels.forEach(wheel -> wheel.setIdleMode(IdleMode.kBrake));
        rightWheels.forEach(wheel -> wheel.setIdleMode(IdleMode.kBrake));
    }

    /**
     * Makes the robot drive, setting the left and right wheel power.
     * @param leftWheelPower
     * @param rightWheelPower
     */
    public void drive(double leftWheelPower, double rightWheelPower) {
        double left = constrainAbsolute(leftWheelPower, 0.15);
        double right = constrainAbsolute(rightWheelPower, 0.15);
        SmartDashboard.putNumber("Left wheel power", left);
        SmartDashboard.putBoolean("Left wheel is NaN", left != left);
        leftWheels.forEach(wheel -> wheel.set(-left));
        rightWheels.forEach(wheel -> wheel.set(right));
    }

    double constrainAbsolute(double power, double max) {
        return Math.min(max, Math.max(-max, power));
        /*if (power < min && power > 0) return min;
        if (power > -min && power < 0) return -min;
        return power;*/
    }

    /**
     * Makes the robot drive, setting the power for both wheels.
     * @param power The power for both wheels
     */
    public void drive(double power) {
        drive(power, power);
    }

    public double getEncoder(Side side) {
        if (side == Side.LEFT) {
            return -averageEncoderValueOfMotors(leftWheels.stream());
        } else {
            return averageEncoderValueOfMotors(rightWheels.stream());
        }
    }

    private double averageEncoderValueOfMotors(Stream<CANSparkMax> sparks) {
        OptionalDouble value = sparks.map(motor -> motor.getEncoder()).mapToDouble(CANEncoder::getPosition).average();
        return value.isPresent() ? value.getAsDouble() : 0;
    }

    @Override
    public void initDefaultCommand() {

    }
}
