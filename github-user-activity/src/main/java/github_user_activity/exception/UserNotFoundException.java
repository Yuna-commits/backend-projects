package github_user_activity.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message){
        super(message);
    }

}
