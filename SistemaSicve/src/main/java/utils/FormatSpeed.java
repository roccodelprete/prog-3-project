package utils;

import org.jetbrains.annotations.NotNull;

/**
 * Class to format a number
 * @author Rocco Del Prete
 */
public class FormatSpeed {
    /**
     * Enum to represent the speed unit
     * @author Rocco Del Prete
     */
    public enum SpeedUnit {
        KMH("km/h"),
        MPH("mph");

        /**
         * The unit
         */
        private final String unit;

        /**
         * Constructor
         * @param unit The unit to set
         */
        SpeedUnit(String unit) {
            this.unit = unit;
        }

        /**
         * Getter for the unit
         * @return The unit
         */
        public String getUnit() {
            return unit;
        }
    }

    /**
     * Function to format a number to a string with 2 digits
     * @param number The number to format
     * @return The formatted number
     */
    public static @NotNull String formatSpeed(Integer number, @NotNull SpeedUnit speedUnit) {
        return number + " " + speedUnit.getUnit();
    }

    /**
     * Function to format the speed
     * @param number The number to format
     * @param speedUnit The speed unit to use
     * @return The formatted number
     */
    public static @NotNull String formatSpeed(Double number, @NotNull SpeedUnit speedUnit) {
        return number + " " + speedUnit.getUnit();
    }
}
