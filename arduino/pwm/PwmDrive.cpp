#include "PwmDrive.h"

void PwmDrive::setup() {

// TCCR2B = TCCR2B & B11111000 | B00000001; // for PWM frequency of 31372.55 Hz

//TCCR2B = TCCR2B & B11111000 | B00000010; // for PWM frequency of 3921.16 Hz

//TCCR2B = TCCR2B & B11111000 | B00000011; // for PWM frequency of 980.39 Hz

TCCR2B = TCCR2B & B11111000 | B00000100; // for PWM frequency of 490.20 Hz (The DEFAULT)

// TCCR2B = TCCR2B & B11111000 | B00000101; // for PWM frequency of 245.10 Hz

// TCCR2B = TCCR2B & B11111000 | B00000110; // for PWM frequency of 122.55 Hz

// TCCR2B = TCCR2B & B11111000 | B00000111; // for PWM frequency of 30.64 Hz
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

void PwmDrive::sendState() {
  Serial.print("OK "); Serial.print(speed0); Serial.print(" "); Serial.print(direction0); Serial.print(" ");
  Serial.print(speed1); Serial.print(" "); Serial.print(direction1); Serial.print(" ");
  Serial.print(brake); Serial.println();
}

