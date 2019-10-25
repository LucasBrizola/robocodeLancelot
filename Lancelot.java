package rsa;

import robocode.*;
import java.awt.*;

/**
 * Mr. Robot - Elliot maded by Pedro Nunes and Lucas Brizola 2019
 */
public class Lancelot extends AdvancedRobot {

  int moveDirection = 1;//which way to move
  int flag = 1;

  public void run() {
    setAdjustRadarForRobotTurn(true);//keep the radar still while we turn
    setBodyColor(Color.black);
    setGunColor(Color.black);
    setRadarColor(Color.black);
    setScanColor(Color.green);
    setBulletColor(Color.yellow);
    setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
    turnRadarRightRadians(Double.POSITIVE_INFINITY);//keep turning radar right
  }

  public void onScannedRobot(ScannedRobotEvent e) {
    double absBearing = e.getBearingRadians() + getHeadingRadians();//enemies absolute bearing
    double latVel = e.getVelocity() * Math.sin(e.getHeadingRadians() - absBearing);//enemies later velocity
    double gunTurnAmt;//amount to turn our gun
    setTurnRadarLeftRadians(getRadarTurnRemainingRadians());//lock on the radar
    setMaxVelocity(9);//randomly change speed
    if (e.getDistance() > 150) {//if distance is greater than 150
      gunTurnAmt = robocode.util.Utils.normalRelativeAngle(
          absBearing - getGunHeadingRadians() + latVel / 22);//amount to turn our gun, lead just a little bit
      setTurnGunRightRadians(gunTurnAmt); //turn our gun
      setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing - getHeadingRadians()
          + latVel / getVelocity()));//drive towards the enemies predicted future location
      setAhead((e.getDistance() - 140) * moveDirection);//move forward
      smartFire(e.getDistance());//fire
    } else {//if we are close enough...
      gunTurnAmt = robocode.util.Utils.normalRelativeAngle(
          absBearing - getGunHeadingRadians() + latVel / 15);//amount to turn our gun, lead just a little bit
      setTurnGunRightRadians(gunTurnAmt);//turn our gun
      setTurnLeft(-90 - e.getBearing()); //turn perpendicular to the enemy
      setAhead((e.getDistance() - 140) * moveDirection);//move forward
      smartFire(e.getDistance());//fire
    }
  }

  public void smartFire(double robotDistance) { // control fire power by distance and low energy
    if(getEnergy() < 25){
      this.fire(1.5D);
    }
    if (robotDistance > 50.0D) {
      this.fire(2.0D);
    }
    else{
      this.fire(3.0D);
    }
  }

  public void BulletMissedEvent(BulletMissedEvent event) {
    if (flag == 3) {
      setTurnGunRight(-20);
      flag = -1;
    }
    if (flag == -3) {
      setTurnGunRight(20);
      flag = 1;
    }
    if (flag == 1) {
      setTurnGunRight(10);
      flag ++;
    }
    if (flag == 2) {
      setTurnGunRight(10);
      flag ++;
    }
    if (flag == -1) {
      setTurnGunRight(10);
      flag --;
    }
    if (flag == -2) {
      setTurnGunRight(10);
      flag --;
    }
  }

  public void onHitRobot(HitRobotEvent event) {
    moveDirection = -moveDirection; //reverse direction upon hitting a enemy
  }

  public void onHitWall(HitWallEvent e) {
    moveDirection = -moveDirection; //reverse direction upon hitting a wall
  }

  public void onWin(WinEvent e) {
    stop();
    for (int i = 0; i < 50; i++) {
      turnRight(30);
      turnLeft(30);
    }
  }
}