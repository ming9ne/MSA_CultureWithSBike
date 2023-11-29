package com.sth.eventservice.vo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Eventdata {
    @XmlElement(name = "CODENAME")
    private String codeName;

    @XmlElement(name = "GUNAME")
    private String guname;

    @XmlElement(name = "TITLE")
    private String title;

    @XmlElement(name = "DATE")
    private String date;

    @XmlElement(name = "PLACE")
    private String place;

    @XmlElement(name = "ORG_NAME")
    private String orgName;

    @XmlElement(name = "USE_TRGT")
    private String useTarget;

    @XmlElement(name = "USE_FEE")
    private String useFee;

    @XmlElement(name = "PLAYER")
    private String player;

    @XmlElement(name = "PROGRAM")
    private String program;

    @XmlElement(name = "ETC_DESC")
    private String etcDesc;

    @XmlElement(name = "ORG_LINK")
    private String orgLink;

    @XmlElement(name = "MAIN_IMG")
    private String mainImg;

    @XmlElement(name = "RGSTDATE")
    private String rgstDate;

    @XmlElement(name = "TICKET")
    private String ticket;

    @XmlElement(name = "STRTDATE")
    private String startDate;

    @XmlElement(name = "END_DATE")
    private String endDate;

    @XmlElement(name = "THEMECODE")
    private String themeCode;

    @XmlElement(name = "LOT")
    private double lot;

    @XmlElement(name = "LAT")
    private double lat;

    @XmlElement(name = "IS_FREE")
    private String isFree;

    @XmlElement(name = "HMPG_ADDR")
    private String hmpgAddr;
}
