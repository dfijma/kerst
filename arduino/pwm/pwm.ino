
#include "Config.h"
#include "Com.h"

// com (it means both "command" and "communication" :-)
Com com;

// pwm driver
PwmDrive pwmDrive;

void setup() {
  pwmDrive.setup();
  com.setup(pwmDrive);
}

void loop() {
  // cmd execution on new serial data
  com.executeOn(pwmDrive);  
}
