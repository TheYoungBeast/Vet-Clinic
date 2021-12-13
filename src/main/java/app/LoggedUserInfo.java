package app;

import entity.Employees;
import entity.Users;

public class LoggedUserInfo
{
    private static final LoggedUserInfo userInfo = new LoggedUserInfo();
    private Employees employee;
    private Users user;

    private LoggedUserInfo() {};

    static LoggedUserInfo getInstance() {
        return userInfo;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
