package net.fijma.kerst;

import net.fijma.mvc.Event;
import net.fijma.mvc.serial.Serial;

import java.io.IOException;
import java.util.logging.Logger;

public class MyModel {

    private static final Logger LOGGER = Logger.getLogger(MyModel.class.getName());
    private final Serial serial;

    final Event<Integer> speedChanged = new Event<>();
    final Event<Boolean> powerChanged = new Event<>();
    final Event<String> serialStateChanged = new Event<>();

    // the actual model
    private int[] speed;
    private boolean power;

    public MyModel(Serial serial) {
        this.serial = serial;
        speed = new int[] {0, 0}; // two channels
        power = false;
    }

    public void inc(int channel) {
        speed[channel] = Math.min(speed[channel]+1, 255);
        speedChanged.trigger(channel);
        logState();
        sendSpeed(channel);
    }

    public void dec(int channel) {
        speed[channel] = Math.max(speed[channel]-1, -255);
        speedChanged.trigger(channel);
        logState();
        sendSpeed(channel);
    }

    public int getSpeed(int channel) {
        return speed[channel];
    }

    private void sendSpeed(int channel) {
        try {
            int v = speed[channel];
            serial.write(String.format("s %d %d %d\n", channel, Math.abs(v), v < 0 ? 0 : 1));
        } catch (IOException e) {
            // TODO
        }
    }

    public void setPower(boolean power) {
        this.power = power;
        powerChanged.trigger(power);
        logState();
        try {
            if (power) {
                serial.write("p\n");
            } else {
                serial.write("o\n");
            }
        } catch (IOException e) {
            // TODO
        }
    }

    public boolean getPower() {
        return power;
    }

    public void onSerialMsg(Serial.SerialMsg msg) {
        final String line = msg.line;
        LOGGER.info(() -> "serial message: " + line);
        serialStateChanged.trigger(line);
    }

    private void logState() {
        LOGGER.info(() -> "A=" + speed[0] + " B=" + speed[1] + " POWER=" + (power ? "ON" : "OFF"));
    }
}