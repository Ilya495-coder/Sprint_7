public class LoginRequest {
    public String login;
    public String password;

    public LoginRequest(String password, String login) {
        this.password = password;
        this.login = login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
