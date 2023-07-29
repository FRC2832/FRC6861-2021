/**
 * Simple class containing constants used throughout project
 */
package frc.robot;

public class Constants {
	/**
	 * Which PID slot to pull gains from. Starting 2018, you can choose from
	 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
	 * configuration.
	 */
	public static final int kSlotIdx = 0;

	/**
	 * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
	 * now we just want the primary one.
	 */
	public static final int kPIDLoopIdx = 0;

	/**
	 * Set to zero to skip waiting for confirmation, set to nonzero to wait and
	 * report to DS if action fails.
	 */
	public static final int kTimeoutMs = 30;
	
	/* Choose so that Talon does not report sensor out of phase */
	public static boolean kSensorPhase = true;

	/**
	 * Choose based on what direction you want to be positive,
	 * this does not affect motor invert. 
	 */
	public static boolean kMotorInvert = false;

	/**
	 * Gains used in Positon Closed Loop, to be adjusted accordingly
     * Gains(kp, ki, kd, kf, izone, peak output);
     */
	static final Gains kGainUp = new Gains(0.3, 2e-5, 1, 0.0, 0, 0.4);
	static final Gains kGainDown = new Gains(0.2, 0, 1, 0.0, 0, 1);

	// Current limit constants
	public static final int ingestorLiftContinuousAmpLimit = 30;

	// Motor ID constants
	public static final int _talonMotorID = 13;
	public static final int sweepMotorID = 2;
	public static final int walkMotorID = 4;
	public static final int colorWheelMotorID = 5;
	public static final int climberMotorCANID = 3;

	// Controller Constants
	public static final int operatorControllerID = 1;
	public static final int driverControllerID = 0;

	// Drivetrain constants
	public static final int rightRearMotor = 1;
	public static final int rightFrontMotor = 0;
	public static final int leftFrontMotor = 15;
	public static final int leftRearMotor = 14;
	public static final double driveTrainMotorSpeed = -0.9;

	// Ingestor Lift Positions
	public static final double lowerIngestorPos = -4200;
	public static final double shootingIngestorPos = -600;
	public static final double stowIngestorPos = 0;
	
}
