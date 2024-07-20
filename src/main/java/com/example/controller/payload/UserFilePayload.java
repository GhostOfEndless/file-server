package com.example.controller.payload;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Date;

@Builder
public record UserFilePayload(
        @NotNull(message = "{file-server.files.create.title.is_null}")
        @Size(min = 3, max = 50, message = "{file-server.files.create.title.size_invalid}")
        @JsonProperty("title")
        String title,
        @NotNull(message = "{file-server.files.create.data.is_null}")
        @JsonProperty("data")
        String data,
        @NotNull(message = "{file-server.files.create.description.is_null}")
        @Size(max = 1000, message = "{file-server.files.create.description.size_invalid}")
        @JsonProperty("description")
        String description,
        @NotNull(message = "{file-server.files.create.creation_date.is_null}")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
        @JsonProperty("creation_date")
        Date creationDate
) {
}
