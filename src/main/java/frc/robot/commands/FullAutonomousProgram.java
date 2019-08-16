package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class FullAutonomousProgram extends CommandGroup {

    public FullAutonomousProgram() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        // addSequential(new Command2());
        // these will run in order.

        // 3 seconds -> 106in
        // y = mx + 7.5
        // 106 = 3m + 7.5
        // 98.5 = 3m
        // 32.83 = m
        // y = 32.83x + 7.5
        // (y - 7.5) = 32.83x
        // 1 / 32.83(y-7.5) = x

        addSequential(new DriveStraightByTime(199));
        addSequential(new TurnRightByTime());
        addSequential(new Shoot(2));

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        // addSequential(new Command2());

        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }

    void loadTwoBallsWithEncoder() {
        addSequential(new DriveStraightByEncoder(128)); // time: 123 + 11
        addSequential(new Wait(), 1.5);
        addSequential(new TurnByGyro(90));
        addSequential(new DriveStraightByEncoder(58)); // 53 + 16
        addSequential(new Shoot(2));
        addSequential(new Wait(), 1.5);
        // addSequential(new DriveStraightByEncoder(-25));
        // addSequential(new Wait(), 1.5);    
        addSequential(new TurnByGyro(183)); // 90
        // addSequential(new Wait(), 1.5);
        // addSequential(new TurnByGyro(90));
        addSequential(new DriveStraightByEncoder(72));
        addSequential(new Shoot(0.6));
        addSequential(new Wait(), 1.5);
        addSequential(new TurnByGyro(177)); // 90
        // addSequential(new Wait(), 1.5);
        // addSequential(new TurnByGyro(90));
        addSequential(new DriveStraightByEncoder(74));
        addSequential(new Shoot(2));
    }

    void drive(double inches) {
        addSequential(new DriveStraightByTime(inches));
        addSequential(new Wait(), 1.5);
    }

    void turn() {
        addSequential(new TurnLeftByTime());
        addSequential(new Wait(), 1.5);
    }

    void driveAndTurn(double inches) {
        drive(inches);
        turn();
    }
}
