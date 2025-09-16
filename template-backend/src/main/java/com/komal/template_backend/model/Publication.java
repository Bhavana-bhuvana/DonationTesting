package com.komal.template_backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Setter
@Getter
@Document(collection = "publications")
public class Publication {

    // Getters and Setters
    @Id
    private String id;

    private String title;
    private String description;
    private String imageUrl;

    // Constructors
    public Publication() {}

    public Publication(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;

    }

}
