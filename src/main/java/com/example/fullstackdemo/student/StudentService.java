package com.example.fullstackdemo.student;

import com.example.fullstackdemo.student.exception.BadRequestException;
import com.example.fullstackdemo.student.exception.StudentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
        //check if email is taken and throw exception
        Boolean existsEmail = studentRepository.selectExistsEmail(student.getEmail());
        if(existsEmail){
            throw new BadRequestException("Email " + student.getEmail() + " already taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        //Check if the student exist
        boolean existsById = studentRepository.existsById(studentId);
        if (!existsById){
            throw new StudentNotFoundException("Student with id: " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);
    }
}
