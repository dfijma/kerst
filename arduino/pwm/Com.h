#pragma once

#include <Arduino.h>
#include "Config.h"
#include <LocoNet.h>

#include "PwmDrive.h"

//// serial communication and command execution

// Notications and responses from controller to host:

// # On setup:
// HELO

// Forward received Loconet package
//   LN A0 11 22 F0
//   - raw packet, including checksum

// Unsollicitated power off (overload)
//   POFF

// ERROR in command
//   ERROR <string>

// Confirmation of command
//   OK <string>

// Commands from host to controller

// set channel:
//   S <channel> <speed> <direction>
//   -example: S 2 126 1 1 0000 0000 0000 
//   -<channel> is 0 (A) or 1 (B)
//   -<speed> is 0..255 (PWM duty cycle)
//   -<direction> is 1: forward, 0: backwards

// send loconet packet
//   L <byte>
//   L A0 11 22
//   max 15 bytes, no checkum, this is calculated 

// power on
//   P
//   - switch on track power

// power off
//   O
//   - switch off track power


class Com {
  public:
    Com() {};
    void setup();
    void executeOn(PwmDrive& pwmDrive);
    void sendLoconet(lnMsg* packet);
    void sendPowerState();
  private:
    byte cmdBuffer[80];
    int cmdLength=0;
};
