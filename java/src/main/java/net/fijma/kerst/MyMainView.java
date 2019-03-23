package net.fijma.kerst;

import net.fijma.mvc.View;
import net.fijma.mvc.Event;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class MyMainView extends View<MyModel> {

    public static final Logger LOGGER = Logger.getLogger(MyMainView.class.getName());

    final Event<Integer> up = new Event<>();
    final Event<Integer> down = new Event<>();

    MyMainView(MyModel model) {
        super(model);
    }

    void onSpeedChanged(int channel) {
        setRC(2+channel,4);
        int v = model.getSpeed(channel);
        System.out.print((v < 0 ? red() : green()) + String.format("%3s", Math.abs(v)) + reset());
    }

    void onPowerChanged(Boolean power) {
        setRC(2, 42);
        if (power) {
            System.out.print(green() + " ON" + reset());
        } else {
            System.out.print(red() + "OFF" +  reset());
        }
    }

    void onSerialStateChanged(String line) {
        setRC(10, 2);
        System.out.print(String.format("%78s", line));
    }

    @Override
    public void draw() {
        red();
        try {
            List<String> lines = Files.readAllLines(Paths.get("screen.txt"), Charset.forName("UTF-8"));
            for (int i=0; i<Math.min(24, lines.size()); ++i) {
                setRC(i+1, 1);
                System.out.print(lines.get(i).replace("\n", ""));
            }
        } catch (IOException e) { throw new RuntimeException(e); }
        onSpeedChanged(0);
        onSpeedChanged(1);
        onPowerChanged(model.getPower());
    }

    @Override
    public boolean key(int k) {
        switch (k) {
            case 81: // Q, quit
            case 113:
                return false;
            case 77: // M
            case 109:
                up.trigger(1);
                break;
            case 78: // N
            case 110:
                down.trigger(1);
                break;
            case 90: // Z
            case 122:
                down.trigger(0);
                break;
            case 88: // X
            case 120:
                up.trigger(0);
                break;
            case 79: // P
            case 111:
                model.setPower(false);
                break;
            case 80: // P
            case 112:
                model.setPower(true);
                break;
            default:
                ; // ?
        }
        return true;
    }
}

