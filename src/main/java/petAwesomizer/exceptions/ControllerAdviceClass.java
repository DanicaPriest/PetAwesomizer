package petAwesomizer.exceptions;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;


@ControllerAdvice
    public class ControllerAdviceClass {


        @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
        @ExceptionHandler(APIUnavailableException.class)
        public @ResponseBody CustomException handle404() {
            CustomException error = new CustomException();
            error.setMessage("Apologies, it appears the API is currently offline. We are not able" +
                    " to process your request. Please try again later.");
            error.setStatus(503);
            return error;
        }


        @ExceptionHandler(ApiKeyException.class)
        public @ResponseBody CustomException handle405(ApiKeyException e) {
            CustomException error = new CustomException();
            error.setReason("Wrong api-key");
            error.setMessage(e.toString());
            error.setStatus(502);
            return error;
        }



    @ExceptionHandler(UserNullException.class)
    public @ResponseBody CustomException handle400(UserNullException e) {
        CustomException error = new CustomException();
        error.setReason("user not found");
        error.setMessage(e.toString());
        error.setStatus(400);
        return error;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public @ResponseBody CustomException handle401(MissingServletRequestParameterException er) {
        CustomException error = new CustomException();
        error.setReason(er.getParameterName() + " is missing");
        error.setMessage("user and api-key parameters are required for access to TacoCatFacts (you don't want to give the power of TacoCatFacts to just anyone!)");
        error.setStatus(400);
        return error;
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public @ResponseBody CustomException handle409(NoHandlerFoundException e) {
        CustomException error = new CustomException();
        error.setReason("path not found");
        error.setMessage("path " + e.getRequestURL() + " does not exist (don't forget to put a ? in front of user) ");
        error.setStatus(404);
        return error;
    }
    @ExceptionHandler(PersistenceException.class)
    public @ResponseBody CustomException handle410(PersistenceException e) {
        CustomException error = new CustomException();
        error.setReason("connection not found");
        error.setMessage(e.getMessage());
        error.setStatus(404);
        return error;
    }
    @ExceptionHandler(NullPointerException.class)
    public @ResponseBody CustomException handle411(NullPointerException e) {
        CustomException error = new CustomException();
        error.setReason(e.getMessage());
        error.setMessage("Your location is not recognized");
        error.setStatus(404);
        return error;
    }
    }

