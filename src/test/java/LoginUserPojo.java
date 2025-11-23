public class LoginUserPojo {
    public String login;
    public String password;
    public String firstName;

    public LoginUserPojo(String password, String login, String firstName) {
        this.password = password;
        this.login = login;
        this.firstName = firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }
}
