package frc.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.DriveTrain;
import frc.robot.Ingester;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.sensors.PigeonIMU;

public class Auton {
    private static Timer m_timer = new Timer();
    private DriveTrain driveTrain;
    private Ingester ingester = new Ingester();
    private PowerDistributionPanel panel;

    private PigeonIMU m_gyro;
    private static int numSecondsMoved;
    private static int numPowerCells;

    private double stepTimeA1 = 4.0;
    private double stepTimeA2 = 1.0;
    private double stepTimeA3 = 3.0;

    private double stepTimeB1 = 1.5;
    private double stepTimeB3 = 2.0;
    private double stepTimeB5 = 2.5;
    private double stepTimeB6 = 1.0;
    private double stepTimeB7 = 3.0;

    private double stepTimeC1 = 2.0; // Default Move Time
    private double stepTimeC2 = 0.5;
    private double stepTimeC3 = 2.0;

    private static boolean move1SecDone = true;
    private static boolean moveHalfSecDone = true;
    private static boolean isAutonDone;
    private static boolean isScoreReady;
    private static boolean putIngesterDown = true;

    private int driveStep = 1;
    private static int autonStep = 1;
    private static int previousAutonStep = 0;

    private static double previousAngle;

    private static double motorOld;
    private static double motorNew;

    public Auton(DriveTrain driveTrain) {
        this.driveTrain = driveTrain;
        this.m_gyro = new PigeonIMU(driveTrain.m_leftRrMotor);
        panel = new PowerDistributionPanel();
    }

    public void autonInit() {
        m_gyro.setFusedHeading(0);
        driveStep = 1;
        autonStep = 1;
        previousAutonStep = 0;
        move1SecDone = true;
        numSecondsMoved = 0;
        numPowerCells = 0;
        m_timer.reset();
        m_timer.start();
        previousAngle = 0;
        SmartDashboard.putNumber("Gyro Fused Heading", m_gyro.getFusedHeading());
        isAutonDone = false;
        driveTrain.driveTank(0, 0);
    }

    // Autonomous mode: Robot positioned anywhere on the line
    // Move forward for a set amount of time
    public void autonMove2Sec() {
        double timerValue = m_timer.get();
        SmartDashboard.putString("Auton Mode", "autonMove2Sec");
        SmartDashboard.putNumber("Auton Step", driveStep);
        SmartDashboard.putNumber("Gyro Fused Heading", m_gyro.getFusedHeading());
        SmartDashboard.putNumber("Timer", timerValue);
        if ((timerValue < stepTimeC1) && (driveStep == 1)) {
            driveTrain.driveArcade(0.6, 0.0);
        } else if ((timerValue > stepTimeC1) && (driveStep == 1)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 2; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }
    }

    public void move1Sec() {
        if (move1SecDone) {
            m_timer.reset();
            m_timer.start();
            return;
        }
        double timerValue = m_timer.get();
        SmartDashboard.putString("Auton Mode", "move1Sec");
        SmartDashboard.putNumber("Auton Step", driveStep);
        SmartDashboard.putNumber("Gyro Fused Heading", m_gyro.getFusedHeading());
        SmartDashboard.putNumber("Timer", timerValue);
        // System.out.println("timer:" + timerValue);
        if ((timerValue < stepTimeC2) && (driveStep == 1)) {
            driveTrain.driveArcade(0.5, 0.0);
        } else if ((timerValue > stepTimeC2) && (driveStep == 1)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 2; // increment driveStep counter, move to next driveStep
            // move1SecDone = true; // TODO: is this right?
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
            System.out.println("move1SecDone = true");
            move1SecDone = true;
            numSecondsMoved++;
            driveStep = 1;
            m_timer.reset();
            m_timer.start();
        }
    }

    public void moveHalfSec() {
        double timerValue = m_timer.get();
        SmartDashboard.putString("Auton Mode", "move1Sec");
        SmartDashboard.putNumber("Auton Step", driveStep);
        SmartDashboard.putNumber("Gyro Fused Heading", m_gyro.getFusedHeading());
        SmartDashboard.putNumber("Timer", timerValue);
        if ((timerValue < stepTimeC3) && (driveStep == 1)) {
            driveTrain.driveArcade(0.6, 0.0);
        } else if ((timerValue > stepTimeC3) && (driveStep == 1)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 2; // increment driveStep counter, move to next driveStep
            // move1SecDone = true; // TODO: is this right?
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
            System.out.println("moveHalfSecDone = true");
            moveHalfSecDone = true;
            isScoreReady = true;
            driveStep = 1;
            m_timer.reset();
            m_timer.start();
        }
    }

    public void score() {
        double timerValue = m_timer.get();
        if ((timerValue < stepTimeC3) && (driveStep == 1)) {
            ingester.ingesterAuton(-1.0);
        } else if ((timerValue > stepTimeC3) && (driveStep == 1)) {
            ingester.ingesterAuton(0.0);
            driveStep = 2; // increment driveStep counter, move to next driveStep
            // move1SecDone = true; // TODO: is this right?
            m_timer.reset();
            m_timer.start();
        } else {
            ingester.ingesterAuton(0.0);
            isScoreReady = false;
            isAutonDone = true;
            driveStep = 1;
            m_timer.reset();
            m_timer.start();
        }
    }

    public static void setMove1SecDone(boolean done) {
        move1SecDone = done;
    }

    public static boolean getMove1SecDone() {
        return move1SecDone;
    }

    public static void setMoveHalfSecDone(boolean done) {
        moveHalfSecDone = done;
    }

    public static boolean getMoveHalfSecDone() {
        return moveHalfSecDone;
    }

    public static boolean getIsAutonDone() {
        return isAutonDone;
    }

    public static boolean getIsScoreReady() {
        return isScoreReady;
    }

    public static void setIsScoreReady(boolean done) {
        isScoreReady = done;
    }

    public static int getAutonStep() {
        return autonStep;
    }

    public static boolean getPutIngesterDown() {
        return putIngesterDown;
    }

    public static int getNumSecondsMoved() {
        return numSecondsMoved;
    }

    public static void resetNumSecondsMoved() {
        numSecondsMoved = 0;
    }

    // Autonomous mode: Robot positioned directly in front of the scoring goal
    // Move fwd to goal, score, get out of the way
    public void autonFrontGoal() {
        SmartDashboard.putString("Auton Mode", "autonFrontGoal");
        SmartDashboard.putNumber("Auton Step", driveStep);
        SmartDashboard.putNumber("Gyro Fused Heading", m_gyro.getFusedHeading());
        SmartDashboard.putNumber("Timer", m_timer.get());
        if ((m_timer.get() < stepTimeA1) && (driveStep == 1)) {
            driveTrain.driveArcade(0.6, 0.0);
        } else if ((m_timer.get() > stepTimeA1) && (driveStep == 1)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 2; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }

        if ((m_timer.get() < stepTimeA2) && (driveStep == 2)) {
            ingester.ingesterAuton(-1.0);
        } else if ((m_timer.get() > stepTimeA2) && (driveStep == 2)) {
            ingester.ingesterAuton(0.0);
            driveStep = 3; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        }

        if ((m_timer.get() < stepTimeA3) && (driveStep == 3)) {
            driveTrain.driveArcade(-0.6, 0.45);
        } else if ((m_timer.get() > stepTimeA3) && (driveStep == 3)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 4; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }
    }

    // Autonomous mode: Robot positioned just left of the scoring goal
    // Move forward, turn right, move forward, move left, move fwd to goal, score,
    // get out of the way
    public void autonLeftGoal() {
        SmartDashboard.putString("Auton Mode", "autonLeftGoal");
        SmartDashboard.putNumber("Auton Step", driveStep);
        SmartDashboard.putNumber("Gyro Fused Heading", m_gyro.getFusedHeading() % 360);
        SmartDashboard.putNumber("Timer", m_timer.get());
        if ((m_timer.get() < stepTimeB1) && (driveStep == 1)) {
            driveTrain.driveArcade(0.6, 0.0);
        } else if ((m_timer.get() > stepTimeB1) && (driveStep == 1)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 2; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }

        if (driveStep == 2) {
            driveTrain.driveArcade(0.3, 0.6);
        }

        if (((m_gyro.getFusedHeading() % 360) < -85) && (driveStep == 2)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 3; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }

        if ((m_timer.get() < stepTimeB3) && (driveStep == 3)) {
            driveTrain.driveArcade(0.5, 0.0);
        } else if ((m_timer.get() > stepTimeB3) && (driveStep == 3)) {
            ingester.ingesterAuton(0.0);
            driveStep = 4; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        }

        if (driveStep == 4) {
            driveTrain.driveArcade(0.3, -0.6);
        }

        if (((m_gyro.getFusedHeading() % 360) > -5) && (driveStep == 4)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 5; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }

        if ((m_timer.get() < stepTimeB5) && (driveStep == 5)) {
            driveTrain.driveArcade(0.6, 0);
        } else if ((m_timer.get() > stepTimeB5) && (driveStep == 5)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 6; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }

        if ((m_timer.get() < stepTimeB6) && (driveStep == 6)) {
            ingester.ingesterAuton(-1.0);
        } else if ((m_timer.get() > stepTimeB6) && (driveStep == 6)) {
            ingester.ingesterAuton(0.0);
            driveStep = 7; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        }

        if ((m_timer.get() < stepTimeB7) && (driveStep == 7)) {
            driveTrain.driveArcade(-0.6, 0.3);
        } else if ((m_timer.get() > stepTimeB7) && (driveStep == 7)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 8; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }
    }

    // Autonomous mode: Robot positioned just right of the scoring goal
    // Move forward, turn left, move forward, move right, move fwd to goal, score,
    // get out of the way
    public void autonRightGoal() {
        SmartDashboard.putString("Auton Mode", "autonRightGoal");
        SmartDashboard.putNumber("Auton Step", driveStep);
        SmartDashboard.putNumber("Gyro Fused Heading", m_gyro.getFusedHeading() % 360);
        SmartDashboard.putNumber("Timer", m_timer.get());
        if ((m_timer.get() < stepTimeB1) && (driveStep == 1)) {
            driveTrain.driveArcade(0.6, 0.0);
        } else if ((m_timer.get() > stepTimeB1) && (driveStep == 1)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 2; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }

        if (driveStep == 2) {
            driveTrain.driveArcade(0.3, -0.6);
        }

        if (((m_gyro.getFusedHeading() % 360) > 85) && (driveStep == 2)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 3; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }

        if ((m_timer.get() < stepTimeB3) && (driveStep == 3)) {
            driveTrain.driveArcade(0.5, 0.0);
        } else if ((m_timer.get() > stepTimeB3) && (driveStep == 3)) {
            ingester.ingesterAuton(0.0);
            driveStep = 4; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        }

        if (driveStep == 4) {
            driveTrain.driveArcade(0.3, 0.6);
        }

        if (((m_gyro.getFusedHeading() % 360) < 5) && (driveStep == 4)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 5; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }

        if ((m_timer.get() < stepTimeB5) && (driveStep == 5)) {
            driveTrain.driveArcade(0.6, 0);
        } else if ((m_timer.get() > stepTimeB5) && (driveStep == 5)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 6; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }

        if ((m_timer.get() < stepTimeB6) && (driveStep == 6)) {
            ingester.ingesterAuton(-1.0);
        } else if ((m_timer.get() > stepTimeB6) && (driveStep == 6)) {
            ingester.ingesterAuton(0.0);
            driveStep = 7; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        }

        if ((m_timer.get() < stepTimeB7) && (driveStep == 7)) {
            driveTrain.driveArcade(-0.6, -0.3);
        } else if ((m_timer.get() > stepTimeB7) && (driveStep == 7)) {
            driveTrain.driveArcade(0, 0);
            driveStep = 8; // increment driveStep counter, move to next driveStep
            m_timer.reset();
            m_timer.start();
        } else {
            driveTrain.driveArcade(0, 0);
        }
    }

    public void centerRobot(boolean isFindingPowerCells) {
        // System.out.println("centering robot");
        // if (Pi.getMoveLeft()) {
        // driveTrain.driveTank(-0.13, 0.13);
        // System.out.println("turning left");
        // } else if (Pi.getMoveRight()) {
        // driveTrain.driveTank(0.13, -0.13);
        // System.out.println("turning right");
        // } else {
        // driveTrain.driveTank(0, 0);
        // System.out.println("not turning");
        // // System.out.println("move1SecDone = false");
        // //if (!isFindingPowerCells)
        // // move1SecDone = false;
        // //else //if (Pi.getHasFoundObjective())
        // autonStep++;
        // }
        motorNew = Pi.getScaledMotorVal();
        // if (motorNew != motorOld) {
        // System.out.println("motor: " + motorNew);
        // }
        driveTrain.driveTank(motorNew * -1, motorNew);
        if (Math.abs(Pi.getMotorVal()) < 0.05) {
            driveTrain.driveTank(0, 0);
            autonStep++;
        }
        motorOld = motorNew;
    }

    public void findPowerCells() {
        if (previousAutonStep != autonStep) {
            System.out.println("Auton Step: " + autonStep);
            SmartDashboard.putNumber("Auton Step", autonStep);
        }
        previousAutonStep = autonStep;
        SmartDashboard.putString("Auton Mode", "Find Power Cells");
        SmartDashboard.putNumber("Gyro Fused Heading", m_gyro.getFusedHeading() % 360);

        switch (autonStep) {
        case 1:
            if (!Pi.getHasFoundObjective()) {
                if (numPowerCells < 3) {
                    driveTrain.driveTank(-0.2, 0.2);
                } else {
                    driveTrain.driveTank(0, 0);
                    autonStep = 6;
                }
            } else {
                Pi.setHasLostPowerCell(false);
                centerRobot(true);
            }
            ingester.ingesterAuton(0.0);
            // autonStep++;
            break;
        case 2:
            ingester.ingesterAuton(1.0);
            if (Pi.getHasLostPowerCell()) {
                numSecondsMoved = 0;
                autonStep++;
                driveTrain.driveTank(0, 0);
            } else if (move1SecDone) {
                if (numSecondsMoved > 2) {
                    numSecondsMoved = 0;
                    autonStep = 1; // Center to ball again.
                    driveTrain.driveTank(0, 0);
                } else {
                    move1SecDone = false;
                    move1Sec();
                }
            } else {
                move1Sec();
            }
            break;
        case 3:
            driveStep = 1;
            move1SecDone = false;
            System.out.println("Set move1SecDone to false.");
            ingester.ingesterAuton(1.0);
            move1Sec();
            autonStep++;
            numSecondsMoved = 0;
            break;
        case 4:
            ingester.ingesterAuton(1.0);
            move1Sec();
            if (numSecondsMoved >= 2) {
                System.out.println("Moved at least two seconds");
                move1SecDone = true;
                driveStep = 1;
                autonStep++;
            } else if (move1SecDone) {
                System.out.println("MoveOneSec is done, but we need to keep moving");
                move1SecDone = false;
                driveStep = 1;
            }
            break;
        case 5:
            driveTrain.driveTank(0.0, 0.0);
            ingester.ingesterAuton(0.0);
            numPowerCells++;
            System.out.println("numPowerCells: " + numPowerCells);
            if (numPowerCells >= 3) {
                autonStep++;
            } else {
                numSecondsMoved = 0;
                autonStep = 1;
            }
            break;
        // System.out.println(panel.getCurrent(2));
        case 6: // TODO? Might need to feed watchdog because of ingester.
            double currentAngle = Math.abs(m_gyro.getFusedHeading() % 360);
            ingester.ingesterAuton(0.0);
            if (Math.abs(currentAngle - previousAngle) > 0.5) {
                System.out.println("gyro: " + currentAngle);
            }
            if (currentAngle != previousAngle) {
                previousAngle = currentAngle;
            }
            if (currentAngle < 15 || currentAngle > 345) {
                driveTrain.driveTank(0, 0);
                numSecondsMoved = 0;
                autonStep++;
            } else if (currentAngle < 180) {
                driveTrain.driveTank(0.25, -0.25);
            } else {
                driveTrain.driveTank(-0.25, 0.25);
            }
            // if(currentAngle < 180) {
            // if (currentAngle < 10) {
            // driveTrain.driveTank(0, 0);
            // } else {
            // driveTrain.driveTank(-0.14, 0.14);
            // }
            // } else {
            // if (currentAngle > 350) {
            // driveTrain.driveTank(0, 0);
            // } else {
            // driveTrain.driveTank(0.14, -0.14);
            // }
            // }
            break;
        case 7:
            ingester.ingesterAuton(0.0);
            if (numSecondsMoved >= 20) {
                driveTrain.driveTank(0, 0);
            } else {
                if (move1SecDone) {
                    move1SecDone = false;
                }
                move1Sec();
            }
            break;
        default:
            break;

        }
    }

}