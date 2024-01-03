package app;

import command.pattern.*;
import observer_memento.pattern.*;
import org.jetbrains.annotations.NotNull;
import utils.PoliceStationTableOperations;
import utils.RouteTableOperations;
import utils.VehicleTableOperations;

import java.util.ArrayList;

/**
 * Test class to test the application
 * @author Rocco Del Prete
 */
public class Test {
    /**
     * function to move a vehicle on a route
     * @param vehicle The vehicle to move
     * @param route The route where the vehicle is moving
     * @param tutorSystem The tutor system to detect infractions
     * @param tripLength  The length of the trip
     * @param policeStation The police station to send the infraction if committed
     * @return The remaining length of the trip
     */
    private static @NotNull Double moveVehicleOnRoute(
            @NotNull Vehicle vehicle,
            @NotNull Route route,
            @NotNull TutorSystem tutorSystem,
            Double tripLength,
            @NotNull PoliceStation policeStation
    ) {
        Double detectedSpeed = Math.random() * 130.0 + 60.0;

        vehicle.moveVehicle();
        tripLength -= vehicle.getCurrentMilestone();

        tutorSystem.detectSpeed(detectedSpeed, route, vehicle, policeStation);

        return tripLength;
    }

    public static void main(String[] args) {
        VehicleTableOperations vehicleTableOperations = new VehicleTableOperations();
        PoliceStationTableOperations policeStationTableOperations = new PoliceStationTableOperations();
        RouteTableOperations routeTableOperations = new RouteTableOperations();

        Admin admin = new Admin();
        TutorSystem tutorSystem = new TutorSystem();

        PoliceStation policeStation =
                policeStationTableOperations.getPoliceStationFromDb("Police Station 1") == null
                ? policeStationTableOperations.insertPoliceStationIntoDb(new PoliceStation("Police Station 1"))
                : policeStationTableOperations.getPoliceStationFromDb("Police Station 1");

        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new AddRouteCommand(tutorSystem, new Route("Route 1", 100.0, 1000.0)));
        commands.add(new AddRouteCommand(tutorSystem, new Route("Route 2", 130.0, 1200.0)));

        Command editRouteCommand = new EditRouteCommand(tutorSystem, routeTableOperations.getRouteFromDb("Route 1"),
                null, 70.0, null);
        admin.addCommand(editRouteCommand);

        commands.forEach(admin::addCommand);

        admin.executeCommand(editRouteCommand);

        Route currentRoute = tutorSystem.getRoutes().getFirst();

        TutorStation entranceStation = new TutorStation();
        TutorStation exitStation = new TutorStation();

        Vehicle vehicle = vehicleTableOperations.getVehicleFromDb("AB123CD") == null ?
                vehicleTableOperations.insertVehicleIntoDb(new Vehicle("AB123CD", "Fiat", "Punto")) :
                vehicleTableOperations.getVehicleFromDb("AB123CD");

        double tripLength = 1000.0;

        entranceStation.attach(vehicle);
        entranceStation.notifyObserver(vehicle, "entered the route " + currentRoute.getName() + "\n");

        ArrayList<Command> statisticsCommands = new ArrayList<>();

        while (tripLength > 0.0) {
            tripLength = moveVehicleOnRoute(vehicle, currentRoute, tutorSystem, tripLength, policeStation);

            if (vehicle.getCurrentMilestone() >= currentRoute.getLength()) {
                exitStation.attach(vehicle);

                exitStation.notifyObserver(vehicle, "exited the route " + currentRoute.getName() + "\n");

                currentRoute = tutorSystem.getRoutes().get(tutorSystem.getRoutes().indexOf(currentRoute) + 1);

                entranceStation.notifyObserver(vehicle, "entered the route " + currentRoute.getName() + "\n");
            }

            if (tripLength <= 0.0) {
                System.out.println("Trip ended...\n");
                break;
            }
        }

        if (tutorSystem.getInfractions().containsKey(vehicle)) {
            tutorSystem.getInfractions().get(vehicle).forEach((route, infractions) ->
                tutorSystem.sendMostSevereInfraction(policeStation, route, vehicle));
        }

        tutorSystem.getRoutes().forEach(route ->
                statisticsCommands.add(new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route, vehicle)));
        tutorSystem.getRoutes().forEach(route ->
                statisticsCommands.add(new GetRouteSpeedStatisticsCommand(tutorSystem, route)));

        statisticsCommands.forEach(admin::addCommand);

        statisticsCommands.forEach(admin::executeCommand);
    }
}
