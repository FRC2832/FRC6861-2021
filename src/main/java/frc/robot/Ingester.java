/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;


public class Ingester {

    private SpeedController m_sweep = new WPI_VictorSPX(Constants.sweepMotorID); // 2
    public XboxController m_driverController2 = new XboxController(Constants.operatorControllerID); // 1

    public void ingesterSweep() {
        SmartDashboard.putNumber("Left Trigger Value: ", m_driverController2.getTriggerAxis(Hand.kLeft));
        SmartDashboard.putNumber("Right Trigger Value: ", m_driverController2.getTriggerAxis(Hand.kRight));
        if (m_driverController2.getTriggerAxis(Hand.kLeft) > 0.1) {
            m_sweep.set(m_driverController2.getTriggerAxis(Hand.kLeft) * -1);
        }
        if (m_driverController2.getTriggerAxis(Hand.kRight) > 0.1) {
            m_sweep.set(m_driverController2.getTriggerAxis(Hand.kRight));
        }
        if (m_driverController2.getTriggerAxis(Hand.kRight) < 0.1 & m_driverController2.getTriggerAxis(Hand.kLeft) < 0.1) {
            m_sweep.set(0.0);
        }
        
        SmartDashboard.putNumber("Left Trigger Value: ", m_driverController2.getTriggerAxis(Hand.kLeft));
        SmartDashboard.putNumber("Right Trigger Value: ", m_driverController2.getTriggerAxis(Hand.kRight));
    }

    public void ingesterAuton(double speed){
        m_sweep.set(speed);
    }

   // public boolean hasCurrentSpiked() {
        //((WPI_VictorSPX) m_sweep)
    //    PowerDistributionPanel panel = PowerDistributionPanel
    //}
}
