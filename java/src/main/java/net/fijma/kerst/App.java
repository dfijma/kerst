package net.fijma.kerst;

import net.fijma.mvc.Application;
import net.fijma.mvc.serial.Serial;
import org.apache.commons.cli.*;

import java.util.logging.Logger;

public class App extends Application {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("kerst", options);
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        new App().init(args);
    }

    private void init(String[] args) throws Exception {

        Options options = new Options();
        options.addOption("p", false, "probe serial ports");
        options.addOption(Option.builder("d").optionalArg(false).hasArg().argName("device").desc("serial port device").build());

        CommandLineParser parser = new DefaultParser();
        String device = null;

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("p")) {
                Serial.probe();
                System.exit(0);
            }
            if (cmd.hasOption("d")) {
                device = cmd.getOptionValue("d");
            } else {
                usage(options);
            }

        } catch (ParseException e) {
            System.err.println(e.getMessage());
            usage(options);
        }

        LOGGER.info("start using: " + device);
        exec(device);
    }

    private void exec(String device) throws Exception {


        // register optional module(s)
        Serial s = new Serial(this,device);
        s.start();
        registerModule(s);

        App app = new App();
        MyModel model = new MyModel(getModule(Serial.class));
        MyMainView view = new MyMainView(model);
        MyController controller = new MyController(app, model, view);

        app.run(controller);
        s.stop();
    }

}
