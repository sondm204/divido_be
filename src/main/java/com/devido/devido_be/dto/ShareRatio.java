package com.devido.devido_be.dto;

import java.math.BigDecimal;

public class ShareRatio {
    private UserDTO user;
    private BigDecimal ratio;

    public ShareRatio(UserDTO user, BigDecimal shareRatio) {
        this.user = user;
        this.ratio = shareRatio;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }
}
