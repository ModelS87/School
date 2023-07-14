package ru.hogwards.school.mapper;

import org.springframework.stereotype.Component;
import ru.hogwards.school.dto.FacultyDtoIn;
import ru.hogwards.school.dto.FacultyDtoOut;
import ru.hogwards.school.dto.StudentDtoIn;
import ru.hogwards.school.dto.StudentDtoOut;
import ru.hogwards.school.entity.Faculty;
import ru.hogwards.school.entity.Student;

@Component
public class FacultyMapper {
    public FacultyDtoOut toDto(Faculty faculty){
            FacultyDtoOut facultyDtoOut = new FacultyDtoOut();
            facultyDtoOut.setId(faculty.getId());
            facultyDtoOut.setName(faculty.getName());
            facultyDtoOut.setColour(faculty.getColour());
            return facultyDtoOut;
        }
        public Faculty toEntity(FacultyDtoIn facultyDtoIn){
            Faculty faculty = new Faculty();
            faculty.setName(facultyDtoIn.getName());
            faculty.setColour(facultyDtoIn.getColour());
            return faculty;
        }
    }
