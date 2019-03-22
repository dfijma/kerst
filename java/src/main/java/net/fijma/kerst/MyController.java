package net.fijma.kerst;

import net.fijma.mvc.Application;
import net.fijma.mvc.Controller;
import net.fijma.mvc.Msg;
import net.fijma.mvc.serial.Serial;

public class MyController extends Controller<App, MyModel, MyMainView> {

    MyController(App app, MyModel model, MyMainView view) {
        super(app, model, view);
        // wire model -> views
        model.onSomethingChanged.attach(view::somethingChanged);
        // wire views -> controller
        view.down.attach(this::onDownEvent);
        view.up.attach(this::onUpEvent);
    }

    private void onUpEvent(Void v) {
        model.inc();
    }

    private void onDownEvent(Void v) {
        model.dec();
    }

    @Override
    protected boolean onEvent(Msg msg) {
        if (msg instanceof Application.KeyMsg) {
            return mainView.key(((Application.KeyMsg)msg).key);
        } else if (msg instanceof Serial.SerialMsg) {
            return true; // ignore
        }
        return true; // catch-all, ignore and continue
    }

}
