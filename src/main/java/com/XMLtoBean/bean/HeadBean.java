package com.XMLtoBean.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 固定报文头，所有报文通用不变
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadBean {

    @XmlElement(name = "trancode")
    private String trancode;

    public String getTrancode() {
        return trancode;
    }

    public void setTrancode(String trancode) {
        this.trancode = trancode;
    }
}