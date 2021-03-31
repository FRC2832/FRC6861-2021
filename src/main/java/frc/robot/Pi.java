package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.XboxController;

public class Pi {
    private NetworkTableInstance netTableInstance;
    private NetworkTable table;
    // private NetworkTableEntry camSelect;
    private boolean isButtonHeld;
    private XboxController m_driverController1;
    private NetworkTableEntry targetCenterX;
    private NetworkTableEntry powerCellCenterX;
    private NetworkTableEntry powerCellCenterY;
    private int numPowerCells;
    private final double CAM_X_RES = 1280;
    private final double CAM_Y_RES = 720;
    private static boolean moveRight;
    private static boolean moveLeft;
    private static boolean hasFoundObjective;
    private static boolean hasLostPowerCell;


    public Pi() {
        m_driverController1 = new XboxController(0);
        netTableInstance = NetworkTableInstance.getDefault();
        table = netTableInstance.getTable("datatable");
        // camSelect = netTableInstance.getTable("SmartDashboard").getEntry("camNumber");
        targetCenterX = table.getEntry("targetX");
        powerCellCenterX = table.getEntry("powerCellX");
        powerCellCenterY = table.getEntry("powerCellY");
        // targetCenterX.getNumberArray(new Number[0]);
        CameraServer.getInstance().addServer("10.68.61.62");
    }

    public void processPowerCells() {
        Number[] powerCellCenterXArray = powerCellCenterX.getNumberArray(new Number[0]);
        Number[] powerCellCenterYArray = powerCellCenterY.getNumberArray(new Number[0]);
        if (powerCellCenterXArray.length == 0) {
            hasFoundObjective = false;
            numPowerCells = 0;
            return;
        }
        hasFoundObjective = true;
        if (powerCellCenterXArray.length < numPowerCells) {
            hasLostPowerCell = true;
        }
        numPowerCells = powerCellCenterXArray.length;
        double powerCellX = (double) powerCellCenterXArray[0];

        if (powerCellX < (528) - 25) {
            moveRight = false;
            moveLeft = true;
        } else if (powerCellX > (528) + 25) {
            moveLeft = false;
            moveRight = true;
        } else {
            System.out.println("Power cell x value: " + powerCellX);
            moveRight = false;
            moveLeft = false;
        }
        
    }

    public void processTargets() {
        Number[] targetCenterArray = targetCenterX.getNumberArray(new Number[0]);
        if (targetCenterArray.length == 0) {
            Auton.setMove1SecDone(true);
            hasFoundObjective = false;
            moveRight = false;
            moveLeft = false;
            return;
        }
        hasFoundObjective = true;
        double targetX = (double) targetCenterArray[targetCenterArray.length - 1]; //rightmost target
        //System.out.println("target x value: " + targetX);
        if (targetX < (CAM_X_RES / 2) - (CAM_X_RES * 0.05)) {
            moveRight = false;
            moveLeft = true;
            // System.out.println(moveRight + "\n" + moveLeft);
        } else if (targetX > (CAM_X_RES / 2) + (CAM_X_RES * 0.05)) {
            moveLeft = false;
            moveRight = true;
            // System.out.println(moveRight + "\n" + moveLeft);
        } else {
            moveRight = false;
            moveLeft = false;
            // System.out.println(moveRight + "\n" + moveLeft);
        }
    }

    public static boolean getMoveRight() {
        return moveRight;
    }

    public static void setHasLostPowerCell(boolean hasLostPowerCell) {
        Pi.hasLostPowerCell = hasLostPowerCell;
    }

    public static boolean getHasLostPowerCell() {
        return hasLostPowerCell;
    }

    public static boolean getMoveLeft() {
        return moveLeft;
    }

    public static boolean getCentered() {
        return !moveRight && !moveLeft;
    }

    public static boolean getHasFoundObjective() {
        return hasFoundObjective;
    }

    public void switchCameras() {
        // if (m_driverController1.getStartButtonPressed()) {
        //     if (!isButtonHeld) {
        //         int currentCam = (int) camSelect.getNumber(0);
        //         camSelect.setNumber((currentCam + 1) % 2);
        //         isButtonHeld = true;
        //     }
        // }
        // else {
        //     isButtonHeld = false;
        // }
    }
}