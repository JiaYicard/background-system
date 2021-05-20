package com.zzs.vo;

import lombok.Data;

/**
 * @author mm013
 * @Date 2021/5/20 17:14
 */
@Data
public class EmailCodeVo {
    private String emailCode;

    private String result;

    public EmailCodeVo() {
    }

    public EmailCodeVo(String emailCode, String result) {
        this.emailCode = emailCode;
        this.result = result;
    }
}
