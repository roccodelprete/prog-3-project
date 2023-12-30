package app;

import command.pattern.*;
import observer_memento.pattern.*;

import java.util.ArrayList;

/**
 * Test class
 * @author Rocco Del Prete
 */
public class Test {
    /**
     * function to move a vehicle on a route
     * @param vehicle The vehicle to move
     * @param route The route where the vehicle is moving
     * @param tutorSystem The tutor system to detect infractions
     * @param entranceStation The entrance station
     * @param exitStation The exit station
     * @param policeStation The police station to send infractions
     */
    private static void moveVehicleOnRoute(Vehicle vehicle, Route route, TutorSystem tutorSystem, TutorStation entranceStation, TutorStation exitStation, PoliceStation policeStation) {
        entranceStation.attach(vehicle);
        entranceStation.notifyObserver(vehicle, "entered the route with tutor system\n");

        for (int i = 0; i < 4; i++) {
            vehicle.moveVehicle();
            tutorSystem.detectSpeed(Math.random() * 200.0 + 60.0, route, vehicle);
        }

        tutorSystem.sendAllInfractions(vehicle, policeStation, route);
        tutorSystem.sendMostSevereInfraction(policeStation, route, vehicle);

        if (vehicle.getCurrentMilestone() >= route.getLength()) {
            exitStation.attach(vehicle);
            entranceStation.detach(vehicle);
            exitStation.notifyObserver(vehicle, "exited the route with tutor system\n");
        }
    }

    public static void main(String[] args) {
        Admin admin = new Admin();
        TutorSystem tutorSystem = new TutorSystem();
        PoliceStation policeStation = new PoliceStation("Police Station 1");

        Route route1 = new Route("Route 1", 90.0, 1200.0);
        Route route2 = new Route("Route 2", 80.0, 1500.0);

        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new AddRouteCommand(tutorSystem, route1));
        commands.add(new AddRouteCommand(tutorSystem, route2));

        commands.forEach(admin::addCommand);
        admin.executeCommands();

        TutorStation entranceStation = new TutorStation();
        TutorStation exitStation = new TutorStation();

        Vehicle vehicle1 = new Vehicle("ABC123", "Fiat", "Punto");
        Vehicle vehicle2 = new Vehicle("DEF456", "Fiat", "Panda");

        ArrayList<Command> statisticsCommands = new ArrayList<>();
        statisticsCommands.add(new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route1, vehicle1));
        statisticsCommands.add(new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route1, vehicle2));
        statisticsCommands.add(new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route2, vehicle2));
        statisticsCommands.add(new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route2, vehicle1));
        statisticsCommands.add(new GetRouteSpeedStatisticsCommand(tutorSystem, route1));
        statisticsCommands.add(new GetRouteSpeedStatisticsCommand(tutorSystem, route2));

        statisticsCommands.forEach(admin::addCommand);

        moveVehicleOnRoute(vehicle1, route1, tutorSystem, entranceStation, exitStation, policeStation);
        moveVehicleOnRoute(vehicle2, route1, tutorSystem, entranceStation, exitStation, policeStation);

        statisticsCommands.forEach(admin::executeCommand);
    }
}
