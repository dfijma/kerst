package net.fijma.kerst;

import net.fijma.mvc.Application;
import net.fijma.mvc.serial.Serial;

/**
 * Hello world!
 *
 */
public class App extends Application {

    public static void main( String[] args ) throws Exception {
        App app = new App();
        MyModel model = new MyModel();
        MyMainView view = new MyMainView(model);
        MyController controller = new MyController(app, model, view);
        // run
        Serial.probe();
        app.run(controller);
    }

    public App() {
        super();
        // register optional module(s)
        registerModule(new Serial(this,"/dev/cu.Bluetooth-Incoming-Port"));
    }


}
