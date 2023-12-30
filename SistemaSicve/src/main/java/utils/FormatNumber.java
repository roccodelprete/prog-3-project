package utils;

/**
 * Class to format a number
 */
public class FormatNumber {
    /**
     * Function to format a number to a string with 2 digits
     * @param number The number to format
     * @return The formatted number
     */
    public static String formatNumber(Double number) {
        return String.format("%.2f", number);
    }
}
