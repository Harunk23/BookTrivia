package com.example.booktrivia.OpenTdb;

import java.util.List;

public class OpenTdbResponse {
    // Alles wat we terugkrijgen van de API request van OpenTDB
    // Elk vraag wordt opgeslagen in het OpenTdbQuestion object
    private List<OpenTdbQuestion> results;

    public List<OpenTdbQuestion> getResults() {
        return results;
    }

    public void setResults(List<OpenTdbQuestion> results) {
        this.results = results;
    }
}
