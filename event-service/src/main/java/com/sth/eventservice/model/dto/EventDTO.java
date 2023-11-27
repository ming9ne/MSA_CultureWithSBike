package com.sth.eventservice.model.dto;

import com.sth.eventservice.model.entity.Event;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {

    private String areaNm;

    private String eventNm;

    public Event toEntity() {
        return Event.builder()
                .areaNm(areaNm)
                .eventNm(eventNm)
                .build();
    }
}
