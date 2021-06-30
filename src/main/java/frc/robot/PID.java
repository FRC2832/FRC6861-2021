/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.*;


public class PID {
    /** Hardware */
	TalonSRX _talon = new TalonSRX(13);
	private final XboxController _xBox = new XboxController(1);

    /** Used to create string thoughout loop */
	StringBuilder _sb = new StringBuilder();
	int _loops = 0;
	
    /** Track button state for single press event */
	boolean _lastButton1 = false;

	/** Save the target position to servo to */
	double targetPositionRotations;

	Gains _gain;

	public void pidControl() {
		SmartDashboard.putNumber("Ingester Lift Position", _talon.getSelectedSensorPosition());
		
		_gain = Constants.kGainDown;

		targetPositionRotations = 0;
		_talon.set(ControlMode.Position, targetPositionRotations);

        _talon.setNeutralMode(NeutralMode.Brake);
        /* Factory Default all hardware to prevent unexpected behaviour */
		_talon.configFactoryDefault();
		
		/* Config the sensor used for Primary PID and sensor direction */
        _talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 
                                            Constants.kPIDLoopIdx,
				                            Constants.kTimeoutMs);

		/* Ensure sensor is positive when output is positive */
		_talon.setSensorPhase(Constants.kSensorPhase);

		/**
		 * Set based on what direction you want forward/positive to be.
		 * This does not affect sensor phase. 
		 */ 
		_talon.setInverted(Constants.kMotorInvert);

		/* Config the peak and nominal outputs, 12V means full */
		_talon.configNominalOutputForward(0, Constants.kTimeoutMs);
		_talon.configNominalOutputReverse(0, Constants.kTimeoutMs);
		_talon.configPeakOutputForward(.50, Constants.kTimeoutMs);
		_talon.configPeakOutputReverse(-.90, Constants.kTimeoutMs); // TODO: Make this stronger.

		/**
		 * Config the allowable closed-loop error, Closed-Loop output will be
		 * neutral within this range. See Table in Section 17.2.1 for native
		 * units per rotation.
		 */
		_talon.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

		/**
		 * Grab the 360 degree position of the MagEncoder's absolute
		 * position, and intitally set the relative sensor to match.
		 */
		int absolutePosition = _talon.getSensorCollection().getPulseWidthPosition();

		/* Mask out overflows, keep bottom 12 bits */
		absolutePosition &= 0xFFF;
		if (Constants.kSensorPhase) { absolutePosition *= -1; }
		if (Constants.kMotorInvert) { absolutePosition *= -1; }
		
		/* Set the quadrature (relative) sensor to match absolute */
		_talon.setSelectedSensorPosition(absolutePosition, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    }
    
	public void commonLoop() {
        SmartDashboard.putNumber("Ingester Lift Position", _talon.getSelectedSensorPosition());

        /* Get Talon/Victor's current output percentage */
		final double motorOutput = _talon.getMotorOutputPercent();

		/* Prepare line to print */
		_sb.append("\tout:");
		/* Cast to int to remove decimal places */
		_sb.append((int) (motorOutput * 100));
		_sb.append("%");	// Percent

		_sb.append("\tpos:");
		_sb.append(_talon.getSelectedSensorPosition(0));
		_sb.append("u"); 	// Native units
        
        if (_xBox.getBumperPressed(Hand.kRight)) {
			targetPositionRotations = 0;
			_talon.set(ControlMode.Position, targetPositionRotations);
			_gain = Constants.kGainUp;
        }
        
		if (_xBox.getBButtonPressed()) {
			targetPositionRotations = -600;
			_talon.set(ControlMode.Position, targetPositionRotations);
			_gain = Constants.kGainUp;
        }

		if (_xBox.getAButtonPressed()) {
			targetPositionRotations = -4500;
			_talon.set(ControlMode.Position, targetPositionRotations);
			_gain = Constants.kGainDown;
		}

		/* Config Position Closed Loop gains in slot0, tsypically kF stays zero. */
		_talon.config_kF(Constants.kPIDLoopIdx, _gain.kF, Constants.kTimeoutMs);
		_talon.config_kP(Constants.kPIDLoopIdx, _gain.kP, Constants.kTimeoutMs);
		_talon.config_kI(Constants.kPIDLoopIdx, _gain.kI, Constants.kTimeoutMs);
		_talon.config_kD(Constants.kPIDLoopIdx, _gain.kD, Constants.kTimeoutMs);

		/* If Talon is in position closed-loop, print some more info */
		if (_talon.getControlMode() == ControlMode.Position) {
			/* ppend more signals to print when in speed mode. */
			_sb.append("\terr:");
			_sb.append(_talon.getClosedLoopError(0));
			_sb.append("u");	// Native Units

			_sb.append("\ttrg:");
			_sb.append(targetPositionRotations);
			_sb.append("u");	/// Native Units

			//check if going up + band around sensor target
			if((targetPositionRotations > -1000) && (Math.abs(_talon.getSelectedSensorPosition(0)-targetPositionRotations) < 100)) {
				_talon.set(ControlMode.PercentOutput,0);
			}
		}

		/**
		 * Print every ten loops, printing too much too fast is generally bad
		 * for performance.
		 */
		if (++_loops >= 10) {
			_loops = 0;
			System.out.println(_sb.toString());
		}

		/* Reset built string for next loop */
		_sb.setLength(0);
		
	}
	
	public void autonLoop() {
		// if (_xBox.getBumperPressed(Hand.kRight)) {
		// 	targetPositionRotations = 0;
		// 	_talon.set(ControlMode.Position, targetPositionRotations);
        // }
		if (Auton.getPutIngesterDown()) {
			targetPositionRotations = -4500;
		} else {
			targetPositionRotations = -600;
		}
		_talon.set(ControlMode.Position, targetPositionRotations);
		SmartDashboard.putNumber("Ingester Lift Position", _talon.getSelectedSensorPosition());
	}

	public double getIngesterPosition() {
		return _talon.getSelectedSensorPosition(); 
	}

}
