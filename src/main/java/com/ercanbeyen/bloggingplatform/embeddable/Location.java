package com.ercanbeyen.bloggingplatform.embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private String country;
    private String city;
}
