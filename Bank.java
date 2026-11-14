import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Bank implements HasMenu {
    private final Admin admin;
    private CustomerList customers;
    private static final String DATA_FILE = "customers.dat";

    public Bank() {
        admin = new Admin();
        customers = new CustomerList();
        // To reset test data, temporarily uncomment these two lines for one run:
        // loadSampleCustomers();
        // saveCustomers();
        loadCustomers();
        start();
        saveCustomers();
    }
    public static void main(String[] args) { new Bank(); }
    public String menu() {
        return "\nBank Menu\n0) Exit system\n1) Login as admin\n2) Login as customer\n";
    }
    public void start() {
        while (true) {
            System.out.print(menu());
            switch (Input.readInt("Action: ")) {
                case 0: return;
                case 1:
                    System.out.println("Admin login");
                    if (admin.login()) startAdmin();
                    else System.out.println("Incorrect admin username or PIN.");
                    break;
                case 2: loginAsCustomer(); break;
                default: System.out.println("Invalid selection.");
            }
        }
    }
    public void startAdmin() {
        while (true) {
            System.out.print(admin.menu());
            switch (Input.readInt("Action: ")) {
                case 0: return;
                case 1: fullCustomerReport(); break;
                case 2: addUser(); break;
                case 3: applyInterest(); break;
                default: System.out.println("Invalid selection.");
            }
        }
    }
    // Alias for the alternate method name used in the assignment description.
    public void adminStart() { startAdmin(); }
    public void fullCustomerReport() {
        System.out.println("Full customer report");
        for (Customer customer : customers) System.out.println(customer.getReport());
    }
    public void addUser() {
        System.out.println("Add User");
        String name = Input.readLine("Name: ");
        if (name.isEmpty() || name.equalsIgnoreCase("admin")) {
            System.out.println("Please choose a valid customer name."); return;
        }
        for (Customer customer : customers) {
            if (customer.getUserName().equalsIgnoreCase(name)) {
                System.out.println("That username is already taken."); return;
            }
        }
        String pin = Input.readLine("PIN: ");
        if (!pin.matches("[0-9]{4}")) {
            System.out.println("PIN must contain exactly 4 digits."); return;
        }
        customers.add(new Customer(name, pin));
        saveCustomers();
        System.out.println("Customer added: " + name);
    }
    public void applyInterest() {
        System.out.println("Apply interest");
        for (Customer customer : customers) customer.getSavings().calcInterest();
        saveCustomers();
    }
    public void loginAsCustomer() {
        System.out.println("Customer login");
        String name = Input.readLine("User name: ");
        String pin = Input.readLine("PIN: ");
        Customer currentCustomer = null;
        for (Customer customer : customers) {
            if (customer.login(name, pin)) {
                currentCustomer = customer;
                break;
            }
        }
        if (currentCustomer == null) System.out.println("Incorrect customer username or PIN.");
        else {
            System.out.println("Welcome, " + currentCustomer.getUserName() + "!");
            currentCustomer.start();
            saveCustomers();
        }
    }
    public void loadSampleCustomers() {
        customers = new CustomerList();
        Customer alice = new Customer("Alice", "1111");
        alice.getChecking().setBalance(1000);
        alice.getSavings().setBalance(1000);
        customers.add(alice);
        customers.add(new Customer("Bob", "2222"));
        customers.add(new Customer("Cindy", "3333"));
    }
    public void saveCustomers() {
        Path destination = Path.of(DATA_FILE);
        Path temp = Path.of(DATA_FILE + ".tmp");
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(temp.toFile()))) {
            output.writeObject(customers);
        } catch (IOException e) {
            System.out.println("Could not save customers: " + e.getMessage()); return;
        }
        try {
            Files.move(temp, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Could not finish saving customers: " + e.getMessage());
        }
    }
    public void loadCustomers() {
        if (!Files.exists(Path.of(DATA_FILE))) {
            loadSampleCustomers();
            saveCustomers();
            return;
        }
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            Object loaded = input.readObject();
            if (!(loaded instanceof CustomerList)) throw new IOException("Unexpected data format");
            customers = (CustomerList) loaded;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Unable to load saved customers: " + e.getMessage());
            System.out.println("Existing data was not overwritten. To reset, use the two commented lines in the constructor.");
            // Don't overwrite an unreadable data file with sample records.
            throw new IllegalStateException("Customer data could not be loaded", e);
        }
    }
}
