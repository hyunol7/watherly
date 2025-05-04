package com.weatherly.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DiaryRequestDto {
    private String text;
    private LocalDate date;
}
