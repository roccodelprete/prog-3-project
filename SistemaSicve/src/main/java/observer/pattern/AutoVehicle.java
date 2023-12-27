package observer.pattern;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Class to represent an auto vehicle
 */
public class AutoVehicle extends Tutor {
    /**
     * The auto plate
     */
    private SimpleStringProperty plate;
    /**
     * The auto brand
     */
    private SimpleStringProperty brand;
    /**
     * The auto model
     */
    private SimpleStringProperty model;
    /**
     * The auto color
     */
    private SimpleStringProperty color;
    /**
     * The auto speed
     */
    private SimpleIntegerProperty speed;

    /**
     * AutoVehicle constructor
     * @param plate The auto plate
     * @param brand The auto brand
     * @param model The auto model
     * @param color The auto color
     * @param speed The auto speed
     */
    public AutoVehicle(String plate, String brand, String model, String color, Integer speed) {
        this.plate = new SimpleStringProperty(plate);
        this.brand = new SimpleStringProperty(brand);
        this.model = new SimpleStringProperty(model);
        this.color = new SimpleStringProperty(color);
        this.speed = new SimpleIntegerProperty(speed);
    }

    /**
     * function to get the auto plate
     */
    public String getPlate() {
        return plate.get();
    }

    /**
     * function to set the auto plate
     * @param plate The auto plate to set
     */
    public void setPlate(String plate) {
        this.plate.set(plate);
    }

    /**
     * function to get the auto brand
     */
    public String getBrand() {
        return brand.get();
    }

    /**
     * function to set the auto brand
     * @param brand The auto brand to set
     */
    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    /**
     * function to get the auto model
     */
    public String getModel() {
        return model.get();
    }

    /**
     * function to set the auto model
     * @param model The auto model to set
     */
    public void setModel(String model) {
        this.model.set(model);
    }

    /**
     * function to get the auto color
     */
    public String getColor() {
        return color.get();
    }

    /**
     * function to set the auto color
     * @param color The auto color to set
     */
    public void setColor(String color) {
        this.color.set(color);
    }

    /**
     * function to get the auto speed
     */
    public Integer getSpeed() {
        return speed.get();
    }

    /**
     * function to set the auto speed
     * @param speed The auto speed to set
     */
    public void setSpeed(Integer speed) {
        this.speed.set(speed);
    }

    /**
     * function to enter a route
     * @param route The route to enter
     */
    public void enterRoute(Route route) {
        route.addTutor(this);
        System.out.println("\nAuto: " + this.getPlate() + " entered route " + route.getRoute().get("name") + "\n");
        route.notifyAuto(this, new SimpleStringProperty(getSpeed() + " km/h"));
    }

    /**
     * function to exit a route
     * @param route The route to exit
     */
    public void exitRoute(Route route) {
        route.removeTutor(this);
        System.out.println("\nAuto: " + this.getPlate() + " exited route " + route.getRoute().get("name") + "\n");
    }

    /**
     * function to update the auto speed
     * @param newSpeed The new auto speed
     */
    @Override
    public void updateSpeed(AutoVehicle auto, SimpleStringProperty newSpeed) {
        System.out.println("\nAuto: " + this.getPlate() + " - " + newSpeed.get() + "\n");
    }
}
