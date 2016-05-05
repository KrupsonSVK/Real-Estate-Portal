import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends Application {

    public static Stage parentWindow;

    public static void main(String[] args) {

        BasicConfigurator.configure();
        Application.launch(Main.class, (java.lang.String[]) null);
    }
// FUNGUJE :D ;D;D;D DAKY DOPYT

    @Override
    public void start(Stage primaryStage) throws IOException {
//co stym zasa je :D :D
        try {
            parentWindow = primaryStage;
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("mainView.fxml"));
            Scene scene = new Scene(root);
            parentWindow.setScene(scene);
            parentWindow.setTitle("Realitný portál");
            parentWindow.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
//preco su tam dva mainy :D ? SKUSIMTO VYPNT ZAPNUT? NOOO ... ONO TO JE KDE TIE FXMLKA?
        //kde je druhy >D neveim co to ma znamenat
    }
}
