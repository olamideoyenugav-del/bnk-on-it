public class SavingsAccount extends CheckingAccount {
    private static final long serialVersionUID = 1L;
    private double interestRate;

    public SavingsAccount() { this(0, 0.05); }
    public SavingsAccount(double balance) { this(balance, 0.05); }
    public SavingsAccount(double balance, double interestRate) {
        super(balance);
        setInterestRate(interestRate);
    }
    public static void main(String[] args) { new SavingsAccount(1000).start(); }
    @Override public String menu() {
        return "\nSavings Menu\n0) Exit this menu\n1) Check balance\n2) Deposit\n3) Withdraw\n4) Show interest rate\n";
    }
    @Override public void start() {
        while (true) {
            System.out.print(menu());
            switch (Input.readInt("Action: ")) {
                case 0: return;
                case 1: checkBalance(); break;
                case 2: makeDeposit(); break;
                case 3: makeWithdrawal(); break;
                case 4: System.out.printf("Interest rate: %.2f%%%n", interestRate * 100); break;
                default: System.out.println("Invalid selection.");
            }
        }
    }
    public void calcInterest() {
        setBalance(getBalance() * (1 + interestRate));
        System.out.println("New balance: " + getBalanceString());
    }
    public void setInterestRate(double interestRate) {
        if (!Double.isFinite(interestRate) || interestRate < 0) throw new IllegalArgumentException("Invalid rate");
        this.interestRate = interestRate;
    }
    public double getInterestRate() { return interestRate; }
}
