package uz.attendance_system.attendance_system.web_controller.vm;

import javax.validation.constraints.NotNull;

public class LoginVM {
    
    @NotNull
    private String Login;
    
    @NotNull
    private String Password;

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
