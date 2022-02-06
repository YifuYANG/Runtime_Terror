package app.exception;

public class UserNotFoundException extends Exception{
    private Long user_id;
    public UserNotFoundException(Long user_id){
        super(String.format("User is not found with ID : '%s'", user_id));
    }
}
