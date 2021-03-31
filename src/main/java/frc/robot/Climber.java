/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Relay;


public class Climber {
    private static final int sparkID = 3;
    public XboxController m_driverController1 = new XboxController(0);
    public CANSparkMax m_motor = new CANSparkMax(sparkID, MotorType.kBrushless);
    public Relay m_latch = new Relay(0);
    public CANEncoder m_encoder;

    public void climberInit() {
        m_motor.setIdleMode(IdleMode.kBrake);
        m_encoder = m_motor.getEncoder();
        m_encoder.setPosition(0.0);
    }

    public void climber() {
        SmartDashboard.putNumber("Light Saber Current", m_motor.getOutputCurrent());
        SmartDashboard.putNumber("Light Saber Position", m_encoder.getPosition());
        if ((m_driverController1.getTriggerAxis(Hand.kRight) > 0.5) && (m_encoder.getPosition() < 31)) {
            m_motor.set(0.33);
        }
        if ((m_driverController1.getTriggerAxis(Hand.kRight) > 0.5) && (m_encoder.getPosition() > 30)
                && (m_encoder.getPosition() < 171)) {
            m_motor.set(0.8);
        }
        if ((m_driverController1.getTriggerAxis(Hand.kRight) > 0.5) && (m_encoder.getPosition() > 170)) {
            m_motor.set(0.33);
        }
        if ((m_driverController1.getTriggerAxis(Hand.kLeft) > 0.5) && (m_encoder.getPosition() < 31)) {
            m_motor.set(-0.33);
        }
        if ((m_driverController1.getTriggerAxis(Hand.kLeft) > 0.5) && (m_encoder.getPosition() > 30)
                && (m_encoder.getPosition() < 171)) {
            m_motor.set(-0.8);
        }
        if ((m_driverController1.getTriggerAxis(Hand.kLeft) > 0.5) && (m_encoder.getPosition() > 170)
                && (m_encoder.getPosition() < 300)) {
            m_motor.set(-0.33);
        }
        if ((m_driverController1.getTriggerAxis(Hand.kRight) > 0.5) && (m_driverController1.getTriggerAxis(Hand.kLeft) > 0.5)) {
            m_motor.set(0.4);
        }
        if ((m_driverController1.getTriggerAxis(Hand.kRight) < 0.5) && (m_driverController1.getTriggerAxis(Hand.kLeft) < 0.5)) {
            m_motor.set(0.0);
        }
    }
}