import java.io.IOException;

import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Collections;
import java.util.Random;

/**
 * This is the SecondaryController class that controls the secondary scene.
 * 
 * @author Nicole Jin 
 * @version 2021-12-06
 */
public class SecondaryController {

    @FXML private TableView<NumData> tableView;
    @FXML private TableColumn<NumData, Number> numCol;
    @FXML private TextField numInTextField;
    @FXML private Button passButton;
    @FXML private Button stepButton;
    @FXML private MenuButton chooseSortButton;
    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    private ObservableList<NumData> list = FXCollections.observableArrayList();

    private int numOfNum = 0;
    private int sortMode = -1; // 0 - bubble sort; 1 - merge sort

    private int bubbleStepIndex;

    Random random = new Random();

    /**
     * The numIn method takes in the user input of number of numbers wanted to display.
     * The method process the input to check if it's a valid natural number, 
     * then generate a list of integer according to the valid input.
     */
    @FXML
    private void numIn(){
        int numIn;
        if (sortMode==-1){ // if the user inputs the number before chosen a sorting algorithm
            numInTextField.setText("");
            numInTextField.setPromptText("Choose the sorting algorithm!");
        } else{
            try {
                numIn = Integer.parseInt(numInTextField.getText());
                if (numIn<=0){ // if the input is not a natural number
                    numInTextField.setText("");
                    numInTextField.setPromptText("You have to input a natural number!");
                } else{ // valid input
                    numOfNum = numIn;
                    bubbleStepIndex = 1;
                    list.clear(); // clear any previous list created
                    generateList(); // generate new list
                }
            }
            catch (NumberFormatException e){ // if the input is not an integer
                numInTextField.setText("");
                numInTextField.setPromptText("You have to input an integer!");
            }
        }
    }

    /**
     * The generateList method generates a list of random number according to the inputted number.
     * The method then reset the background of the cells and update the bar chart.
     */
    private void generateList(){
        for (int i = 0; i < numOfNum; i++){
            list.add(new NumData(random.nextInt(100) & Integer.MAX_VALUE)); // this can be customized
        }
        numCol.setSortType(TableColumn.SortType.ASCENDING);
        resetBackground();
        updateChart();
    }

    /**
     * The populateTable method populates the table cell based on the list that is generated.
     */
    private void populateTable(){
        // this is another way of setting cell value factory for table cells
        // numCol.setCellValueFactory(cellData -> cellData.getValue().numValueProperty()); 
        numCol.setCellValueFactory(new PropertyValueFactory<NumData, Number>("numValue"));
        tableView.setItems(list);
    }

    /**
     * The setBubbleMode method sets the sorting algorithm to bubble sort.
     */
    @FXML
    private void setBubbleMode(){
        sortMode = 0;
        chooseSortButton.setText("Bubble Sort");
        stepButton.setVisible(true);
    }

    /**
     * The setMergeMode method sets the sorting algorithm to merge sort.
     */
    @FXML
    private void setMergeMode(){
        sortMode = 1;
        chooseSortButton.setText("Merge Sort");
        stepButton.setVisible(false);
    }

    /**
     * The sortAll method practices the sorting algorithm all at once.
     * This method can call bubbleSortAll or mergeSort depending on the sorting algorithm chosen.
     */
    @FXML
    private void sortAll(){
        if (list!=null){
            if (sortMode==0){
                bubbleSortAll();
            } else if (sortMode==1){
                mergeSort(list);
            }
        }
    }

    /**
     * The singleStep method practices one step of the sorting algorithm.
     * This method can only call bubbleSingleStep.
     */
    @FXML
    private void singleStep(){
        if (list!= null){
            if (sortMode==0){
                bubbleSingleStep();
            }
        }
    }

    /**
     * The singlePass method practices one pass of the sorting algorithm.
     * This method can call bubbleSinglePass or mergeSortSinglePassAnimation depending on the sorting algorithm chosen.
     */
    @FXML
    private void singlePass(){
        if (list != null){
            if (sortMode==0){
                bubbleSinglePass();
            } else if(sortMode==1){
                mergeSortSinglePassAnimation();
            }
        }
    }

    /**
     * The bubbleSortAll method calls bubbleSortAllPass method which contains the bubble sort algorithm, 
     * animated with time delay, background update and chart updates.
     * These three animations are displayed at the same time.
     */
    private void bubbleSortAll(){
        ParallelTransition pt = new ParallelTransition();
        Timeline sortLoopAnimation = new Timeline(); 
        Timeline backgroundAnimation = new Timeline();
        Timeline chartAnimation = new Timeline();
        int timelineDelayAmount = 750;
        int timelineKeyCount = 0;

        // sorting algorithm
        for (int i = 0; i<numOfNum-1; i++){
            timelineKeyCount++;
            KeyFrame kf = new KeyFrame(Duration.millis(timelineKeyCount * timelineDelayAmount), ae -> {
                bubbleSortAllPass();
            });

            sortLoopAnimation.getKeyFrames().add(kf);
        }

        timelineKeyCount = 0;

        // background update
        for (int i = 0; i<numOfNum; i++){
            for (int j = 1; j<numOfNum; j++){
                int temp = j;
                timelineKeyCount++;
                KeyFrame kf = new KeyFrame(Duration.millis(timelineKeyCount * timelineDelayAmount), ae -> {
                    updateBackground(temp-1, temp);
                });

                backgroundAnimation.getKeyFrames().add(kf);
            }
        }

        timelineKeyCount = 0;

        // chart update
        for (int i = 0; i<numOfNum; i++){
            for (int j = 1; j<numOfNum; j++){
                timelineKeyCount++;
                KeyFrame kf = new KeyFrame(Duration.millis(timelineKeyCount * timelineDelayAmount), ae -> {
                    updateChart();
                });

                backgroundAnimation.getKeyFrames().add(kf);
            }
        }

        pt.getChildren().addAll(sortLoopAnimation, backgroundAnimation, chartAnimation);
        pt.play();
    }

    /**
     * The bubbleSortAllPass method contains the actual bubble sort algorithm with time delay.
     */
    private void bubbleSortAllPass(){
        Timeline sortLoopAnimation = new Timeline(); 
        int timelineDelayAmount = 750;
        int timelineKeyCount = 0;

        for (int j = 1; j<numOfNum; j++){
            int temp = j;
            timelineKeyCount++;
            KeyFrame kf = new KeyFrame(Duration.millis(timelineKeyCount * timelineDelayAmount), ae -> {
                if (list.get(temp-1).getNum() > list.get(temp).getNum()){
                    Collections.swap(list, temp-1, temp);
                }
            });

            sortLoopAnimation.getKeyFrames().add(kf);
        }

        sortLoopAnimation.play();
    }

    /**
     * The bubbleSinglePass method calls bubbleSortOnce for the broken-down bubble sort algorithm and combined it with time delay.
     */
    private void bubbleSinglePass(){
        Timeline sortLoopAnimation = new Timeline(); 
        int timelineDelayAmount = 750;
        int timelineKeyCount = 0;

        for (int j = 1; j<numOfNum; j++){
            int temp = j;
            timelineKeyCount++;
            KeyFrame kf = new KeyFrame(Duration.millis(timelineKeyCount * timelineDelayAmount), ae -> {
                bubbleSortOnce(temp);
            });

            sortLoopAnimation.getKeyFrames().add(kf);
        }

        sortLoopAnimation.play();
    }

    /**
     * The bubbleSingleStep method calls bubbleSortOnce and loop through it with a variable to achieve the single step feature.
     */
    private void bubbleSingleStep(){
        bubbleSortOnce(bubbleStepIndex);
        if (bubbleStepIndex < numOfNum-1){
            bubbleStepIndex++;
        } else{
            bubbleStepIndex = 1;
        }
    }

    /**
     * The bubbleSortOnce method contains the core algorithm of bubble sort and update the background and chart.
     * 
     * @param index is an integer value of the index of element in the list that needs to be compared with its previous element.
     */
    private void bubbleSortOnce(int index){
        if (list.get(index-1).getNum() > list.get(index).getNum()){
            Collections.swap(list, index-1, index);
            updateChart();
        }
        updateBackground(index-1, index);
    }

    /**
     * The mergeSortSinglePassAnimation method calls mergeSort for the merge sort algorithm and combined it with time delay.
     */
    private void mergeSortSinglePassAnimation(){
        Timeline sortLoopAnimation = new Timeline(); 
        int timelineDelayAmount = 750;
        int timelineKeyCount = 0;

        timelineKeyCount++;
        KeyFrame kf = new KeyFrame(Duration.millis(timelineKeyCount * timelineDelayAmount), ae -> {
            mergeSort(list);
        });

        sortLoopAnimation.getKeyFrames().add(kf);

        sortLoopAnimation.play();
    }

    /**
     * The mergeSort method splits every list into two smaller lists and call merge method to compare them, and call itself recursively to keep spliting.
     * When the list is split so its length is smaller than 2, the method would return.
     * 
     * @param listIn is an ObservableList<NumData> list that needs to be splitted into two smaller lists.
     */
    private void mergeSort(ObservableList<NumData> listIn){

        int length = listIn.size();

        if (length < 2){
            return;
        } 
        int mid = length/2;
        ObservableList<NumData> leftList = FXCollections.observableArrayList();
        ObservableList<NumData> rightList = FXCollections.observableArrayList();

        for (int i = 0; i < mid; i++) {
            leftList.add(listIn.get(i));
        }
        for (int i = mid; i < length; i++) {
            rightList.add(listIn.get(i));
        }

        mergeSort(leftList);
        mergeSort(rightList);

        merge(listIn, leftList, rightList);
    }

    /**
     * The merge method compares each item in the two smaller lists and put them back in order into the bigger list.
     * 
     * @param listIn is an ObservableList<NumData> list that is the bigger list.
     * @param lefListIn is an ObservableList<NumData> list that is the first smaller list.
     * @param rightListIn is an ObservableList<NumData> list that is the second smaller list.
     */
    private void merge(ObservableList<NumData> listIn, ObservableList<NumData> lefListIn, ObservableList<NumData> rightListIn){
        
        int i = 0, j = 0, k = 0, leftLength = lefListIn.size(), rightLength = rightListIn.size();

        while (i<leftLength && j<rightLength){ // if neither of the smaller lists are fully looped through
            if (lefListIn.get(i).getNum() <= rightListIn.get(j).getNum()) { // if the item in the left list is smaller or equal to the item in the right list
                updateBackground(k, i);
                updateChart();
                listIn.set(k++, lefListIn.get(i++));
            }
            else { // if the item in the left list is bigger than the item in the right list
                updateBackground(k, j+leftLength);
                updateChart();
                listIn.set(k++, rightListIn.get(j++));
            }
        }
        while (i<leftLength){ // if there is left over item in the left list
            updateBackground(k, i);
            updateChart();
            listIn.set(k++, lefListIn.get(i++));
        }
        while (j<rightLength){ // if there is left over item in the right list
            updateBackground(k, j+leftLength);
            updateChart();
            listIn.set(k++, rightListIn.get(j++));
        }
    }

    /**
     * The updateBackground method updates the table cell background.
     * 
     * @param index1 is an integer value which is the first index of the list element that the cell background needs to be updated.
     * @param index2 is an integer value which is the second index of the list element that the cell background needs to be updated.
     */
    private void updateBackground(int index1, int index2){

        numCol.setCellFactory((tableColumn) -> {
            TableCell<NumData, Number> tableCell = new TableCell<>() {
                @Override
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty){
                        this.setText("");
                    }else{
                        if((this.getIndex()==index1) || (this.getIndex()==index2)){
                        this.setStyle("-fx-background-color: green;");
                        }
                        this.setText(item.toString());
                    }
                }
            };
            return tableCell;
        });
    }

    /**
     * The resetBackground method resets all of the table cell background to transparent.
     */
    private void resetBackground(){

        numCol.setCellFactory((tableColumn) -> {
            TableCell<NumData, Number> tableCell = new TableCell<>() {
                @Override
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty){
                        this.setText("");
                    }else{
                        this.setStyle("-fx-background-color: transparent;");
                        this.setText(item.toString());
                    }
                }
            };
            return tableCell;
        });
    }

    /**
     * The updateChart method updates the chart to represent the current list shown in the table (in the same order).
     */
    private void updateChart(){
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        for (int i = 0; i<numOfNum; i++){
            series.getData().add(new XYChart.Data<String, Number>(" ".repeat(i), list.get(i).getNum()));
        }

        barChart.getData().add(series);
    }

    /**
     * The autoSort method sorts the list in the table all in once without any animation.
     */
    @FXML
    private void autoSort(){
        tableView.getSortOrder().add(numCol);
        updateChart();
    }

    /**
     * The randomize method randomizes the order of the list, resets the background, and updates the chart.
     */
    @FXML
    private void randomize(){
        Collections.shuffle(list);
        resetBackground();
        updateChart();
        bubbleStepIndex = 1;
    }

    /**
     * The showPrompt method enables the text field to still show the prompt text even when it is focused.
     * 
     * @param textFieldIn is the text field that needs to show the prompt text.
     */
    protected static void showPrompt(TextField textFieldIn){
        textFieldIn.styleProperty().bind(
            Bindings
                    .when(textFieldIn.focusedProperty())
                    .then("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);")
                    .otherwise("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);"));
    }

    /**
     * The switchToPrimary method switches the scene to primary.
     * 
     * @exception IOException if the stream is corrupted or errors occurred during reading the data (connecting to the fxml files).
     */
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    /**
     * The endProgram method ends the program.
     */
    @FXML
    private void endProgram(){
        Platform.exit();
    }

    /**
     * The initialize method is called when the scene is first built.
     * It sets the showing prompt text while focused feature for certain text field, sets the step button to be visible, and populates the table.
     */
    @FXML
    private void initialize(){
        showPrompt(numInTextField);
        stepButton.setVisible(true);
        populateTable();
    }
}