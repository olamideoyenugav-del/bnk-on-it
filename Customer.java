public class Customer extends User implements HasMenu {
    private static final long serialVersionUID = 1L;
    private CheckingAccount checking;
    private SavingsAccount savings;

    public Customer() { this("", ""); }
    public Customer(String userName, String PIN) {
        super(userName, PIN);
        checking = new CheckingAccount();
        savings = new SavingsAccount();
    }
    public static void main(String[] args) { new Customer("Demo", "1234").start(); }
    public String menu() {
        return "\nCustomer Menu - " + getUserName() + "\n0) Exit this menu\n1) Checking account\n2) Savings account\n3) Account report\n4) Change PIN\n";
    }
    public void start() {
        while (true) {
            System.out.print(menu());
            switch (Input.readInt("Action: ")) {
                case 0: return;
                case 1: checking.start(); break;
                case 2: savings.start(); break;
                case 3: System.out.println(getReport()); break;
                case 4: changePIN(); break;
                default: System.out.println("Invalid selection.");
            }
        }
    }
    public void changePIN() {
        String oldPin = Input.readLine("Current PIN: ");
        if (!getPIN().equals(oldPin)) {
            System.out.println("Incorrect current PIN."); return;
        }
        String newPin = Input.readLine("New PIN (4 digits): ");
        if (!newPin.matches("[0-9]{4}")) {
            System.out.println("PIN must contain exactly 4 digits."); return;
        }
        setPIN(newPin);
        System.out.println("PIN updated.");
    }
    public CheckingAccount getChecking() { return checking; }
    public SavingsAccount getSavings() { return savings; }
    @Override public String getReport() {
        return "User: " + getUserName() + ", Checking: " + checking.getBalanceString()
            + ", Savings: " + savings.getBalanceString();
    }
}
