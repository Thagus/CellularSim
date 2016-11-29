package view;

import controllers.TechnologyToggleController;
import dataObjects.TechnologyStandard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Thagus on 28/11/16.
 */
public class View {
    private Label receiverCellBlockedCalls, senderCellBlockedCalls, locationBlockedCalls, successfullyStartedCalls, successfullyEndedCalls;
    private Label technologyName, technologyDataRate, technologyCellRadius, technologyUsersPerCell;
    private Label clock;
    private BorderPane borderPane;
    private ArrayList<TechnologyStandard> technologies;

    public Scene createScene(){
        VBox layout = new VBox();
        layout.setSpacing(5);

        loadStandardTechnologies();

        layout.getChildren().add(createMenus());

        borderPane = new BorderPane();
        borderPane.setLeft(createLeftMenu());

        changeTechnology(0);

        layout.getChildren().add(borderPane);

        return new Scene(layout, 1280, 720);
    }

    private MenuBar createMenus(){
        MenuBar menuBar = new MenuBar();

        //File menu
        Menu technologyMenu = new Menu("Technology");

        ToggleGroup technologiesGroup = new ToggleGroup();
        int i = 0;

        for(TechnologyStandard technologyStandard : technologies){
            //Add every technology to the toggle group
            RadioMenuItem technology = new RadioMenuItem(technologyStandard.getTechnologyName());
            technology.setUserData(i);
            technology.setToggleGroup(technologiesGroup);

            if(i == 0) {
                technology.setSelected(true);
            }

            technologyMenu.getItems().add(technology);

            i++;
        }

        //Add controller to the toggle group
        technologiesGroup.selectedToggleProperty().addListener(new TechnologyToggleController(this));

        menuBar.getMenus().add(technologyMenu);
        return  menuBar;
    }

    private VBox createLeftMenu(){
        VBox leftMenu = new VBox();
        leftMenu.setAlignment(Pos.TOP_LEFT);
        leftMenu.setSpacing(10);
        leftMenu.setPadding(new Insets(10, 7, 5, 7));

        BorderStrokeStyle boderStyle = new BorderStrokeStyle(StrokeType.INSIDE, StrokeLineJoin.MITER, StrokeLineCap.BUTT, 10, 0, null);
        BorderStroke borderStroke = new BorderStroke(Color.LIGHTGRAY, boderStyle, CornerRadii.EMPTY, new BorderWidths(2), new Insets(2));
        leftMenu.setBorder(new Border(borderStroke));

        leftMenu.setPrefSize(256, 500);

        GridPane technologySpecsPane = new GridPane();

        technologyName = new Label();
        technologySpecsPane.add(new Label("Technology name: "), 0, 0);
        technologySpecsPane.add(technologyName, 1, 0);
        technologyDataRate = new Label();
        technologySpecsPane.add(new Label("Data rate: "), 0, 1);
        technologySpecsPane.add(technologyDataRate, 1, 1);
        technologyCellRadius = new Label();
        technologySpecsPane.add(new Label("Cell radius: "), 0, 2);
        technologySpecsPane.add(technologyCellRadius, 1, 2);
        technologyUsersPerCell = new Label();
        technologySpecsPane.add(new Label("Users per cell: "), 0, 3);
        technologySpecsPane.add(technologyUsersPerCell, 1, 3);

        leftMenu.getChildren().addAll(technologySpecsPane, new Separator());

        GridPane statusGridPane = new GridPane();

        successfullyStartedCalls = new Label("0");
        statusGridPane.add(new Label("Started: "), 0, 0);
        statusGridPane.add(successfullyStartedCalls, 1, 0);
        successfullyEndedCalls = new Label("0");
        statusGridPane.add(new Label("Ended: "), 0, 1);
        statusGridPane.add(successfullyEndedCalls, 1, 1);
        locationBlockedCalls = new Label("0");
        statusGridPane.add(new Label("Location blocked: "), 0, 2);
        statusGridPane.add(locationBlockedCalls, 1, 2);
        senderCellBlockedCalls = new Label("0");
        statusGridPane.add(new Label("Sender blocked: "), 0, 3);
        statusGridPane.add(senderCellBlockedCalls, 1, 3);
        receiverCellBlockedCalls = new Label("0");
        statusGridPane.add(new Label("Receiver blocked: "), 0, 4);
        statusGridPane.add(receiverCellBlockedCalls, 1, 4);

        leftMenu.getChildren().addAll(statusGridPane, new Separator());

        Font headerFont = Font.font("Arial", FontWeight.BOLD, 14);
        clock = new Label();
        clock.setFont(headerFont);

        leftMenu.getChildren().add(clock);

        return leftMenu;
    }

    public void setLabelValues(int successfullyStartedCalls, int successfullyEndedCalls, int locationBlockedCalls, int senderCellBlockedCalls, int receiverCellBlockedCalls){
        this.successfullyStartedCalls.setText(successfullyStartedCalls + "");
        this.successfullyEndedCalls.setText(successfullyEndedCalls + "");
        this.locationBlockedCalls.setText(locationBlockedCalls + "");
        this.senderCellBlockedCalls.setText(senderCellBlockedCalls + "");
        this.receiverCellBlockedCalls.setText(receiverCellBlockedCalls + "");
    }

    public void updateClock(Time clock){
        this.clock.setText(clock.toString());
    }

    private void loadStandardTechnologies(){
        technologies = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(getClass().getResourceAsStream("/technologies.xml"));

            doc.getDocumentElement().normalize();

            NodeList technologiesList = doc.getElementsByTagName("technology");

            for (int prof = 0; prof < technologiesList.getLength(); prof++) {
                org.w3c.dom.Node nNode = technologiesList.item(prof);

                if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    //Create profile object
                    TechnologyStandard technologyStandard = new TechnologyStandard(
                            eElement.getAttribute("name"),
                            eElement.getElementsByTagName("multipleAccess").item(0).getTextContent(),
                            Float.parseFloat(eElement.getElementsByTagName("dataRate").item(0).getTextContent()),
                            Float.parseFloat(eElement.getElementsByTagName("cellRadius").item(0).getTextContent()),
                            Integer.parseInt(eElement.getElementsByTagName("usersPerCell").item(0).getTextContent())
                    );

                    technologies.add(technologyStandard);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeTechnology(int index){
        TechnologyStandard technology = technologies.get(index);
        borderPane.setCenter(new SimulationView(this, technology));

        technologyName.setText(technology.getTechnologyName());
        technologyCellRadius.setText(technology.getCellRadius() + " km");
        technologyUsersPerCell.setText(technology.getUsersPerCell() + " users");
        technologyDataRate.setText(technology.getDataRate() + " kbps");
    }
}
