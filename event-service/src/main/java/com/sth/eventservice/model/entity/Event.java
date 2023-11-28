package com.sth.eventservice.model.entity;

import com.sth.eventservice.model.dto.EventDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Event {
    @Id
    private String areaNm;
    @Column
    private String eventNm;

    public EventDTO toDto() {
        return EventDTO.builder()
                .areaNm(areaNm)
                .eventNm(eventNm)
                .build();
    }
}
