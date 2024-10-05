public class Utility {

    public static String formatMoney(float money, boolean longForm) {
        if (longForm) return String.format("£%,.2f", money);
        return formatMoney(money);
    }

    public static String formatMoney(float money) {
        float millions = money / 1_000_000;

        // Format the number to 2 decimal places if it's not a whole number, otherwise no decimal places
        String formatted;
        if (millions % 1 == 0) {
            // No decimal part
            formatted = String.format("£%.0fM", millions);
        } else {
            // Round to 2 decimal places if there's a fractional part
            formatted = String.format("£%.1fM", Math.round(millions * 100.0) / 100.0);
        }

        return formatted;
    }
}
