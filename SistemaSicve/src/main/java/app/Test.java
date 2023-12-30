package app;

import command.pattern.*;
import observer_memento.pattern.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Test class to test the application
 * @author Rocco Del Prete
 */
public class Test {
    /**
     * function to move a vehicle on a route
     * @param vehicle                   The vehicle to move
     * @param route                     The route where the vehicle is moving
     * @param tutorSystem               The tutor system to detect infractions
     * @param tripLength                The length of the trip
     */
    private static Double moveVehicleOnRoute(@NotNull Vehicle vehicle, @NotNull Route route, TutorSystem tutorSystem, Double tripLength) {
        Double detectedSpeed = Math.random() * 130.0 + 60.0;

        vehicle.moveVehicle();
        tripLength -= vehicle.getCurrentMilestone();

        tutorSystem.detectSpeed(detectedSpeed, route, vehicle);

        return tripLength;
    }

    public static void main(String[] args) {
        Admin admin = new Admin();
        TutorSystem tutorSystem = new TutorSystem();
        PoliceStation policeStation = new PoliceStation("Police Station 1");

        Route route1 = new Route("Route 1", 90.0, 120.0);
        Route route2 = new Route("Route 2", 80.0, 1500.0);

        ArrayList<Command> commands = new ArrayList<>();

        commands.add(new AddRouteCommand(tutorSystem, route1));
        commands.add(new AddRouteCommand(tutorSystem, route2));

        commands.forEach(admin::addCommand);
        commands.forEach(admin::executeCommand);

        Route currentRoute = tutorSystem.getRoutes().getFirst();

        TutorStation entranceStation = new TutorStation();
        TutorStation exitStation = new TutorStation();

        Vehicle vehicle1 = new Vehicle("ABC123", "Fiat", "Punto");
        Vehicle vehicle2 = new Vehicle("DEF456", "Fiat", "Panda");

        double tripLength = 1000.0;

        entranceStation.attach(vehicle1);
        entranceStation.notifyObserver(vehicle1, "entered the route " + currentRoute.getName() + "\n");
        entranceStation.attach(vehicle2);

        ArrayList<Command> statisticsCommands = new ArrayList<>();
        statisticsCommands.add(new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route1, vehicle1));
        statisticsCommands.add(new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route1, vehicle2));
        statisticsCommands.add(new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route2, vehicle2));
        statisticsCommands.add(new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route2, vehicle1));
        statisticsCommands.add(new GetRouteSpeedStatisticsCommand(tutorSystem, route1));
        statisticsCommands.add(new GetRouteSpeedStatisticsCommand(tutorSystem, route2));

        statisticsCommands.forEach(admin::addCommand);

        while (tripLength > 0.0) {
            tripLength = moveVehicleOnRoute(vehicle1, currentRoute, tutorSystem, tripLength);

            if (vehicle1.getCurrentMilestone() >= currentRoute.getLength()) {
                exitStation.attach(vehicle1);

                exitStation.notifyObserver(vehicle1, "exited the route " + currentRoute.getName() + "\n");

                entranceStation.notifyObserver(vehicle1, "entered the route " + currentRoute.getName() + "\n");

                currentRoute = tutorSystem.getRoutes().get(tutorSystem.getRoutes().indexOf(route2));
            }

            if (tripLength <= 0.0) {
                System.out.println("Trip ended...\n");
                break;
            }
        }

        if (tutorSystem.getInfractions().containsKey(vehicle1)) {
            tutorSystem.getInfractions().get(vehicle1).forEach((route, infractions) -> {
                tutorSystem.sendAllInfractions(vehicle1, policeStation, route);
                tutorSystem.sendMostSevereInfraction(policeStation, route, vehicle1);
            });
        }

        statisticsCommands.forEach(admin::executeCommand);
    }
}
