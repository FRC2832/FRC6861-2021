/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;

//import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private static final String kDefaultAuto = "Default - Move 2s";
    private static final String kCustomAuto1 = "Front of Goal";
    private static final String kCustomAuto2 = "Left of Goal";
    private static final String kCustomAuto3 = "Right of Goal";
    private static final String kCustomAuto4 = "Turn towards Center";
    private static final String kCustomAuto5 = "Find Power Cell";
    private String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();
    private final SendableChooser<String> cam_chooser = new SendableChooser<>();
    private static final String kCameraBack = "Camera Back";
    private static final String kCameraFront = "Camera Front";
    private String cam_autoSelected;
    private static boolean isCenterAutonSelected;
    private static DriveTrain driveTrain;
    private static Ingester ingester;
    private static ColorWheel colorWheel;
    private static Climber climber;
    private static PID pid;
    private static SkyWalker skywalker;
    private static Auton auton;
    // private static UsbCamera usbCameraBack = new UsbCamera("USB Camera 0", 0);
    // private static MjpegServer mjpegServer1 = new MjpegServer("serve_USB Camera
    // 0", 1181);
    // private static UsbCamera usbCameraFront = new UsbCamera("USB Camera 1", 1);
    private static VideoSink server;
    private static Pi camera;
    private static CameraServer camServer;
    private static MjpegServer jpegServ;
    private static VideoSource vSource;
    // private PigeonIMU m_gyro;

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        // CameraServer.getInstance().startAutomaticCapture();
        // CameraServer.getInstance().addCamera(usbCameraBack);
        // CameraServer.getInstance().addCamera(usbCameraFront);
        // mjpegServer1.setSource(usbCameraBack);
        // mjpegServer1.setSource(usbCameraFront);
        // m_chooser.setDefaultOption("Default 2s Move", kDefaultAuto);
        m_chooser.setDefaultOption("Turn towards Center", kCustomAuto4);
        m_chooser.addOption("Default 2s Move", kDefaultAuto);
        m_chooser.addOption("Front of Goal", kCustomAuto1);
        m_chooser.addOption("Left of Goal", kCustomAuto2);
        m_chooser.addOption("Right of Goal", kCustomAuto3);
        m_chooser.addOption("Find Power Cell", kCustomAuto5);
        // m_chooser.addOption("Turn towards Center", kCustomAuto4);
        cam_chooser.setDefaultOption("Front Camera", kCameraFront);
        cam_chooser.addOption("Back Camera", kCameraBack);
        SmartDashboard.putData("Auto Choices", m_chooser);

        driveTrain = new DriveTrain();
        // this.m_gyro = new PigeonIMU(driveTrain.m_leftRrMotor);
        ingester = new Ingester();
        colorWheel = new ColorWheel();
        climber = new Climber();
        pid = new PID();
        skywalker = new SkyWalker();
        auton = new Auton(driveTrain);
        camera = new Pi();
        camServer = CameraServer.getInstance();
        jpegServ = camServer.addServer("10.68.61.62");
        camServer.addSwitchedCamera("OpenCV Camera");
        // vSource = jpegServ.getSource();
        // camServer.startAutomaticCapture(vSource);
        // camera.processTargets();
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like diagnostics that you want ran during disabled, autonomous,
     * teleoperated and test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        //camera.processTargets();
        camera.switchCameras();
        camera.processPowerCells();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable chooser
     * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
     * remove all of the chooser code and uncomment the getString line to get the
     * auto name from the text box below the Gyro
     *
     * <p>
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure below with additional strings. If using the SendableChooser
     * make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        m_autoSelected = m_chooser.getSelected();
        Pi.setHasLostPowerCell(false);
        isCenterAutonSelected = m_autoSelected.equalsIgnoreCase(kCustomAuto4);
        System.out.println("Auto selected: " + m_autoSelected);
        pid.pidControl();
        auton.autonInit();
        // m_gyro.setFusedHeading(0);
        //camera.processTargets();
        camera.processPowerCells();
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        // System.out.println("Autonomous Periodic: " + m_autoSelected);
        // setCamera();

        if (isCenterAutonSelected && !Auton.getMove1SecDone()) {
            System.out.println("Move 1 Second in the if");
            auton.move1Sec();
        } else if (isCenterAutonSelected && !Auton.getMoveHalfSecDone()) {
            if (Auton.getIsAutonDone()) {
                driveTrain.driveTank(0.0, 0.0);
            } else {
                auton.moveHalfSec();
            }
        } else {
            switch (m_autoSelected) {
                case kCustomAuto1:
                    // Put custom auto code here
                    System.out.println("Front of Goal: " + m_autoSelected);
                    auton.autonFrontGoal();
        
                    break;
        
                case kCustomAuto2:
                    // Put custom auto code here
                    System.out.println("Left of Goal: " + m_autoSelected);
                    auton.autonLeftGoal();
        
                    break;
        
                case kCustomAuto3:
                    // Put custom auto code here
                    System.out.println("Right of Goal: " + m_autoSelected);
                    auton.autonRightGoal();
        
                    break;
        
                case kCustomAuto4:
                    // System.out.println("Turn towards Center: " + m_autoSelected);
                    if (Pi.getHasFoundObjective() && !Auton.getIsAutonDone() && !Auton.getIsScoreReady()) {
                        auton.centerRobot(false);
                    } else {
                        driveTrain.driveTank(0,0);
                        if (!Auton.getIsAutonDone() && !Auton.getIsScoreReady()) {
                            Auton.setMoveHalfSecDone(false);
                        } else if (!Auton.getIsAutonDone() && Auton.getIsScoreReady()) {
                            auton.score();
                        }
                    }

                    // if (!Auton.getMove1SecDone()) {
                    //     System.out.println("move1Sec");
                    //     auton.move1Sec();
                    // } else {
                    //     if (Pi.getCentered()) {
                    //         auton.move1Sec();
                    //     } else {
                    //         auton.centerRobot(false);
                    //     }
                    // }
                    // if (!Pi.getCentered() && Auton.getMove1SecDone()) {
                    //     auton.centerRobot(false);
                    // } else {
                    //     System.out.println("move1Sec");
                    //     auton.move1Sec();
                    // }
        
                    break;

                case kCustomAuto5:
                    pid.autonLoop();
                    auton.findPowerCells();
                    if (!Auton.getMove1SecDone() && Auton.getNumSecondsMoved() < 2) {
                        System.out.println("Moving one second.");
                        auton.move1Sec();
                    } else if (Auton.getNumSecondsMoved() < 2) {
                        Auton.setMove1SecDone(false);
                        auton.move1Sec();
                    }
                    break;
        
                case kDefaultAuto:
                default:
                    // Put default auto code here
                    // System.out.println("Move 2 Seconds: " + m_autoSelected);
                    // auton.autonMove2Sec();
                    System.out.println("Move 1 Second");
                    auton.move1Sec();
        
                    break;
            }

        }

    }

    /**
     * This function is called once each time the robot enters teleoperated mode.
     */
    @Override
    public void teleopInit() {

        climber.climberInit();
        pid.pidControl();
        colorWheel.colorInit();
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        System.out.println("Drive tank");
        driveTrain.driveTank();
        System.out.println("Color Wheel");
        colorWheel.colorWheelSpin();
        System.out.println("Climber");
        climber.climber();
        System.out.println("Injetser");
        ingester.ingesterSweep();
        System.out.println("pid");
        pid.commonLoop();
        System.out.println("SkyWalker");
        skywalker.SkyWalk();
        System.out.println("All teleop periodic statements have run");

        // setCamera();
    }

    // private void setCamera() {
    // switch (cam_autoSelected) {
    // case kCameraBack:
    // server.setSource(usbCameraBack);
    // break;
    // case kCameraFront:
    // default:
    // server.setSource(usbCameraFront);
    // break;
    // }
    // }

    @Override
    public void testPeriodic() {

    }

}
