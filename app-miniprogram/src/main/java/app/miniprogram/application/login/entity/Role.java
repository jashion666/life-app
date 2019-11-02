package app.miniprogram.application.login.entity;


import lombok.Data;

import java.util.List;

/**
 * @author :wkh
 */
@Data
public class Role {
    private Long uId;
    private Long roleId;
    private String roleName;
    private Long locked;
    private List<Authority> authorities;
}
