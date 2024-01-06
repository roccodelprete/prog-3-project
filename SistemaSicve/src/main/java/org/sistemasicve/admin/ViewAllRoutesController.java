package org.sistemasicve.admin;

import command.pattern.Route;
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
import org.jetbrains.annotations.Nullable;
import strategy.pattern.RouteTable;
import strategy.pattern.TableType;

import java.io.IOException;

import static utils.Alert.showAlert;
import static utils.Alert.showConfirmationAlert;
import static utils.CursorStyle.setCursorStyleOnHover;
import static utils.FormatNumber.formatNumber;
import static utils.RouteTableOperations.deleteRouteFromDb;

public class ViewAllRoutesController {
    /**
     * The delete button
     */
    @FXML
    private Button deleteRouteButton;

    /**
     * The route speed limit column
     */
    @FXML
    private TableColumn<Route, Double> routeSpeedLimitColumn;

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
    private TableColumn<Route, Double> routeLengthColumn;

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
        routeLengthColumn.setCellFactory(col -> new TableCell<Route, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatNumber(item) + " km");
                }
            }
        });

        routeSpeedLimitColumn.setCellValueFactory(new PropertyValueFactory<>("speedLimit"));
        routeSpeedLimitColumn.setCellFactory(col -> new TableCell<Route, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatNumber(item) + " km/h");
                }
            }
        });

        setCursorStyleOnHover(addRouteButton, Cursor.HAND);
        setCursorStyleOnHover(editRouteButton, Cursor.HAND);
        setCursorStyleOnHover(deleteRouteButton, Cursor.HAND);
        setCursorStyleOnHover(getRouteStatisticsButton, Cursor.HAND);

        routeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setSelectedRoute(newSelection);
                addRouteButton.setVisible(true);
                editRouteButton.setVisible(true);
                deleteRouteButton.setVisible(true);
                getRouteStatisticsButton.setVisible(true);
            } else {
                setSelectedRoute(null);
            }
        });
    }

    /**
     * Function to handle the add route button
     * @param event The event to handle
     */
    @FXML
    public void handleAddRoute(@NotNull ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/add-route-view.fxml"));
        Parent addRouteView = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        AddRouteController addRouteController = loader.getController();

        stage.setTitle("SICVE System - Add Route");
        stage.setScene(new Scene(addRouteView));
        stage.setResizable(false);

        Route addedRoute = addRouteController.getAddedRoute();
        if (addedRoute != null) {
            setRouteTableItem(addedRoute);
        }
    }

    /**
     * Function to handle the edit route button
     * @param event The event to handle
     */
    @FXML
    public void handleEditRoute(@NotNull ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/edit-route-view.fxml"));
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
            try {
                deleteRouteFromDb(selectedRoute);
                showAlert(Alert.AlertType.CONFIRMATION, "Success", "Route deleted successfully");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Cannot delete route: " + e.getMessage());
            } finally {
                routeTable.getItems().remove(selectedRoute);
                routeTable.getSelectionModel().clearSelection();
                editRouteButton.setVisible(false);
                deleteRouteButton.setVisible(false);

            }
        }
    }

    /**
     * Function to handle route statistics button
     * @param event The event to handle
     */
    @FXML
    void handleGetRouteStatistics(@NotNull ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/sistemasicve/route-statistics-view.fxml"));
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
     * Function to set a route table item
     * @param route The route to set
     */
    public void setRouteTableItem(Route route) {
        routeTable.getItems().add(route);
    }

    /**
     * Function to set selected route
     * @param route The route to set
     */
    public void setSelectedRoute(@Nullable Route route) {
        this.selectedRoute = route;
    }
}
