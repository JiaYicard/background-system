package com.zzs.vo;

import lombok.Data;

/**
 * @author mm013
 * @Date 2021/5/20 17:14
 */
@Data
public class EmailCodeVo {

    private String result;

    private String token;

    public EmailCodeVo() {
    }

    public EmailCodeVo(String result, String token) {
        this.result = result;
        this.token = token;
    }

    public EmailCodeVo(String result) {
        this.result = result;
    }
}
