package ru.hogwards.school.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.hogwards.school.dto.FacultyDtoIn;
import ru.hogwards.school.dto.FacultyDtoOut;
import ru.hogwards.school.dto.StudentDtoOut;
import ru.hogwards.school.entity.Faculty;
import ru.hogwards.school.exception.FacultyNotFoundException;
import ru.hogwards.school.mapper.FacultyMapper;
import ru.hogwards.school.mapper.StudentMapper;
import ru.hogwards.school.repository.FacultyRepository;
import ru.hogwards.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;

    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository,
                          FacultyMapper facultyMapper,
                          StudentMapper studentMapper) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.facultyMapper = facultyMapper;
        this.studentMapper = studentMapper;
    }

    public FacultyDtoOut create(FacultyDtoIn facultyDtoIn) {
        return facultyMapper.toDto(
                facultyRepository.save(
                        facultyMapper.toEntity(facultyDtoIn)
                )
        );
    }

    public FacultyDtoOut update(long id, FacultyDtoIn facultyDtoIn) {
        return facultyRepository.findById(id)
                .map(oldFaculty -> {
                    oldFaculty.setColour(facultyDtoIn.getColour());
                    oldFaculty.setName(facultyDtoIn.getName());
                    return facultyMapper.toDto(facultyRepository.save(oldFaculty));
                })
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public FacultyDtoOut delete(long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return facultyMapper.toDto(faculty);
    }

    public FacultyDtoOut get(long id) {
        return facultyRepository.findById(id)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public List<FacultyDtoOut> findAll(@Nullable String colour) {
        return Optional.ofNullable(colour)
                .map(facultyRepository::findAllByColour)
                .orElseGet(facultyRepository::findAll).stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<FacultyDtoOut> findByColourOrName(String colourOrName) {
        return facultyRepository.findAllByColourContainingIgnoreCaseOrNameContainingIgnoreCase(
                        colourOrName,
                        colourOrName
                ).stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> findStudents(long id) {
        return studentRepository.findAllByFaculty_Id(id).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public String getLongestName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .get();
    }
    public Integer sum(){
        long start = System.currentTimeMillis();
        int res = Stream.iterate(1, a-> a + 1)
                .limit(1_000_000)
                .reduce(0,(a,b)-> a + b);
        long finish = System.currentTimeMillis();
        long dif = finish - start;
        System.out.println("simple: " + dif);
        return res;
    }
    public Integer sumImpr(){
        long start = System.currentTimeMillis();
        int res = Stream.iterate(1, a-> a + 1)
                .parallel()
                .limit(1_000_000)
                .reduce(0,(a,b)-> a + b);
        long finish = System.currentTimeMillis();
        long dif = finish - start;
        System.out.println("impr: " + dif);
        return res;
    }
}
