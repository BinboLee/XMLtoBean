package com.XMLtoBean.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

// 加上这个！！！
@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class Body0001 {

    @XmlElement(name = "Msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}