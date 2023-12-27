package app;

import command.pattern.*;
import javafx.beans.property.SimpleStringProperty;
import observer.pattern.*;

/**
 * Test class
 */
public class Test {
    public static void main(String[] args) {
        TutorSystem tutorSystem = new TutorSystem();
        TrafficMonitoringSystem trafficMonitoringSystem = new TrafficMonitoringSystem();

        Route route = new Route("Autostrada del Sole", 130, trafficMonitoringSystem);
        PoliceStation policeStation = new PoliceStation("Polizia Autostradale");
        AutoVehicle auto = new AutoVehicle("AB123CD", "Mercedes", "Classe A", "Black", 95);
        AutoVehicle auto2 = new AutoVehicle("AB456CD", "Mercedes", "Classe B", "Black", 100);
        AutoVehicle auto3 = new AutoVehicle("AB789CD", "Mercedes", "Classe C", "Black", 105);

        route.addTutor(policeStation);
        route.addTutor(auto);

        Command addRouteCommand = new AddRouteCommand(tutorSystem, route);
        Command editRouteCommand = new EditRouteCommand(tutorSystem, route, new Route("Autostrada del Sole", 150, trafficMonitoringSystem));
        Command removeRouteCommand = new RemoveRouteCommand(tutorSystem, route);
        Command getSpeedStatisticsCommand = new GetSpeedStatisticsCommand(tutorSystem, route);

        Admin admin = new Admin();

        admin.addCommand(editRouteCommand);
        admin.addCommand(addRouteCommand);
        admin.addCommand(removeRouteCommand);
        admin.addCommand(getSpeedStatisticsCommand);

        admin.executeCommand(addRouteCommand);
        admin.executeCommand(editRouteCommand);
        admin.executeCommand(getSpeedStatisticsCommand);
        tutorSystem.getRoutes().forEach((r) -> {
            System.out.println(r.getRoute().get("name") + " - " + r.getRoute().get("speedLimit") + " km/h");
        });

        auto.enterRoute(route);
        auto2.enterRoute(route);
        auto3.enterRoute(route);

        auto.setSpeed(125);
        auto2.setSpeed(130);
        auto3.setSpeed(165);

        route.notifyAuto(auto, new SimpleStringProperty(auto.getSpeed() + " km/h"));
        route.notifyAuto(auto2, new SimpleStringProperty(auto2.getSpeed() + " km/h"));
        route.notifyAuto(auto3, new SimpleStringProperty(auto3.getSpeed() + " km/h"));

        auto.exitRoute(route);
        auto2.exitRoute(route);
        auto3.exitRoute(route);

        if (auto.getSpeed() > route.getSpeedLimit()) {
            System.out.println("Auto " + auto.getPlate() + " has exceeded the speed limit\n");
            trafficMonitoringSystem.reportInfraction(new Infraction("Speed limit exceeded"));
            trafficMonitoringSystem.sendInfractionToPolice(policeStation);
        }

        if (auto2.getSpeed() > route.getSpeedLimit()) {
            System.out.println("Auto " + auto2.getPlate() + " has exceeded the speed limit\n");
            trafficMonitoringSystem.reportInfraction(new Infraction("Speed limit exceeded"));
            trafficMonitoringSystem.sendInfractionToPolice(policeStation);
        }

        if (auto3.getSpeed() > route.getSpeedLimit()) {
            System.out.println("Auto " + auto3.getPlate() + " has exceeded the speed limit\n");
            trafficMonitoringSystem.reportInfraction(new Infraction("Speed limit exceeded"));
            trafficMonitoringSystem.sendInfractionToPolice(policeStation);
        }
    }
}
