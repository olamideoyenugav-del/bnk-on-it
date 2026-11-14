import java.util.Scanner;

// Shared console scanner is static, never serialized with customer objects.
public final class Input {
    private static final Scanner SCANNER = new Scanner(System.in);
    private Input() { }
    public static String readLine(String prompt) {
        System.out.print(prompt);
        if (!SCANNER.hasNextLine()) return "0";
        return SCANNER.nextLine().trim();
    }
    public static int readInt(String prompt) {
        while (true) {
            try { return Integer.parseInt(readLine(prompt)); }
            catch (NumberFormatException e) { System.out.println("Please enter a whole number."); }
        }
    }
    public static double readAmount(String prompt) {
        while (true) {
            try {
                double amount = Double.parseDouble(readLine(prompt));
                if (Double.isFinite(amount) && amount > 0) return amount;
            } catch (NumberFormatException e) { /* retry */ }
            System.out.println("Please enter a positive amount.");
        }
    }
}
