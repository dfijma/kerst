#include "PwmDrive.h"

void PwmDrive::setup() {

  pinMode(DIR_A, OUTPUT);
  pinMode(BRAKE_A, OUTPUT);

  pinMode(DIR_B, OUTPUT);
  pinMode(BRAKE_B, OUTPUT);
  
  speed0 = 0;
  speed1 = 0;
  direction0 = true;
  direction1 = true;
  brake = true;
  setOutput();
}

  /*
  Serial.print(speed0); Serial.print(direction0); Serial.println();
  Serial.print(speed1); Serial.print(direction1); Serial.println();
  Serial.print(brake); Serial.println();
  */
  
void PwmDrive::set(int channel, int speed, boolean direction) {
  if (channel == 0) {
    speed0 = speed;
    direction0 = direction;
  } else {
    speed1 = speed;
    direction1 = direction;
  }
  setOutput();
}

void PwmDrive::setOutput() {

  digitalWrite(BRAKE_A, brake);
  digitalWrite(BRAKE_B, brake);

  digitalWrite(DIR_A, direction0);
  digitalWrite(DIR_B, direction1);
  
  analogWrite(SPEED_A, speed0);
  analogWrite(SPEED_B, speed1);
}

