/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;


public class SkyWalker {
    
    private SpeedController m_walk = new WPI_VictorSPX(4);
    public XboxController m_driverController2 = new XboxController(1);
    
    public void SkyWalk(){
        
        if (m_driverController2.getX(Hand.kLeft) > 0.1) {
            m_walk.set(m_driverController2.getX(Hand.kLeft) * 1);
        }
        if (m_driverController2.getX(Hand.kLeft) < -0.1) {
            m_walk.set(m_driverController2.getX(Hand.kLeft) * 1);
        }
        if ((m_driverController2.getX(Hand.kLeft) < 0.1) && (m_driverController2.getX(Hand.kLeft) > -0.1))  {
            m_walk.set(0.0);
        }
    }
}
