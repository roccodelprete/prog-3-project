package app;

import command.pattern.*;
import observer.pattern.*;

/**
 * Test class
 * @author Rocco Del Prete
 */
public class Test {
    public static void main(String[] args) {
        Admin admin = new Admin();
        TutorSystem tutorSystem = new TutorSystem();
        PoliceStation policeStation = new PoliceStation("Police Station 1");

        Route route1 = new Route("Route 1", 90.0);
        Route route2 = new Route("Route 2", 80.0);

        Command addRouteCommand = new AddRouteCommand(tutorSystem, route1);
        Command addRouteCommand2 = new AddRouteCommand(tutorSystem, route2);

        admin.addCommand(addRouteCommand);
        admin.addCommand(addRouteCommand2);

        admin.executeCommands();

        TutorStation entranceStation = new TutorStation();
        TutorStation exitStation = new TutorStation();

        Vehicle vehicle1 = new Vehicle("ABC123");
        Vehicle vehicle2 = new Vehicle("DEF456");

        Command getStatisticsCommand = new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route1, vehicle1);
        Command getStatisticsCommand2 = new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route1, vehicle2);
        Command getStatisticsCommand3 = new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route2, vehicle2);
        Command getStatisticsCommand4 = new GetRouteVehicleSpeedStatisticsCommand(tutorSystem, route2, vehicle1);

        Command getStatisticsCommand5 = new GetRouteSpeedStatisticsCommand(tutorSystem, route1);
        Command getStatisticsCommand6 = new GetRouteSpeedStatisticsCommand(tutorSystem, route2);

        admin.addCommand(getStatisticsCommand);
        admin.addCommand(getStatisticsCommand2);
        admin.addCommand(getStatisticsCommand3);
        admin.addCommand(getStatisticsCommand4);
        admin.addCommand(getStatisticsCommand5);
        admin.addCommand(getStatisticsCommand6);

        entranceStation.attach(vehicle1);
        entranceStation.attach(vehicle2);

        entranceStation.notifyObservers("entered the route with tutor system");

        vehicle1.handleSpeeding(121.0, route1, tutorSystem);
        vehicle1.handleSpeeding(123.0, route1, tutorSystem);
        vehicle1.handleSpeeding(125.0, route1, tutorSystem);
        vehicle2.handleSpeeding(127.0, route1, tutorSystem);
        vehicle2.handleSpeeding(130.0, route1, tutorSystem);
        vehicle2.handleSpeeding(123.0, route1, tutorSystem);

        admin.executeCommand(getStatisticsCommand);
        admin.executeCommand(getStatisticsCommand2);
        admin.executeCommand(getStatisticsCommand3);
        admin.executeCommand(getStatisticsCommand4);

        admin.executeCommand(getStatisticsCommand5);
        admin.executeCommand(getStatisticsCommand6);

        tutorSystem.sendAllInfractions(vehicle1, policeStation, route1);
        tutorSystem.sendAllInfractions(vehicle1, policeStation, route2);

        tutorSystem.sendAllInfractions(vehicle2, policeStation, route1);
        tutorSystem.sendAllInfractions(vehicle2, policeStation, route2);

        tutorSystem.sendMostSevereInfraction(policeStation, route1, vehicle1);
        tutorSystem.sendMostSevereInfraction(policeStation, route1, vehicle2);

        tutorSystem.sendMostSevereInfraction(policeStation, route2, vehicle1);
        tutorSystem.sendMostSevereInfraction(policeStation, route2, vehicle2);

        for (VehicleObserver vehicle : entranceStation.getObservers()) {
            exitStation.attach(vehicle);
        }

        exitStation.notifyObservers("exited the route with tutor system");

        for (VehicleObserver vehicle : entranceStation.getObservers()) {
            entranceStation.detach(vehicle);
        }

        for (VehicleObserver vehicle : exitStation.getObservers()) {
            exitStation.detach(vehicle);
        }
    }
}
