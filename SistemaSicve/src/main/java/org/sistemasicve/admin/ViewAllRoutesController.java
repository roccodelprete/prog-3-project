package org.sistemasicve.admin;

import command.pattern.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import singleton.pattern.LoggedUser;
import strategy.pattern.RouteTable;
import strategy.pattern.TableType;
import utils.FormatLength;
import utils.FormatSpeed;
import utils.PoliceStation;
import utils.Route;

import java.io.IOException;

import static database.operations.PoliceStationTableOperations.getPoliceStationFromDb;
import static utils.Alert.showConfirmationAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import static utils.FormatLength.formatLength;
import static utils.FormatSpeed.formatSpeed;

public class ViewAllRoutesController {
    /**
     * The logout button
     */
    @FXML
    private Button logoutButton;

    /**
     * The delete button
     */
    @FXML
    private Button deleteRouteButton;

    /**
     * The route speed limit column
     */
    @FXML
    private TableColumn<Route, Integer> routeSpeedLimitColumn;

    /**
     * The edit button
     */
    @FXML
    private Button editRouteButton;

    /**
     * The route table
     */
    @FXML
    private TableView<Route> routeTable;

    /**
     * The route name column
     */
    @FXML
    private TableColumn<Route, String> routeNameColumn;

    /**
     * The route length column
     */
    @FXML
    private TableColumn<Route, Integer> routeLengthColumn;

    /**
     * The add button
     */
    @FXML
    private Button addRouteButton;

    /**
     * The add button
     */
    @FXML
    private Button getRouteStatisticsButton;

    /**
     * The selected route
     */
    private Route selectedRoute;

    /**
     * The police station column
     */
    @FXML
    private TableColumn<Route, String> policeStationColumn;

    @FXML
    private Button addPoliceStationButton;

    /**
     * Function to initialize the view
     */
    @FXML
    void initialize () {
        editRouteButton.setVisible(false);
        deleteRouteButton.setVisible(false);
        getRouteStatisticsButton.setVisible(false);

        TableType<Route> tableType = new TableType<>(new RouteTable());
        routeTable.setItems(tableType.getTableElements());

        routeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        routeLengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        routeLengthColumn.setCellFactory(col -> new TableCell<Route, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatLength(item, FormatLength.LengthUnit.KM));
                }
            }
        });

        routeSpeedLimitColumn.setCellValueFactory(new PropertyValueFactory<>("speedLimit"));
        routeSpeedLimitColumn.setCellFactory(col -> new TableCell<Route, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatSpeed(item, FormatSpeed.SpeedUnit.KMH));
                }
            }
        });
        policeStationColumn.setCellValueFactory(new PropertyValueFactory<>("policeStation"));

        setCursorStyleOnHover(addRouteButton, Cursor.HAND);
        setCursorStyleOnHover(editRouteButton, Cursor.HAND);
        setCursorStyleOnHover(deleteRouteButton, Cursor.HAND);
        setCursorStyleOnHover(getRouteStatisticsButton, Cursor.HAND);
        setCursorStyleOnHover(logoutButton, Cursor.HAND);
        setCursorStyleOnHover(addPoliceStationButton, Cursor.HAND);

        routeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedRoute = newSelection;
                addRouteButton.setVisible(true);
                editRouteButton.setVisible(true);
                deleteRouteButton.setVisible(true);
                getRouteStatisticsButton.setVisible(true);
            } else {
                selectedRoute = null;
            }
        });
    }

    /**
     * Function to handle the add route button
     * @param event The event to handle
     */
    @FXML
    public void handleAddRoute(@NotNull ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/route-views/add-route-view.fxml"));
        Parent addRouteView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        AddRouteController addRouteController = loader.getController();

        stage.setTitle("SICVE System - Add Route");
        stage.setScene(new Scene(addRouteView));
        stage.setResizable(false);

        Route addedRoute = addRouteController.getAddedRoute();
        if (addedRoute != null) {
            routeTable.getItems().add(addedRoute);
        }
    }

    /**
     * Function to handle the edit route button
     * @param event The event to handle
     */
    @FXML
    public void handleEditRoute(@NotNull ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/route-views/edit-route-view.fxml"));
        Parent editRouteView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        EditRouteController editRouteController = loader.getController();

        if (selectedRoute != null) {
            editRouteController.setRoute(selectedRoute);
        }

        stage.setTitle("SICVE System - Edit Route");
        stage.setScene(new Scene(editRouteView));
        stage.setResizable(false);

        stage.show();
    }

    /**
     * Function to handle the delete route button
     */
    @FXML
    void handleDeleteRoute() {
        Route selectedRoute = routeTable.getSelectionModel().getSelectedItem();

        boolean confirmation = showConfirmationAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Are you sure you want to delete this route?");

        if (selectedRoute != null && confirmation) {
            TutorSystem tutorSystem = new TutorSystem();

            Command deleteRouteCommand = new DeleteRouteCommand(
                    LoggedUser.getInstance().getAdmin(),
                    selectedRoute
            );

            tutorSystem.addCommand(deleteRouteCommand);
            tutorSystem.executeCommand(deleteRouteCommand);

            routeTable.getItems().remove(selectedRoute);
        }
    }

    /**
     * Function to handle route statistics button
     * @param event The event to handle
     */
    @FXML
    void handleGetRouteStatistics(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/route-views/route-statistics-view.fxml"));
        Parent routeStatisticsView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        GetStatisticsController routeStatisticsController = loader.getController();

        if (selectedRoute != null) {
            routeStatisticsController.setRoute(selectedRoute);
            routeStatisticsController.setLabelTitle("Statistics for route " + selectedRoute.getName());
        }

        stage.setTitle("SICVE System - Route Statistics");
        stage.setScene(new Scene(routeStatisticsView));
        stage.setResizable(false);

        stage.show();
    }

    /**
     * Function to handle the logout button
     * @param event The event to handle
     */
    @FXML
    void handleLogout(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/home-view.fxml"));
        Parent root = loader.load();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Function to handle the add police station button
     * @param event The event to handle
     */
    @FXML
    void handleAddPoliceStation(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/add-police-station-view.fxml"));
        Parent addPoliceStationView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        stage.setScene(new Scene(addPoliceStationView));
        stage.show();
    }
}
