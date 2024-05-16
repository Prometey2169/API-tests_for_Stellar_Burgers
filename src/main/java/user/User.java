package user;

import lombok.*;

@Setter
@Getter
public class User {

    private String email;
    private String name;
    private String password;


    public User(String email, String name, String password){
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
}

