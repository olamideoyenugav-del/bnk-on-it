import java.io.Serializable;

public class CheckingAccount implements HasMenu, Serializable {
    private static final long serialVersionUID = 1L;
    private double balance;

    public CheckingAccount() { this(0); }
    public CheckingAccount(double balance) {
        if (!Double.isFinite(balance) || balance < 0) throw new IllegalArgumentException("Invalid balance");
        this.balance = balance;
    }
    public static void main(String[] args) { new CheckingAccount(1000).start(); }
    public String menu() {
        return "\nChecking Menu\n0) Exit this menu\n1) Check balance\n2) Deposit\n3) Withdraw\n";
    }
    public void start() {
        while (true) {
            System.out.print(menu());
            switch (Input.readInt("Action: ")) {
                case 0: return;
                case 1: checkBalance(); break;
                case 2: makeDeposit(); break;
                case 3: makeWithdrawal(); break;
                default: System.out.println("Invalid selection.");
            }
        }
    }
    public double getBalance() { return balance; }
    public String getBalanceString() { return String.format("$%.2f", balance); }
    public void setBalance(double balance) {
        if (!Double.isFinite(balance) || balance < 0) throw new IllegalArgumentException("Invalid balance");
        this.balance = balance;
    }
    public void checkBalance() { System.out.println("Balance: " + getBalanceString()); }
    public double getDouble() { return Input.readAmount("Amount: $"); }
    public void makeDeposit() {
        double amount = getDouble();
        setBalance(balance + amount);
        System.out.println("New balance: " + getBalanceString());
    }
    public void makeWithdrawal() {
        double amount = getDouble();
        if (amount > balance) System.out.println("Insufficient funds.");
        else {
            setBalance(balance - amount);
            System.out.println("New balance: " + getBalanceString());
        }
    }
}
