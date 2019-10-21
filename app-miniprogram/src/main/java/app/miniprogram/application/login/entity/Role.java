package app.miniprogram.application.login.entity;


import lombok.Data;

@Data
public class Role {
    private Long uId;
    private Long menuId;
    private String menuName;
    private Long locked;
}
