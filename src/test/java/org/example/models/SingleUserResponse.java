package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleUserResponse {
    Integer id;
    String email, first_name, last_name, avatar;

}
