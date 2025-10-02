package pojo;

import java.util.List;

public class UsersResponse {
    private List<User> users;
    private Support support;

    public UsersResponse() {
    }

    public UsersResponse(List<User> users, Support support) {
        this.users = users;
        this.support = support;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Support getSupport() {
        return support;
    }

    public void setSupport(Support support) {
        this.support = support;
    }

    @Override
    public String toString() {
        return "UsersResponse{" +
                "users=" + users +
                ", support=" + support +
                '}';
    }
}
