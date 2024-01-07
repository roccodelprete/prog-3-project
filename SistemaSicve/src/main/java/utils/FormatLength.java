package utils;

import org.jetbrains.annotations.NotNull;

/**
 * Class to format the length
 * @Author Rocco Del Prete
 */
public class FormatLength {
    /**
     * Enum to represent the length unit
     * @author Rocco Del Prete
     */
    public enum LengthUnit {
        KM("km"),
        MILES("miles");

        /**
         * The unit
         */
        private final String unit;

        /**
         * Constructor
         * @param unit The unit to set
         */
        LengthUnit(String unit) {
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
     * Function to format the speed
     * @param number The number to format
     * @param lengthUnit The length unit to use
     * @return The formatted number
     */
    public static @NotNull String formatLength(Integer number, @NotNull LengthUnit lengthUnit) {
        return number + " " + lengthUnit.getUnit();
    }

    /**
     * Function to format the speed
     * @param number The number to format
     * @param lengthUnit The length unit to use
     * @return The formatted number
     */
    public static @NotNull String formatLength(Double number, @NotNull LengthUnit lengthUnit) {
        return number + " " + lengthUnit.getUnit();
    }
}
