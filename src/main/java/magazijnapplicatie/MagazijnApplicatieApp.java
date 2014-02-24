/*
 * MagazijnApplicatieApp.java
 */

package magazijnapplicatie;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class MagazijnApplicatieApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        try {
            show(new Magazijn());
        } catch (Exception ex) {
            Logger.getLogger(MagazijnApplicatieApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of MagazijnApplicatieApp
     */
    public static MagazijnApplicatieApp getApplication() {
        return Application.getInstance(MagazijnApplicatieApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(MagazijnApplicatieApp.class, args);
    }
}
