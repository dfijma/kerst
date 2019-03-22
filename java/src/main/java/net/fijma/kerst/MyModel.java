package net.fijma.kerst;

import net.fijma.mvc.Event;
import net.fijma.mvc.serial.Serial;

import java.io.IOException;
import java.util.logging.Logger;

public class MyModel {

    private static final Logger LOGGER = Logger.getLogger(MyModel.class.getName());
    private final Serial serial;

    final Event<Integer> onSomethingChanged = new Event<>();

    // the actual model
    private int value;
    private boolean power;

    public MyModel(Serial serial) {
        this.serial = serial;
        value = 0;
        power = false;
    }

    public void inc() {
        value++;
        if (value > 255) value = 255;
        LOGGER.info(() -> String.format("inc: value is now: %d", value));
        onSomethingChanged.trigger(value);
        try {
            serial.write(String.format("s 0 %d 1\n", value));
        } catch (IOException e) {
            // TODO
        }
    }

    public void dec() {
        value--;
        if (value <0) value = 0;
        LOGGER.info(() -> String.format("dec: value is now: %d", value));
        onSomethingChanged.trigger(value);
        try {
            serial.write(String.format("s 0 %d 1\n", value));
        } catch (IOException e) {
            // TODO
        }
    }

    public void setPower(boolean power) {
        this.power = power;
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
}