package TriangleMesh;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Takes the map created by the DSAlgorithm and displays it
 *
 * @author Anthony
 */
public class TriangleMesh extends Application {

    @Override
    public void start(Stage primaryStage) {
        GridPane leftpane = new GridPane();
        leftpane.setAlignment(Pos.CENTER);
        // Set up some padding around the edges of the grid,
        // top, right, bottom, left - number of pixels
        leftpane.setPadding(new Insets(15, 15, 15, 15));
        // Set up the horizontal and vertical spacing between 
        // the rows and columns of the grid
        leftpane.setHgap(10);
        leftpane.setVgap(10);
        // Create labels and text fields and place them into the grid layout
        //textfields
        TextField a = new TextField();
        a.setText("-150"); //set placeholder text
        a.setPrefWidth(70); //set prefered width
        TextField b = new TextField();
        b.setText("150"); //set placeholder text
        b.setPrefWidth(70); //set prefered width
        TextField h = new TextField();
        h.setText(".7"); //set placeholder text
        h.setPrefWidth(70); //set prefered width
        TextField i = new TextField();
        i.setText("6"); //set placeholder text
        i.setPrefWidth(70); //set prefered width
        TextField pixel = new TextField();
        pixel.setText("7"); //set placeholder text
        pixel.setPrefWidth(70); //set prefered width

        RadioButton b1 = new RadioButton();
        RadioButton b2 = new RadioButton();
        
        //button for hard edges
        RadioButton tb = new RadioButton();

        ToggleGroup radioGroup = new ToggleGroup();

        b1.setToggleGroup(radioGroup);
        b2.setToggleGroup(radioGroup);

        Button create = new Button("Create");
        create.setPrefWidth(70); //set prefered width

        // Parameters to the add routine are: component, column, row
        leftpane.add(new Label("A: Min Range"), 0, 0);
        leftpane.add(a, 1, 0);
        leftpane.add(new Label("B: Max Range"), 0, 1);
        leftpane.add(b, 1, 1);
        leftpane.add(new Label("H: Decay (0 - 1)"), 0, 2);
        leftpane.add(h, 1, 2);
        leftpane.add(new Label("EXP: (1 - 7)"), 0, 3);
        leftpane.add(i, 1, 3);
        leftpane.add(new Label("Lines:"), 0, 4);
        leftpane.add(b1, 1, 4);
        leftpane.add(new Label("Terrain:"), 0, 5);
        leftpane.add(b2, 1, 5);
        leftpane.add(new Label("Hard Edges:"), 0, 6);
        leftpane.add(tb, 1, 6);

        leftpane.add(create, 1, 7);

        create.setOnAction(e -> {
            //if no button is selected, pick lines
            if (!b1.isSelected() && !b2.isSelected()) {
                b1.setSelected(true);
            }

            if (Double.parseDouble(h.getText()) < 0) //if below the proper range...
            {
                h.setText("0");
            }
            if (Double.parseDouble(h.getText()) > 1) //if below the proper range...
            {
                h.setText("1");
            }
            if (Integer.parseInt(i.getText()) < 1) //if below the proper range...
            {
                i.setText("1");
            }
            if (Integer.parseInt(i.getText()) > 7) //if above the proper range...
            {
                i.setText("7");
            }
            
            createMap(a, b, h, i, b1, tb); //display the map
        });

        Scene scene = new Scene(leftpane);

        primaryStage.setTitle("Settings");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void createMap(TextField a, TextField b, TextField h, TextField i, RadioButton b1, RadioButton tb) {
        Stage primaryStage = new Stage();

        Pane pane = new Pane(); //pane to add all of the pixels to
        //pane.setPrefWidth(MAPSIZE * PIXELSIZE); //set the size of each wrapped region
        //pane.setMinWidth(MAPSIZE * PIXELSIZE); //set the size of each wrapped region
        
        DSAlgorithm dsa = new DSAlgorithm(Integer.parseInt(a.getText()), Integer.parseInt(b.getText()), Double.parseDouble(h.getText()), Integer.parseInt(i.getText()), 10, 126, 126, 126, 126); //create the map to display
        //DSAlgorithm(int a, int b, double h, int i, int pixelsize, double seed1, double seed2, double seed3, double seed4)

        dsa.fillMap(0, (dsa.getMAPSIZE() - 1), 0, (dsa.getMAPSIZE() - 1), dsa.getA(), dsa.getB()); //fill the map with heights
        double[][] dsaMap = dsa.getMap(); //save the map

        //variables for drawing the mesh
        double startX = 750; //how far in the center of the mesh is
        double startY = 0; //how far up the mesh starts
        double xInc = 0; //x increment value
        double yInc = 0; //y increment value

        double waterLvl = 150;

        //add switch statement here to specify start x, y, and increments based on the mapSize
        switch (dsa.getEXP()) {
            case 1:
                startY = 200;
                xInc = 300;
                yInc = 100;
                break;
            case 2:
                startY = 150;
                xInc = 180;
                yInc = 60;
                break;
            case 3:
                startY = 130;
                xInc = 90;
                yInc = 30;
                break;
            case 4:
                startY = 160;
                xInc = 45;
                yInc = 13;
                break;
            case 5:
                startY = 150;
                xInc = 21;
                yInc = 7;
                break;
            case 6:
                startY = 170;
                xInc = 11;
                yInc = 3;
                break;
            case 7:
                startY = 150;
                xInc = 5;
                yInc = 2;
                break;
            case 8:
                startY = 150;
                xInc = 3;
                yInc = 1;
                break;
        }

        double[][] xVert = new double[dsa.getMAPSIZE()][dsa.getMAPSIZE()];
        double[][] yVert = new double[dsa.getMAPSIZE()][dsa.getMAPSIZE()];

        //get the positions
        for (int x = 0; x <= dsa.getMAPSIZE() - 1; x++) {
            for (int y = 0; y <= dsa.getMAPSIZE() - 1; y++) {
                //set Y pos
                yVert[y][x] = startY + ((y + x) * yInc);

                //set x pos
                if (y - x > 0) { //left of center
                    xVert[y][x] = startX + ((y - x) * xInc);
                } else if (y - x < 0) { //right of center
                    xVert[y][x] = startX - ((x - y) * xInc);
                } else {
                    xVert[y][x] = startX; // along the center
                }
            }
        }

        //add the height discrepancies
        for (int x = 0; x <= dsa.getMAPSIZE() - 1; x++) {
            for (int y = 0; y <= dsa.getMAPSIZE() - 1; y++) {
                if (dsaMap[y][x] < 126) {
                    yVert[y][x] += dsaMap[y][x] - 126;
                } else {
                    yVert[y][x] -= 126 - dsaMap[y][x];
                }
            }
        }

        double avg = 0; //average for the mesh color
        //add the mesh 
        for (int x = 0; x <= dsa.getMAPSIZE() - 1; x++) {
            for (int y = 0; y <= dsa.getMAPSIZE() - 1; y++) {
                if (x != dsa.getMAPSIZE() - 1 && y != dsa.getMAPSIZE() - 1) {
                    Polygon p1 = new Polygon(xVert[y][x], yVert[y][x], xVert[y][(x + 1)], yVert[y][(x + 1)], xVert[(y + 1)][x], yVert[(y + 1)][x]); //bottom triangle
                    Polygon p2 = new Polygon(xVert[y + 1][x + 1], yVert[y + 1][x + 1], xVert[y][(x + 1)], yVert[y][(x + 1)], xVert[(y + 1)][x], yVert[(y + 1)][x]); //top triangle

                    if (tb.isSelected()) { //if user wants hard edges
                        p1.setStroke(BLACK);
                        p2.setStroke(BLACK);
                    }

                    if (b1.isSelected()) { //if user wants lines
                        p1.setStroke(BLACK);
                        p2.setStroke(BLACK);
                        p1.setFill(WHITE);
                        p2.setFill(WHITE);
                    } else { //if user wants colors 
                        //bottom polygon
                        avg = (dsaMap[y][x] + dsaMap[y][(x + 1)] + dsaMap[(y + 1)][x]) / 3;
                        if (avg <= 40) {
                            p1.setFill(SNOW); //snowcaps
                        } else if (avg > 40 && avg <= 75) {
                            p1.setFill(GRAY); //mountain
                        } else if (avg > 75 && avg <= 90) {
                            p1.setFill(DARKGREEN); //tall forrest
                        } else if (avg > 90 && avg <= 125) {
                            p1.setFill(OLIVEDRAB); //grassland
                        } else if (avg > 125 && avg <= 150) {
                            p1.setFill(KHAKI); //beach
                        } else if (avg > 150 && avg <= 250) {
                            p1.setFill(DEEPSKYBLUE); //shallow ocean
                            Polygon water = new Polygon(xVert[y][x], yVert[y][x] - dsaMap[y][x] + waterLvl, xVert[y + 1][x], yVert[y + 1][x] - dsaMap[y + 1][x] + waterLvl, xVert[y][x + 1], yVert[y][x + 1] - dsaMap[y][x + 1] + waterLvl); //transparent triangle
                            water.setFill(Color.rgb(72, 61, 139, .4)); //add transparency to the flat plane -000, 191, 255
                            pane.getChildren().add(water);
                        } else if (avg > 250) {
                            p1.setFill(BLUE); //deep ocean
                            Polygon water = new Polygon(xVert[y][x], yVert[y][x] - dsaMap[y][x] + waterLvl, xVert[y + 1][x], yVert[y + 1][x] - dsaMap[y + 1][x] + waterLvl, xVert[y][x + 1], yVert[y][x + 1] - dsaMap[y][x + 1] + waterLvl); //transparent triangle
                            water.setFill(Color.rgb(72, 61, 139, .4)); //add transparency to the flat plane -000, 191, 255
                            pane.getChildren().add(water);
                        }

                        //top polygon
                        avg = (dsaMap[y + 1][x + 1] + dsaMap[y][(x + 1)] + dsaMap[(y + 1)][x]) / 3;
                        if (avg <= 40) {
                            p2.setFill(SNOW); //snowcaps
                        } else if (avg > 40 && avg <= 75) {
                            p2.setFill(GRAY); //mountain
                        } else if (avg > 75 && avg <= 90) {
                            p2.setFill(DARKGREEN); //tall forrest
                        } else if (avg > 90 && avg <= 125) {
                            p2.setFill(OLIVEDRAB); //grassland
                        } else if (avg > 125 && avg <= 150) {
                            p2.setFill(KHAKI); //beach
                        } else if (avg > 150 && avg <= 250) {
                            p2.setFill(DEEPSKYBLUE); //shallow ocean
                            Polygon water = new Polygon(xVert[y + 1][x + 1], yVert[y + 1][x + 1] - dsaMap[y + 1][x + 1] + waterLvl, xVert[y + 1][x], yVert[y + 1][x] - dsaMap[y + 1][x] + waterLvl, xVert[y][x + 1], yVert[y][x + 1] - dsaMap[y][x + 1] + waterLvl); //transparent triangle
                            water.setFill(Color.rgb(72, 61, 139, .4)); //add transparency to the flat plane
                            pane.getChildren().add(water);
                        } else if (avg > 250) {
                            p2.setFill(BLUE); //deep ocean
                            Polygon water = new Polygon(xVert[y + 1][x + 1], yVert[y + 1][x + 1] - dsaMap[y + 1][x + 1] + waterLvl, xVert[y + 1][x], yVert[y + 1][x] - dsaMap[y + 1][x] + waterLvl, xVert[y][x + 1], yVert[y][x + 1] - dsaMap[y][x + 1] + waterLvl); //transparent triangle
                            water.setFill(Color.rgb(72, 61, 139, .4)); //add transparency to the flat plane
                            pane.getChildren().add(water);
                        }
                    }
                    //add the polygons
                    pane.getChildren().add(p1);
                    pane.getChildren().add(p2);
                }
            }
        }

        Scene scene = new Scene(pane, 1500, 800); //create a window large enough to hold the map
        
        scene.setOnMousePressed((MouseEvent event) -> {
            createMap(a, b, h, i, b1, tb); //new map with same settings
            
            scene.getWindow().hide();//close current pane
        } //create new project
        );

        primaryStage.setTitle("Diamond-Square Triangle Mesh Height Map");
        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        primaryStage.show();
    }
}