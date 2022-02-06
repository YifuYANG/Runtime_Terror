package model;

public interface UserDao {
    public int registerUser(User user);
    public String loginUser(User user);
}
