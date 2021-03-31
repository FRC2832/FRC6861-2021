/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class DriveTrain {
  
  public WPI_TalonSRX m_leftFtrMotor = new WPI_TalonSRX(15);
  public WPI_TalonSRX m_leftRrMotor = new WPI_TalonSRX(14);
  public WPI_TalonSRX m_rightFrtMotor = new WPI_TalonSRX(0);
  public WPI_TalonSRX m_rightRrMotor = new WPI_TalonSRX(1);
  public XboxController m_driverController1 = new XboxController(0);
  public SpeedControllerGroup leftMtrGroup = new SpeedControllerGroup(m_leftFtrMotor,m_leftRrMotor);
  public SpeedControllerGroup rightMtrGroup = new SpeedControllerGroup(m_rightFrtMotor,m_rightRrMotor);
  public DifferentialDrive m_robotDrive = new DifferentialDrive(leftMtrGroup, rightMtrGroup);
  
  private double mtrSpeed = -0.75;
  
  public void driveTank() {
      m_leftFtrMotor.setNeutralMode(NeutralMode.Brake);
      m_rightFrtMotor.setNeutralMode(NeutralMode.Brake);
      m_leftRrMotor.setNeutralMode(NeutralMode.Brake);
      m_rightRrMotor.setNeutralMode(NeutralMode.Brake);
      m_robotDrive.tankDrive(Math.pow(m_driverController1.getY(Hand.kLeft), 3) * mtrSpeed, 
                             Math.pow(m_driverController1.getY(Hand.kRight), 3) * mtrSpeed,
                             false); // do not squre since we already cubed it
      
      /*m_robotDrive.tankDrive(m_driverController1.getY(Hand.kLeft) * mtrSpeed,
                               m_driverController1.getY(Hand.kRight) * mtrSpeed,
                               true); // square inputs*/
  }

  public void driveTank(double leftSpeed, double rightSpeed) {
    m_leftFtrMotor.setNeutralMode(NeutralMode.Brake);
    m_rightFrtMotor.setNeutralMode(NeutralMode.Brake);
    m_leftRrMotor.setNeutralMode(NeutralMode.Brake);
    m_rightRrMotor.setNeutralMode(NeutralMode.Brake);
    m_robotDrive.tankDrive(leftSpeed, rightSpeed, false);
  }

  public void driveArcade(double speed, double rotations) {
    m_leftFtrMotor.setNeutralMode(NeutralMode.Brake);
    m_rightFrtMotor.setNeutralMode(NeutralMode.Brake);
    m_leftRrMotor.setNeutralMode(NeutralMode.Brake);
    m_rightRrMotor.setNeutralMode(NeutralMode.Brake);

    m_robotDrive.arcadeDrive(speed, rotations);
  }
}
