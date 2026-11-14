public class Admin extends User implements HasMenu {
    private static final long serialVersionUID = 1L;
    public Admin() { super("admin", "0000"); }
    public String menu() {
        return "\nAdmin Menu\n0) Exit this menu\n1) Full customer report\n2) Add user\n3) Apply interest to savings accounts\n";
    }
    // Bank handles these actions because Bank owns the customer list.
    public void start() { }
    @Override public String getReport() {
        return "Admin: " + getUserName() + ", PIN: " + getPIN();
    }
}
