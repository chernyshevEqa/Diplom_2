package models.userPojo;

public class CreateUserPojo {
    private String email;
    private String password;
    private String name;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public CreateUserPojo withEmail(String email) {
        this.email = email;
        return this;
    }

    public CreateUserPojo withPassword(String password) {
        this.password = password;
        return this;
    }

    public CreateUserPojo withName(String name){
        this.name = name;
        return this;
    }
}
