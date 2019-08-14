package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class FullAutonomousProgram extends CommandGroup {

    public FullAutonomousProgram() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        // addSequential(new Command2());
        // these will run in order.

        addSequential(new DriveStraightByEncoder(372 - 6, 0.1));
        
        // addSequential(new TurnByGyro(-90));

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

    void goAroundCargoOnce() {
        
    }
}
