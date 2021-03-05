package zuiljmark;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import zuiljmark.model.PicListWrapper;
import zuiljmark.model.Rectangle;
import zuiljmark.view.MarkOverviewController;
import zuiljmark.view.RootLayoutController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;


/**
 * The main class to start the app.
 *
 * @author Mengde
 */
public class Main extends Application {

    public static boolean flag = true;

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Rectangles.
     */
    private final ObservableList<Rectangle> rectangleData = FXCollections.observableArrayList();

    /**
     * The constructor of the main class.
     * When the class is initialized, an new rectangle will be add to the rectangleData.
     */
    public Main() {
        // an example rectangle
        rectangleData.add(new Rectangle(20.0, 20.0, 100.0, 100.0));
    }


    @Override
    public void start(Stage primaryStage) {
        // Initial the stage and set title to the window
        this.setPrimaryStage(primaryStage);
        this.getPrimaryStage().setTitle("zuiljmark");

        // Initial the root layout
        initRootLayout();
        // Initial the mark overview
        showMarkOverview();

        // Ensure the files saved before closed
        primaryStage.setOnCloseRequest(event -> {
            if (!Main.flag) {
                saveOrNot();
            }
        });
    }


    /**
     * Initialize the root layout and try to load the example picture file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout
            Scene scene = new Scene(rootLayout);
            getPrimaryStage().setScene(scene);

            // Give the controller access to the main app
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            getPrimaryStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the data as an observable list of Rectangles.
     */
    public ObservableList<Rectangle> getRectangleData() {
        return rectangleData;
    }

    /**
     * Return the main stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Set primary stage.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Remove all elements in current list and then add a new elements to the new list.
     */
    public void rmAll() {
        rectangleData.clear();
        rectangleData.add(new Rectangle(20.0, 20.0, 100.0, 100.0));
    }

    /**
     * Shows mark overview inside the root layout.
     */
    public void showMarkOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MarkOverview.fxml"));
            AnchorPane markOverview = loader.load();

            // Set mark overview into the center of root layout.
            rootLayout.setCenter(markOverview);

            // Give the controller access to the main app.
            MarkOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Return the picture file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     */
    public File getPicFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setPicFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            getPrimaryStage().setTitle("zuiljMark - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            getPrimaryStage().setTitle("zuiljMark");
        }
    }

    /**
     * Load picture data from the specified file.
     * The current person data will be replaced.
     */
    public void loadPicDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PicListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            PicListWrapper wrapper = (PicListWrapper) um.unmarshal(file);

            //update the rectangle data list
            rectangleData.clear();
            rectangleData.addAll(wrapper.getRectangles());

            // Save the file path to the registry.
            setPicFilePath(file);
        } catch (Exception e) {
            // catches ANY exception
            Dialogs.create()
                    .title("Error")
                    .masthead("Could not load data from file:\n" + file.getPath())
                    .showException(e);
        }
    }

    /**
     * Saves the current picture data to the specified file.
     */
    public void savePicDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PicListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            PicListWrapper wrapper = new PicListWrapper();
            wrapper.setRectangles(rectangleData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setPicFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Dialogs.create().title("Error")
                    .masthead("Could not save data to file:\n" + file.getPath())
                    .showException(e);
        }
    }

    /**
     * Ask if you want to save the file when closing the window.
     */
    public void saveOrNot() {
        Alert alert;
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Tips");
        alert.setHeaderText("Do you want to save it?");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            File picFile = getPicFilePath();
            if (picFile != null) {
                savePicDataToFile(picFile);
            } else {
                FileChooser fileChooser = new FileChooser();

                // Set extension filter
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("XMl files", ".xml"));

                // Show save file dialog
                File file = fileChooser.showSaveDialog(getPrimaryStage());

                if (file != null) {
                    // Make sure it has the correct extension
                    if (!file.getPath().endsWith(".xml")) {
                        file = new File(file.getPath() + ".xml");
                    }
                    savePicDataToFile(file);
                }
            }
            System.exit(0);
            // ... user choose "Yes"
        } else if (result.get() == buttonTypeTwo) {
            System.exit(0);
            // ... user choose "No"
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}