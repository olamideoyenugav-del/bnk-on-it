import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String PIN;

    public User() { this("", ""); }
    public User(String userName, String PIN) {
        this.userName = userName;
        this.PIN = PIN;
    }
    public boolean login() {
        String name = Input.readLine("User name: ");
        String pin = Input.readLine("PIN: ");
        return login(name, pin);
    }
    public boolean login(String userName, String PIN) {
        return this.userName.equals(userName) && this.PIN.equals(PIN);
    }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserName() { return userName; }
    public void setPIN(String PIN) { this.PIN = PIN; }
    public String getPIN() { return PIN; }
    public abstract String getReport();
}
