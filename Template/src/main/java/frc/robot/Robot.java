// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.auto.AutoUtils;
import frc.robot.subsystems.example.Subsystem;
import frc.robot.subsystems.swerve.Swerve;
import frc.robot.utils.ShuffleData;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  public static Swerve swerve = new Swerve();
  public static Subsystem subsystem = new Subsystem();

  private ShuffleData<Double> batteryVoltageLog = new ShuffleData<Double>("DS", "battery voltage", 0.0);
  private ShuffleData<Boolean> isBrownedOutLog = new ShuffleData<Boolean>("DS", "brownout", false);
  private ShuffleData<Double> cpuTempLog = new ShuffleData<Double>("DS", "cpu temp", 0.0);
  private ShuffleData<Double> CANUtilizationLog = new ShuffleData<Double>("DS", "CAN utilizaition", 0.0);
  private ShuffleData<String> radioStatusLog = new ShuffleData<String>("DS", "radio status", "kOff");
  private ShuffleData<String> allianceLog = new ShuffleData<String>("DS", "alliance", "Red");
  private ShuffleData<Boolean> FMSLog = new ShuffleData<Boolean>("DS", "FMS connected", false);
  private RobotContainer m_robotContainer;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    batteryVoltageLog.set(RobotController.getBatteryVoltage());
    cpuTempLog.set(RobotController.getCPUTemp());
    CANUtilizationLog.set(RobotController.getCANStatus().percentBusUtilization);
    radioStatusLog.set(RobotController.getRadioLEDState().name());
    isBrownedOutLog.set(RobotController.isBrownedOut());
    allianceLog.set(DriverStation.getAlliance().isPresent() ? DriverStation.getAlliance().get().name() : "None");
    FMSLog.set(DriverStation.isFMSAttached());

  }

  @Override
  public void disabledInit() {
    swerve.setBreakMode(false);
  }

  @Override
  public void disabledPeriodic() {
      AutoUtils.getChooser().update();

  }

  @Override
  public void disabledExit() {
    swerve.setBreakMode(true);
  }

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void autonomousExit() {
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void teleopExit() {
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void testExit() {
  }
}