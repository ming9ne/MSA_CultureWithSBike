package com.sth.eventservice.vo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "culturalEventInfo")
public class CulturalEventInfo {
    private List<Row> row = new ArrayList<>();  // 빈 리스트로 초기화

    @XmlElement(name = "row")
    public void setRow(List<Row> row) {
        this.row = row;
    }

    public List<Row> getRows() {
        return row;
    }

    @XmlRootElement(name = "row")
    public static class Row {
        private String GUNAME;
        private String TITLE;
        private String CODENAME;
        private String STRTDATE;
        private String END_DATE;
        private String PLACE;
        private String USE_FEE;
        private String PLAYER;
        private String PROGRAM;
        private String ORG_LINK;
        private Double LOT;
        private Double LAT;

        // Getter 및 Setter 메서드는 필요에 따라 추가

        public String getGUNAME() {
            return GUNAME;
        }

        public void setGUNAME(String GUNAME) {
            this.GUNAME = GUNAME;
        }

        public String getTITLE() {
            return TITLE;
        }

        public void setTITLE(String TITLE) {
            this.TITLE = TITLE;
        }

        public String getCODENAME() {
            return CODENAME;
        }

        public void setCODENAME(String CODENAME) {
            this.CODENAME = CODENAME;
        }

        public String getSTRTDATE() {
            return STRTDATE;
        }

        public void setSTRTDATE(String STRTDATE) {
            this.STRTDATE = STRTDATE;
        }

        public String getEND_DATE() {
            return END_DATE;
        }

        public void setEND_DATE(String END_DATE) {
            this.END_DATE = END_DATE;
        }

        public String getPLACE() {
            return PLACE;
        }

        public void setPLACE(String PLACE) {
            this.PLACE = PLACE;
        }

        public String getUSE_FEE() {
            return USE_FEE;
        }

        public void setUSE_FEE(String USE_FEE) {
            this.USE_FEE = USE_FEE;
        }

        public String getPLAYER() {
            return PLAYER;
        }

        public void setPLAYER(String PLAYER) {
            this.PLAYER = PLAYER;
        }

        public String getPROGRAM() {
            return PROGRAM;
        }

        public void setPROGRAM(String PROGRAM) {
            this.PROGRAM = PROGRAM;
        }

        public String getORG_LINK() {
            return ORG_LINK;
        }

        public void setORG_LINK(String ORG_LINK) {
            this.ORG_LINK = ORG_LINK;
        }

        public Double getLOT() {
            return LOT;
        }

        public void setLOT(Double LOT) {
            this.LOT = LOT;
        }

        public Double getLAT() {
            return LAT;
        }

        public void setLAT(Double LAT) {
            this.LAT = LAT;
        }
    }
}
