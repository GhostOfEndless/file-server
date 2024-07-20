package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "user_files", name = "t_user_file")
public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_title")
    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @Column(name = "c_description")
    @NotNull
    @Size(max = 1000)
    private String description;

    @Column(name = "c_creation_date")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Moscow")
    @JsonProperty("creation_date")
    private Date creationDate;

    @Column(name = "c_data")
    @NotNull
    private String data;
}
