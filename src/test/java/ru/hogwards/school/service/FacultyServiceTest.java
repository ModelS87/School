package ru.hogwards.school.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwards.school.dto.FacultyDtoIn;
import ru.hogwards.school.dto.FacultyDtoOut;
import ru.hogwards.school.entity.Faculty;
import ru.hogwards.school.mapper.FacultyMapper;
import ru.hogwards.school.mapper.StudentMapper;
import ru.hogwards.school.repository.FacultyRepository;
import ru.hogwards.school.repository.StudentRepository;
import ru.hogwards.school.service.FacultyService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

        private FacultyRepository facultyRepository;
        private FacultyService facultyService;

        @BeforeEach
        public void beforeEach() {
            facultyRepository = mock(FacultyRepository.class);
            FacultyMapper facultyMapper = new FacultyMapper();
            facultyService = new FacultyService(
                    facultyRepository,
                    mock(StudentRepository.class),
                    facultyMapper,
                    new StudentMapper(facultyMapper, facultyRepository)
            );
        }

        @Test
        public void createTest() {
            FacultyDtoIn facultyDtoIn = new FacultyDtoIn();
            facultyDtoIn.setName("name");
            facultyDtoIn.setColour("colour");

            Faculty faculty = new Faculty();
            faculty.setId(1L);
            faculty.setName("name");
            faculty.setColour("colour");

            FacultyDtoOut expected = new FacultyDtoOut();
            expected.setId(1L);
            expected.setName("name");
            expected.setColour("colour");

            when(facultyRepository.save(any())).thenReturn(faculty);

            FacultyDtoOut actual = facultyService.create(facultyDtoIn);

            assertThat(actual)
                    .usingRecursiveComparison();
            verify(facultyRepository, new Times(1)).save(any());
        }

    }

