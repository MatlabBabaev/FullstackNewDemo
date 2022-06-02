package com.example.fullstackdemo.student;

import com.example.fullstackdemo.student.exception.BadRequestException;
import com.example.fullstackdemo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;

    @Captor
    private ArgumentCaptor<Student> studentArgumentCaptor;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void itShouldGetAllStudents() {
        //Given
        underTest.getAllStudents();
        //When

        //Then
        verify(studentRepository).findAll();
    }

    @Test
    void itShouldAddStudent() {
        //Given
        String email  = "some@gmail.com";
        Student student = new Student(
                "Jamila",
                email,
                Gender.FEMALE);

        //When
        underTest.addStudent(student);

        //Then
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student studentArgumentCaptorValue = studentArgumentCaptor.getValue();
        assertThat(studentArgumentCaptorValue).isEqualTo(student);
    }

    @Test
    void itShouldNotAddStudentWhenEmailIsTaken() {
        //Given
        String email  = "some@gmail.com";
        Student student = new Student(
                "Jamila",
                email,
                Gender.FEMALE);
        given(studentRepository.selectExistsEmail(email)).willReturn(true);

        //When
        assertThatThrownBy(()->underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " already taken");

        //Then
        verify(studentRepository, never()).save(any());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void itShouldDeleteStudent() {

        //Given
        long id = 22L;

        //given(studentRepository.existsById(id)).willReturn(true);
        given(studentRepository.existsById(anyLong())).willReturn(true);

        //When
        underTest.deleteStudent(id);

        //Then
        then(studentRepository).should().deleteById(id);
    }

//    @Test
//    void canDeleteStudent() {
//        // given
//        long id = 10;
//        given(studentRepository.existsById(id))
//                .willReturn(true);
//        // when
//        underTest.deleteStudent(id);
//
//        // then
//        verify(studentRepository).deleteById(id);
//    }


    @Test
    void itShouldThrowExceptionWhenIdIsNotFoundWhileDeleting() {
        //Given
        long id = 22L;

        //given(studentRepository.existsById(id)).willReturn(true);
        given(studentRepository.existsById(anyLong())).willReturn(false);

        //When
        assertThatThrownBy(()-> underTest.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id: " + id + " does not exist");

        //Then
        //verifyNoMoreInteractions(studentRepository);
        verify(studentRepository, never()).deleteById(any());
    }
}