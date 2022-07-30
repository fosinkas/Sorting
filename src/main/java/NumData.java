import javafx.beans.property.SimpleIntegerProperty;

/**
 * This is the NumData class that creates the values in every table cell in table view in the program.
 * Each NumData contains an integer that will be displayed in the cell.
 * 
 * @author Nicole Jin 
 * @version 2021-12-06
 */
public class NumData {
    
    private final SimpleIntegerProperty numValue;

    /**
     * The NumData method is the default constructer of NumData class.
     * The SimpleIntegerProperty is initialized here.
     * 
     * @param numIn is the integer value contained in the SimpleIntegerProperty.
     */
    public NumData(int numIn){
        this.numValue = new SimpleIntegerProperty(numIn);
    }

    /**
     * The getNum method is the getter method for the integer value that is contained in the SimpleIntegerProperty.
     * 
     * @return num is the integer value that is contained.
     */
    public int getNum(){
        int num = this.numValue.get();
        return num;
    }

    /**
     * The numValueProperty method is the getter method for the SimpleIntegerProperty.
     * 
     * @return this.numValue is the SimpleIntegerProperty value.
     */
    public SimpleIntegerProperty numValueProperty(){
        return this.numValue;
    }
}
