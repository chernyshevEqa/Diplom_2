package models.userPojo;

public class UserData {

    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
