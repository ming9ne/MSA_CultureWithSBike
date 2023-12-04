// com.sth.sbikeservice.vo.RentBikeStatus.java
package com.sth.sbikeservice.vo;

import com.sth.sbikeservice.model.dto.SbikeDTO;

import java.util.List;

public class RentBikeStatus {
    private RentBikeInfo rentBikeStatus;

    public RentBikeInfo getRentBikeStatus() {
        return rentBikeStatus;
    }

    public void setRentBikeStatus(RentBikeInfo rentBikeStatus) {
        this.rentBikeStatus = rentBikeStatus;
    }

    public static class RentBikeInfo {
        private List<SbikeDTO> row;

        public List<SbikeDTO> getRow() {
            return row;
        }

        public void setRow(List<SbikeDTO> row) {
            this.row = row;
        }
    }
}
