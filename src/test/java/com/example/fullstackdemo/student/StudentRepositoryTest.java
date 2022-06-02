package com.example.fullstackdemo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldSelectExistsEmail() {
        //Given
        String email  = "some@gmail.com";
        Student student = new Student(
                "Jamila",
                email,
                Gender.FEMALE);
        underTest.save(student);
        
        //When
        Boolean expected = underTest.selectExistsEmail(email);

        //Then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfStudentEmailDoesNotExist() {
        String email  = "some@gmail.com";

        //When
        Boolean expected = underTest.selectExistsEmail(email);

        //Then
        assertThat(expected).isFalse();
    }
}