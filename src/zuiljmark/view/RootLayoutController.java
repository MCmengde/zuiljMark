package zuiljmark.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import zuiljmark.Main;

import java.io.File;
import java.util.Objects;
import java.util.Optional;


/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 * 
 * @author mengde
 */
public class RootLayoutController {

    /**
     *    Reference to the main application
     */
    private Main mainApp;
    
    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleOpen() {
    	if(!Main.flag) {

    		Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Tips");
        	alert.setHeaderText("Do you want to save it?");
        	alert.setContentText("Choose your option.");

    		ButtonType buttonTypeOne = new ButtonType("Yes");
    		ButtonType buttonTypeTwo = new ButtonType("No");
    		

    		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.get() == buttonTypeOne){
    			handleSave();
    			openFile();
    		    // ... user choose "One"
    		} else if (result.get() == buttonTypeTwo) {
    			openFile();
    		    // ... user choose "Two"
    		} 
    	}
    	else {
    		openFile();
    	}
    	Main.flag = true;
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File picFile = mainApp.getPicFilePath();
        if (picFile != null) {
            mainApp.savePicDataToFile(picFile);
        } else {
            handleSaveAs();
        }
        Main.flag = true;
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        fileChooser.getExtensionFilters().addAll(
        		new ExtensionFilter("XMl files", ".xml"));

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.savePicDataToFile(file);
        }
        Main.flag = true;
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("How to use the software");
    	alert.setHeaderText("when you get the software for the fisrt time, you maybe don't know the usage of it, so the specific steps are as following");
    	alert.setContentText("1.Load an image.  Click on the \"File\" item and select the \"Open\" button to open the image you want to label in the folder.\n"+ "\n"
    	           		   +"2.The movement and scaling of the picture.  Click the \"Refresh\" button and the image will appear in the action box. When you\n"
    			           +"   manipulate the picture, the mouse must be on the picture. Press and hold the right mouse button to move the image, and slide the\n"
    			           +"   wheel to zoom in and out\n" + "\n"
    			           +"3.The label of the picture.  After placing the image in the appropriate position and adjusting the appropriate size, hold down the\n"
    			           +"   left mouse button to draw a rectangle on the image, click the \"New\" button to clear the text box, and edit the corresponding \n"
    			           +"   character in the text box, click the \"OK\" button That is, complete the labeling. Repeat the above steps to complete multiple \n"
    			           +"   annotations." 
    			           + "\n" + "\n"+"4.Save the picture.  Save the marked picture using the \"ctrl\" + \"s\" action on the keyboard");

    	alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Tips");
    	alert.setHeaderText("Do you want to save it?");
    	alert.setContentText("Choose your option.");

    	ButtonType buttonTypeOne = new ButtonType("Yes");
    	ButtonType buttonTypeTwo = new ButtonType("No");
    	ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

    	alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == buttonTypeOne){
    		handleSave();
    		System.exit(0);
    	    // ... user choose "Yes"
    	} else if (result.get() == buttonTypeTwo) {
    		 System.exit(0);
    	    // ... user choose "No"
        }
    }
    
    /**
     * used to search all sons of the current directory for 
     * the image file or the xml file
     * @param filenameSuffix : extension of file
     * @param currentDirUsed : the directory searching on
     */
    private String findFiles(String filenameSuffix, String currentDirUsed) {  
        File dir = new File(currentDirUsed);  
        if (!dir.exists() || !dir.isDirectory()) {  
            return "0"; 
        }  
  
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {   
                findFiles(filenameSuffix, file.getAbsolutePath());  
            } else {   
                if (file.getAbsolutePath().endsWith(filenameSuffix)) {  
                    return file.getAbsolutePath();
                }
            }  
        }
		return "0";  
    }
    /**
     * remove the extension of the file name
     */
    public static String getFileNameNoEx(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length()))) { 
                return filename.substring(0, dot); 
            } 
        } 
        return filename; 
    }
    
    private void openFile() {
    	FileChooser fileChooser = new FileChooser();
        
        // Set extension filter
        fileChooser.getExtensionFilters().addAll(
        		new ExtensionFilter("Image files", "*.jpg", "*.png"),
        		new ExtensionFilter("XML files", "*.xml"));

        // Show save file dialog
        File orginFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        System.out.println("orginpath" + orginFile.toString());
        System.out.println("name:" + getFileNameNoEx(orginFile.getName ()));
        if (orginFile.getName().endsWith(".xml")) {
        	//send xml file to mainApp
        	mainApp.loadPicDataFromFile(orginFile);
        	//find the corresponding image file
        	if (!"0".equals(findFiles(getFileNameNoEx(orginFile.getName()) + ".jpg", orginFile.getParent()))) {
				MarkOverviewController.setPicPath("file:" + getFileNameNoEx(orginFile.toString()) + ".jpg");
			} else if (!"0".equals(findFiles(getFileNameNoEx(orginFile.getName()) + ".png", orginFile.getParent()))) {
				MarkOverviewController.setPicPath("file:" + getFileNameNoEx(orginFile.toString()) + ".jpg");
			}
		} else if (orginFile.toString().endsWith(".jpg") || 
				orginFile.toString().endsWith(".png")) {
			System.out.println("current image" + orginFile.getName ());
			MarkOverviewController.setPicPath("file:" + orginFile.toString());
			if (!"0".equals(findFiles(getFileNameNoEx(orginFile.getName()) + ".xml", orginFile.getParent()))) {
				mainApp.loadPicDataFromFile(new File(findFiles(getFileNameNoEx(orginFile.getName()) 
						+ ".xml", orginFile.getParent())));
				System.out.println("current xml" + orginFile.getParent()
						+ orginFile.getName() + ".xml");
			} else {
				mainApp.rmAll();
				mainApp.savePicDataToFile(new File(getFileNameNoEx(orginFile.toString()) + ".xml"));
				mainApp.loadPicDataFromFile(new File(getFileNameNoEx(orginFile.toString()) + ".xml"));
				System.out.println("new xml" +orginFile.getParentFile().toString() 
						+ getFileNameNoEx(orginFile.getName()) + ".xml");
			}
		}
    }
}







