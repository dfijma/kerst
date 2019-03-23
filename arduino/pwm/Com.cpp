
#include "Com.h"
#include <LocoNet.h>

// cmd parsing and execution

static boolean parseHexDigit(int& res, byte*& cmd) {
  byte b = *cmd;
  if (b>='0' && b<='9') {
    res = b-'0';
    cmd++;
    return true;
  } else if (b>='a' && b<='f') {
    res = b-'a' + 10;
    cmd++;
    return true;
  } else if (b>='A' && b<='F') {
    res = b-'A'+10;
    cmd++;
    return true;
  }
  return false;
}

static boolean parseHexByte(int &res, byte*& cmd) {
  int b1;
  if (!parseHexDigit(b1, cmd)) return false;
  if (!parseHexDigit(res, cmd)) return false;
  res = b1*16 + res; return true;
}

static boolean parseDigit(int& res, byte* &cmd, byte m='9') {
  byte b = *cmd;
  if (b>='0' && b<=m) {
    cmd++;
    res = b - '0';
    return true;
  }
  return false;
}

static boolean skipWhiteSpace(byte*& cmd) {
  while ((*cmd) == ' ') cmd++;
}

static boolean parseNumber(int& res, byte*& cmd, int max) {
  if (!parseDigit(res, cmd)) return false;
  int next;
  while (parseDigit(next, cmd)) { res = res*10 + next; }
  return res <= max;
}


static void parse(PwmDrive& pwmDrive, byte *cmd) {
  switch (*cmd) {
  case 'S': case 's': {
    cmd++;

    int channel;
    int speed;
    int direction;

    skipWhiteSpace(cmd);
    if (!parseNumber(channel, cmd, 1)) {
      Serial.println("invalid channel");
      return;
    }
    
    skipWhiteSpace(cmd);
    if (!parseNumber(speed, cmd, 255)) {
      Serial.println("invalid speed");
      return;
    }

    skipWhiteSpace(cmd);
    if (!parseNumber(direction, cmd, 1)) {
      Serial.println("invalid direction");
      return;
    }
    
    pwmDrive.set(channel, speed, direction==1);
    pwmDrive.sendState();
    break;
  }

  case 'P': case 'p': {
    pwmDrive.powerOn();
    pwmDrive.sendState();
    break;
  }
  
  case 'O': case 'o': {
    pwmDrive.powerOff();
    pwmDrive.sendState();
    break;
  }
 
  default:
    Serial.println("ERROR no command");
  }
}

void Com::setup(PwmDrive& pwmDrive) {
  Serial.begin(57600);
  pwmDrive.sendState();
}

void Com::executeOn(PwmDrive& pwmDrive) {
  // parse and execute received serial data
  while (Serial.available()) {
    byte b = Serial.read();
    if (b == '\r') continue; // bye windows
    if (b == '\n') {
      // EOL, parse it
      cmdBuffer[cmdLength++] = '\0';
      parse(pwmDrive, cmdBuffer);
      cmdLength = 0;
      continue;
    }

    // buffer byte, if there is room for it (safe one for terminating zero)
    if (cmdLength < (sizeof(cmdBuffer)-1)) {
      cmdBuffer[cmdLength] = b;
      cmdLength++;
    }   
  }
}

void Com::sendLoconet(lnMsg* packet) {
  uint8_t msgLen = getLnMsgSize(packet);
  Serial.print("LN");
  for (uint8_t x = 0; x < msgLen; x++) {
    uint8_t val = packet->data[x];
    Serial.print(" "); Serial.print(val, 16); 
  }
  Serial.println();
}

void Com::sendPowerState() {
  Serial.println("POFF");
}

