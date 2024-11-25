package models.userPojo;

public class UserCredsPojo {

    private String email;
    private String password;

    public UserCredsPojo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCredsPojo credsFromUser(CreateUserPojo user) {
        return new UserCredsPojo(user.getEmail(), user.getPassword());
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
