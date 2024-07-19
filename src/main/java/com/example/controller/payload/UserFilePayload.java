package com.example.controller.payload;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record UserFilePayload(
        String title,
        String data,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
        @JsonProperty("creation_date")
        Date creationDate
) {
}
