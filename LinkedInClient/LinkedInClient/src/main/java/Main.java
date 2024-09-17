import Controller.RetrofitBuilder;
import Controller.SignUpController;
import Model.*;
import Model.Requests.*;
import Model.Response.WatchPostSearchResults;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please enter server IP address:");
        String connectionIPString = scanner.next();
        String connectionUrl = "http://" + connectionIPString + ":8080";
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(connectionUrl);

        System.out.println(retrofitBuilder.syncCallSayHello().get(""));

        LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
        Messages loginResp = retrofitBuilder.syncCallLogin(loginCredentials);
        System.out.println(loginResp.getMessage());
        System.out.println(Cookies.getSessionToken());

    }
}
