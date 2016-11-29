package view;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

/**
 * Created by Thagus on 28/11/16.
 */
public class View {
    private Label receiverCellBlockedCalls, senderCellBlockedCalls, locationBlockedCalls, successfullyStartedCalls, successfullyEndedCalls;
    private Label technologyName, technologyDataRate, technologyCellRadius, technologyUsersPerCell;
    private ArrayList<TechnologyStandard> technologies;

    public Scene createScene(){
        VBox layout = new VBox();
        layout.setSpacing(5);

        loadStandardTechnologies();

        layout.getChildren().add(createMenus());

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(new SimulationView(this, technologies.get(0)));
        borderPane.setLeft(createLeftMenu());

        layout.getChildren().add(borderPane);


        return new Scene(layout, 1280, 720);
    }

    private MenuBar createMenus(){
        MenuBar menuBar = new MenuBar();

        //File menu
        Menu technologyMenu = new Menu("Technology");

        ToggleGroup technologiesGroup = new ToggleGroup();
        boolean firstOne = true;

        for(TechnologyStandard technologyStandard : technologies){
            //Add every technology to the toggle group
            RadioMenuItem technology = new RadioMenuItem(technologyStandard.getTechnologyName());
            technology.setUserData(technologyStandard.getTechnologyName());
            technology.setToggleGroup(technologiesGroup);

            if(firstOne) {
                technology.setSelected(true);
                firstOne = false;
            }

            technologyMenu.getItems().add(technology);
        }

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

        technologyName = new Label();
        technologyDataRate = new Label();
        technologyCellRadius = new Label();
        technologyUsersPerCell = new Label();

        leftMenu.getChildren().addAll(technologyName, technologyDataRate, technologyCellRadius, technologyUsersPerCell);

        receiverCellBlockedCalls = new Label("0");
        senderCellBlockedCalls = new Label("0");
        locationBlockedCalls = new Label("0");
        successfullyStartedCalls = new Label("0");
        successfullyEndedCalls = new Label("0");

        leftMenu.getChildren().addAll(successfullyStartedCalls, successfullyEndedCalls, locationBlockedCalls, senderCellBlockedCalls, receiverCellBlockedCalls);

        return leftMenu;
    }

    public void setLabelValues(int successfullyStartedCalls, int successfullyEndedCalls, int locationBlockedCalls, int senderCellBlockedCalls, int receiverCellBlockedCalls){
        this.successfullyStartedCalls.setText(successfullyStartedCalls + "");
        this.successfullyEndedCalls.setText(successfullyEndedCalls + "");
        this.locationBlockedCalls.setText(locationBlockedCalls + "");
        this.senderCellBlockedCalls.setText(senderCellBlockedCalls + "");
        this.receiverCellBlockedCalls.setText(receiverCellBlockedCalls + "");
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
}
