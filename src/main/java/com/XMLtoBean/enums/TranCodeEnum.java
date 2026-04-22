package com.XMLtoBean.enums;

import com.XMLtoBean.bean.Body0001;

/**
 * 交易码 -> 对应Body实体类映射
 * 新增交易码只需要新增Bean+枚举加一行，无需改动主逻辑
 */
public enum TranCodeEnum {

    TRAN_0001("0001", Body0001.class);

    private final String trancode;
    private final Class<?> bodyClass;

    TranCodeEnum(String trancode, Class<?> bodyClass) {
        this.trancode = trancode;
        this.bodyClass = bodyClass;
    }

    /**
     * 根据交易码获取枚举
     */
    public static TranCodeEnum getByCode(String code) {
        for (TranCodeEnum e : values()) {
            if (e.trancode.equals(code)) {
                return e;
            }
        }
        throw new RuntimeException("不支持的交易码：" + code);
    }

    public String getTrancode() {
        return trancode;
    }

    public Class<?> getBodyClass() {
        return bodyClass;
    }
}