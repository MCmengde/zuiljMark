package zuiljmark.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Model class for a Person.
 *
 * @author Mengde
 */
public class Rectangle {
	
    private final StringProperty words;
    private final DoubleProperty startX;
    private final DoubleProperty startY;
    private final DoubleProperty endX;
    private final DoubleProperty endY;


    /**
     * Constructor with some initial data.
     * 
     */
    public Rectangle(double startX, double startY, double endX, double endY) {
        this.startX = new SimpleDoubleProperty(startX);
        this.startY = new SimpleDoubleProperty(startY);

        // Some initial dummy data, just for convenient testing.
        this.endX = new SimpleDoubleProperty(endX);
        this.endY = new SimpleDoubleProperty(endY);
        this.words = new SimpleStringProperty("Example");
    }


    public WritableImage drawSelf(WritableImage myImage, Color clr) {
    	
    	System.out.println((int)this.getStartX() + "," + (int)this.getStartY());
    	System.out.println((int)this.getEndX() + "," + (int)this.getEndY());
    	PixelWriter pixelWriter = myImage.getPixelWriter();
    	
		for(int x = (int)this.getStartX(); x <= (int)this.getEndX(); x ++) {
			pixelWriter.setColor(x, (int)this.getStartY(), clr);
		}
		
		for(int y = (int)this.getStartY(); y <= (int)this.getEndY(); y ++) {
			pixelWriter.setColor((int)this.getStartX(), y, clr);
		}
		
		for(int y = (int)this.getStartY(); y <= (int)this.getEndY(); y ++) {
			pixelWriter.setColor((int)this.getEndX(), y, clr);
		}
		
		for(int x = (int)this.getStartX(); x <= (int)this.getEndX(); x ++) {
			pixelWriter.setColor(x, (int)this.getEndY(), clr);
		}
    	
		return myImage;
	}

    public String getWords() {
        return words.get();
    }

    public void setWords(String words) {
        this.words.set(words);
    }

    public StringProperty wordsProperty() {
        return words;
    }

    public double getStartX() {
        return startX.get();
    }

    public double getStartY() {
        return startY.get();
    }

    public double getEndX() {
        return endX.get();
    }

    public void setEndX(double endX) {
        this.endX.set(endX);
    }

    public double getEndY() {
        return endY.get();
    }

    public void setEndY(double endY) {
        this.endY.set(endY);
    }

    public void adjustRectX(double x) {
		this.startX.set(this.startX.get() + x);
		this.endX.set(this.endX.get() + x);
	}
    public void adjustRectY(double x) {
		this.startY.set(this.startY.get() + x);
		this.endY.set(this.endY.get() + x);
	}
}