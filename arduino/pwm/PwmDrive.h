#pragma once

#include <Arduino.h>
#include "Config.h"

class PwmDrive {
  public:
    void setup();
    void set(int channel, int speed, boolean direction);
    void powerOn() { brake = false; setOutput(); }
    void powerOff() { brake = true; setOutput(); }
    void sendState();

  private:
     int speed0;
     int speed1;
     boolean direction0;
     boolean direction1;
     boolean brake;

     void setOutput();
};

