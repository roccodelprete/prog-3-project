package app;

import command.pattern.*;
import observer_memento.pattern.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static utils.PoliceStationTableOperations.getPoliceStationFromDb;
import static utils.PoliceStationTableOperations.insertPoliceStationIntoDb;
import static utils.RouteTableOperations.getRouteFromDb;
import static utils.UserTableOperations.getUserFromDb;
import static utils.UserTableOperations.insertUserIntoDb;
import static utils.VehicleTableOperations.getVehicleFromDb;
import static utils.VehicleTableOperations.insertVehicleIntoDb;

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
        User user = getUserFromDb("rocco@gmail.com") == null
                ? insertUserIntoDb(
                        new User("Rocco", "Del Prete", "rocco@gmail.com", "rocco", false
                    ))
                : getUserFromDb("rocco@gmail.com");

        Admin admin = new Admin(
                getUserFromDb("admin@admin.com") == null
                    ? insertUserIntoDb(
                        new User("Admin", "Admin", "admin@admin.com", "admin", false))
                    : getUserFromDb("admin@admin.com"));

        TutorSystem tutorSystem = new TutorSystem();

        PoliceStation policeStation =
                getPoliceStationFromDb("Police Station 1") == null
                ? insertPoliceStationIntoDb(new PoliceStation("Police Station 1"))
                : getPoliceStationFromDb("Police Station 1");

        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new AddRouteCommand(tutorSystem, new Route("Route 1", 100.0, 1000.0)));
        commands.add(new AddRouteCommand(tutorSystem, new Route("Route 2", 130.0, 1200.0)));

        Command editRouteCommand = new EditRouteCommand(tutorSystem, getRouteFromDb("Route 1"),
                null, 70.0, null);
        admin.addCommand(editRouteCommand);

        commands.forEach(admin::addCommand);

        admin.executeCommand(editRouteCommand);

        Route currentRoute = tutorSystem.getRoutes().getFirst();

        TutorStation entranceStation = new TutorStation();
        TutorStation exitStation = new TutorStation();

        Vehicle vehicle = getVehicleFromDb("AB123CD") == null
                ? insertVehicleIntoDb(new Vehicle("AB123CD", "Fiat", "Punto", user.getEmail()))
                : getVehicleFromDb("AB123CD");

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
                statisticsCommands.add(new GetRouteVehicleStatisticsCommand(tutorSystem, route, vehicle)));
        tutorSystem.getRoutes().forEach(route ->
                statisticsCommands.add(new GetRouteStatisticsCommand(tutorSystem, route)));

        statisticsCommands.forEach(admin::addCommand);

        statisticsCommands.forEach(admin::executeCommand);
    }
}
