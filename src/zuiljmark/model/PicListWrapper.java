package zuiljmark.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Helper class to wrap a list of rectangle. This is used for saving the
 * list of rectangles to XML.
 * 
 * @author Mengde
 */
@XmlRootElement(name = "rectangles")
public class PicListWrapper {

    private List<Rectangle> rectangles;

    @XmlElement(name = "rectangle")
    public List<Rectangle> getRectangles() {
        return rectangles;
    }

    public void setRectangles(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }
}