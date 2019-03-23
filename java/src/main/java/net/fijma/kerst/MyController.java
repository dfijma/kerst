package net.fijma.kerst;

import net.fijma.mvc.Application;
import net.fijma.mvc.Controller;
import net.fijma.mvc.Msg;
import net.fijma.mvc.serial.Serial;

import java.util.logging.Logger;

public class MyController extends Controller<App, MyModel, MyMainView> {

    public static final Logger LOGGER = Logger.getLogger(MyMainView.class.getName());

    MyController(App app, MyModel model, MyMainView view) {
        super(app, model, view);
        // wire model -> views
        model.speedChanged.attach(view::onSpeedChanged);
        model.powerChanged.attach(view::onPowerChanged);
        // wire views -> controller
        view.down.attach(this::onDownEvent);
        view.up.attach(this::onUpEvent);
    }

    private void onUpEvent(int channel) {
        model.inc(channel);
    }

    private void onDownEvent(int channel) {
        model.dec(channel);
    }

    @Override
    protected boolean onEvent(Msg msg) {
        if (msg instanceof Application.KeyMsg) {
            return mainView.key(((Application.KeyMsg)msg).key);
        } else if (msg instanceof Serial.SerialMsg) {
            LOGGER.info("serial: " +((Serial.SerialMsg)msg).line);
            return true; // ignore
        }
        return true; // catch-all, ignore and continue
    }

}
