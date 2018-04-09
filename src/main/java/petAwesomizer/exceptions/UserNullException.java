package petAwesomizer.exceptions;

public class UserNullException extends Exception {
    public String toString(){
        return "user does not exist in the database";
    }
}
