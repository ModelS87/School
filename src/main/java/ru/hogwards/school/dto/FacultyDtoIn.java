package ru.hogwards.school.dto;

public class FacultyDtoIn {
    private String name;
    private String colour;

    public FacultyDtoIn(String name, String colour) {
        this.name = name;
        this.colour = colour;
    }
    public FacultyDtoIn(){
}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
