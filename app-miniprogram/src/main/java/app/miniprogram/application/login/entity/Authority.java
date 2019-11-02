package app.miniprogram.application.login.entity;

import lombok.Data;

@Data
public class Authority {

    private Long roleId;
    private Long menuId;
    private String menuName;
}
