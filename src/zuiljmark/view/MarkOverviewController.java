package zuiljmark.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import zuiljmark.Main;
import zuiljmark.model.Rectangle;

import java.io.File;

/**
 * @author Mengde
 */
public class MarkOverviewController {
	@FXML
	private HBox myHBox;
    @FXML
    private ImageView myImageView;
    @FXML
    private TextArea myTextArea;
    @FXML
    private TableView<Rectangle> rectangleTable;
    @FXML
    private TableColumn<Rectangle, String> wordsColumn;
    
    private Main mainApp;
	private static String picPath = new File("./resources/testImage/test.jpg").toURI().toString();
    private Image image;
    
    Color RED = Color.RED;
    Color BLUE = Color.BLUE;
    Color GREEN = Color.GREEN;
    
    
    private double startX;
	private double startY;
	private double translateX;
	private double translateY;
	private double sX;
	private double sY;
	private double eX;
	private double eY;
	
	/**
	 * Change the size of Image View to fit the image.
	 */
	private void setImageView() {
		myImageView.setFitHeight(image.getHeight());
		myImageView.setFitWidth(image.getWidth());
	}

    /**
     * The constructor, which is called before the initialize() method.
     */
    public MarkOverviewController() {
    	
	}

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
    	System.out.println(picPath);

    	wordsColumn.setCellValueFactory(cellData -> cellData.getValue().wordsProperty());
    	// Add listener to rectangle table.
    	rectangleTable.itemsProperty().addListener(
    			(observable, oldValue, newValue) -> drawRects());
    	
        // Clear text details.
        showRectsDetails(null);

        // Listen for selection changes and show the text and rectangle details when changed.
        rectangleTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRectsDetails(newValue));
        
    }


    /**
     * get data from the rectangle list and draw those
     * rectangle on the image
     */
    private void drawRects() {
    	image = new Image(picPath);
		WritableImage wImage = new WritableImage(
	    		image.getPixelReader(),
	            (int)image.getWidth(),
	            (int)image.getHeight());
        for(Rectangle rect: mainApp.getRectangleData()) {
        	wImage = rect.drawSelf(wImage, RED);
        	System.out.println(picPath);
        }
        showPicture(wImage);
	}

	/**
	 *highlight the selected rectangle
	 */
	private void drawRects(Rectangle ctnrect) {
    	image = new Image(picPath);
		WritableImage wImage = new WritableImage(
	    		image.getPixelReader(),
	            (int)image.getWidth(),
	            (int)image.getHeight());
		for(Rectangle rect: mainApp.getRectangleData()) {
        	
        	if (rect == ctnrect) {
				wImage = rect.drawSelf(wImage, BLUE);
			}else {
				wImage = rect.drawSelf(wImage, RED);
			}
				
        }
        showPicture(wImage);
	}

	/**
	 *	draw a temporary rectangle before add a new rectangle
	 */
    private void drawRects(double sx, double sy, double ex, double ey) {
    	image = new Image(picPath);
    	WritableImage wImage = new WritableImage(
	    		image.getPixelReader(),
	            (int)image.getWidth(),
	            (int)image.getHeight());
        for(Rectangle rect: mainApp.getRectangleData()) {
        	wImage = rect.drawSelf(wImage, RED);
        }
        
        PixelWriter pixelWriter = wImage.getPixelWriter();
        for(int x = (int)sx; x <= (int)ex; x ++) {
			pixelWriter.setColor(x, (int)sy, GREEN);
		}
		
		for(int y = (int)sy; y <= (int)ey; y ++) {
			pixelWriter.setColor((int)sx, y, GREEN);
		}
		
		for(int y = (int)sy; y <= (int)ey; y ++) {
			pixelWriter.setColor((int)ex, y, GREEN);
		}
		
		for(int x = (int)sx; x <= (int)ex; x ++) {
			pixelWriter.setColor(x, (int)ey, GREEN);
		}
        showPicture(wImage);
	}

    /**
     * show the picture and handle those actions
     * @param wImage : writable image
     */
	private void showPicture(WritableImage wImage) {
		setImageView();
		try {
			myImageView.setImage(wImage);
			//handle Scroll events to control the size of image
			myImageView.setOnScroll(event -> {

				System.out.println("scorllevents!");

				if (event.getDeltaY() > 0) {
					myImageView.setScaleX(myImageView.getScaleX() * 1.2);
					myImageView.setScaleY(myImageView.getScaleY() * 1.2);
					event.consume();
					System.out.println("image resized!");
				} else {
					myImageView.setScaleX(myImageView.getScaleX() * 0.8);
					myImageView.setScaleY(myImageView.getScaleY() * 0.8);
					event.consume();
				}
			});
			//handle mouse events to control the position of image
			myImageView.setOnMousePressed(event -> {
				if (event.getButton() == MouseButton.SECONDARY) {

					System.out.println("rightPressed:x=" + event.getX() + ";y=" + event.getY());
					startX = event.getSceneX();
					startY = event.getSceneY();
					translateX = myImageView.getTranslateX();
					translateY = myImageView.getTranslateY();

				} else if (event.getButton() == MouseButton.PRIMARY) {

					System.out.println("leftPressed:x=" + event.getX() + ";y=" + event.getY());
					sX = event.getX();
					sY = event.getY();

				}
			});
			myImageView.setOnMouseMoved(event -> {

				if (event.getButton() == MouseButton.SECONDARY) {
					System.out.println("rightMoved:x=" + event.getX() + ";y=" + event.getY());
				}
			});
			myImageView.setOnMouseReleased(event -> {
				if (event.getButton() == MouseButton.SECONDARY) {
					System.out.println("rightRelease:x=" + event.getX() + ";y=" + event.getY());
				}else if (event.getButton() == MouseButton.PRIMARY) {
					System.out.println("leftPressed:x=" + event.getX() + ";y=" + event.getY());
					eX = event.getX();
					eY = event.getY();
					drawRects(sX, sY, eX, eY);
					rectangleTable.getSelectionModel().select(null);
				}
				event.consume();
			});
			myImageView.setOnDragDetected(event -> {

				if (event.getButton() == MouseButton.SECONDARY) {
					System.out.println("OnDragDetected" + event.getX() + ";y=" + event.getY());
					Dragboard db = myImageView.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putString("hahahahh");
					db.setContent(content);
					event.consume();
				}
			});
			myImageView.setOnDragOver(event -> {
				System.out.println("onDragOver:" + (event.getSceneX()-startX) + ";"
						+ "y=" + (event.getSceneY()-startY));
				myImageView.setTranslateX(translateX +event.getSceneX()-startX);
				myImageView.setTranslateY(translateY +event.getSceneY()-startY);
			});
			myImageView.setOnMouseClicked(event -> {

				if (event.getClickCount() == 2) {
					System.out.println("double click");
					for (Rectangle rect : mainApp.getRectangleData()) {
						if (rect.getStartX() <= event.getX()
								&& event.getX() <= rect.getEndX()
								&& rect.getStartY() <= event.getY()
								&& event.getY() <= rect.getEndY()) {
							drawRects(rect);
							myTextArea.setText(rect.getWords());
							rectangleTable.getSelectionModel().select(rect);
						}
					}
					if (rectangleTable.getSelectionModel().getSelectedItem() == null) {
						rectangleTable.getSelectionModel().select(null);
					}
				}
			}
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    
    
    /**
     * Fills all text fields to show details about the rectangle.
     * If the specified rectangle is null,text field is cleared.
     */
    private void showRectsDetails(Rectangle rectangle) {
        if (rectangle != null) {
            // Fill the labels with info from the rectangle object.
            myTextArea.setText(rectangle.getWords());
            drawRects(rectangle);
			mainApp.getPrimaryStage().getScene().addEventFilter(KeyEvent.KEY_PRESSED, (event) -> {
            	
    			System.out.println("keyevent!!");
    			System.out.println(rectangleTable.getSelectionModel().getSelectedItem().getEndX() + ","
    					 + rectangleTable.getSelectionModel().getSelectedItem().getEndY());
    			switch (event.getCode()) {
    			case UP:{
    				rectangleTable.getSelectionModel().getSelectedItem().setEndY(
    						rectangleTable.getSelectionModel().getSelectedItem().getEndY()
    						- myImageView.getFitHeight()/100 );
    				drawRects(rectangleTable.getSelectionModel().getSelectedItem());
    				Main.flag = false;
    				event.consume();
    				break;
    			}
    			case DOWN:{
    				rectangleTable.getSelectionModel().getSelectedItem().setEndY(
    						rectangleTable.getSelectionModel().getSelectedItem().getEndY()
    						+ myImageView.getFitHeight()/100 );
    				drawRects(rectangleTable.getSelectionModel().getSelectedItem());
    				Main.flag = false;
    				event.consume();
    				break;
    			}
    			case LEFT:{
    				rectangleTable.getSelectionModel().getSelectedItem().setEndX(
    						rectangleTable.getSelectionModel().getSelectedItem().getEndX()
    						- myImageView.getFitHeight()/100 );
    				drawRects(rectangleTable.getSelectionModel().getSelectedItem());
    				Main.flag = false;
    				event.consume();
    				break;
    			}
    			case RIGHT:{
    				rectangleTable.getSelectionModel().getSelectedItem().setEndX(
    						rectangleTable.getSelectionModel().getSelectedItem().getEndX()
    						+ myImageView.getFitHeight()/100 );
    				drawRects(rectangleTable.getSelectionModel().getSelectedItem());
    				Main.flag = false;
    				event.consume();
    				break;
    			}
    			case W:{
    				rectangleTable.getSelectionModel().getSelectedItem().adjustRectY(
    						-myImageView.getFitHeight()/100);
    				drawRects(rectangleTable.getSelectionModel().getSelectedItem());
    				Main.flag = false;
    				event.consume();
    				break;
    			}
    			case S:{
    				rectangleTable.getSelectionModel().getSelectedItem().adjustRectY(
    						myImageView.getFitHeight()/100);
    				drawRects(rectangleTable.getSelectionModel().getSelectedItem());
    				Main.flag = false;
    				event.consume();
    				break;
    			}
    			case A:{
    				rectangleTable.getSelectionModel().getSelectedItem().adjustRectX(
    						-myImageView.getFitHeight()/100);
    				drawRects(rectangleTable.getSelectionModel().getSelectedItem());
    				Main.flag = false;
    				event.consume();
    				break;
    			}
    			case D:{
    				rectangleTable.getSelectionModel().getSelectedItem().adjustRectX(
    						myImageView.getFitHeight()/100);
    				drawRects(rectangleTable.getSelectionModel().getSelectedItem());
    				Main.flag = false;
    				event.consume();
    				break;
    			}
    			default:
    				break;
    			}
            }
            );
            
        } else {
            // rectangle is null, remove the text.
            myTextArea.setText("");
        }
    }
    
    @FXML
    private void handleOK() {
    	if (rectangleTable.getSelectionModel().getSelectedItem() == null) {

			Rectangle tempRect = new Rectangle(sX, sY, eX, eY);
    		tempRect.setWords(myTextArea.getText());
    		System.out.println(tempRect.getStartX() + "," + tempRect.getStartY() 
    		+ "," + tempRect.getEndX() + "," + tempRect.getEndY());
    		mainApp.getRectangleData().add(tempRect);
    		tempRect = null;
    		Main.flag = false;
    		
		} else {
			if (rectangleTable.getSelectionModel().getSelectedItem() != null) {
				rectangleTable.getSelectionModel().getSelectedItem().setWords(myTextArea.getText());
			}
		}
	}
    
    @FXML
    private void handleDelete() {
    	    int selectedIndex = rectangleTable.getSelectionModel().getSelectedIndex();
    	    if (selectedIndex >= 0) {
    	        rectangleTable.getItems().remove(selectedIndex);
    	    } else {
    	        System.out.println("nothing selected!!");
    	    }
	}
     
    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
     // Add observable list data to the table
        rectangleTable.setItems(mainApp.getRectangleData());
    }
    
    /**
     * A static method to set the path of picture
     * @param string
     */
	public static void setPicPath(String string) {
		picPath = string;
		System.out.println("picPath changed :" + picPath);
	}
    
}
