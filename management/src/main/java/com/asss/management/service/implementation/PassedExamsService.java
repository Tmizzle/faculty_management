package com.asss.management.service.implementation;

import com.asss.management.dao.*;
import com.asss.management.entity.*;
import com.asss.management.entity.Enums.Type_of_event;
import com.asss.management.service.dto.AssignedProfesorsDTO;
import com.asss.management.service.dto.PassedExamsDTO;
import com.asss.management.service.mapper.PassedExamsMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Key;
import java.util.List;
import java.util.Objects;

@Service
@Data
public class PassedExamsService {

    private final PassedExamsRepo passedExamsRepo;
    private final PassedExamsMapper passedExamsMapper;
    private final EmployeeRepo employeeRepo;
    private final EventsRepo eventsRepo;
    private final StudentRepo studentRepo;
    private final SubjectsRepo subjectsRepo;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // get all passed exams
    public List<PassedExamsDTO> getPassedExams(){
        List<PassedExams> passedExamsList = passedExamsRepo.findAll();
        List<PassedExamsDTO> passedExamsDTOList = passedExamsMapper.entitiesToDTOs(passedExamsList);
        return passedExamsDTOList;
    }

    // Retrieves a passed exam by id
    public PassedExamsDTO getPassedExamById(Integer id) {
        PassedExams passedExams = passedExamsRepo.findById(id).orElse(null);
        PassedExamsDTO passedExamsDTO = passedExamsMapper.entityToDTO(passedExams);
        return passedExamsDTO;
    }

    public List<PassedExamsDTO> getPassedExamsByProfesor(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        Integer userId = claims.get("id", Integer.class);
        List<PassedExams> passedExamsList = passedExamsRepo.passedExamsByProfesor(userId);
        List<PassedExamsDTO> passedExamsDTOList = passedExamsMapper.entitiesToDTOs(passedExamsList);
        return passedExamsDTOList;
    }

    @Transactional
    public void updatePassedExam(Integer id,
                              Integer grade){
        PassedExams passedExams = passedExamsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Specified passed exam has not been found."));

        if (grade != null && !Objects.equals(passedExams.getGrade(), grade)) {
            passedExams.setGrade(grade);
        }
    }

    public void addNewPassedExam(PassedExams passedExams, Integer studentID, Integer profesorID, Integer subjectID, Integer eventID) {
        Employee employee = employeeRepo.findById(profesorID).orElse(null);
        Events events = eventsRepo.findById(eventID).orElse(null);
        Student student = studentRepo.findById(studentID).orElse(null);
        Subjects subject = subjectsRepo.findById(subjectID).orElse(null);

        PassedExams passedExamCheck = passedExamsRepo.checkIfExamAlreadyPassed(subjectID, studentID);

        if(events.getType() != Type_of_event.EXAM_PERIOD && events.getType() != Type_of_event.EXTRA_EXAM_PERIOD){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are trying to add passed exam for a non Exam period event");
        }
        if(passedExamCheck != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student already passed that exam");
        }

        if(subject.getCourse() != student.getCourseOfStudies()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong input");
        }
        passedExams.setEvent(events);
        passedExams.setProfesor(employee);
        passedExams.setStudent(student);
        passedExams.setSubject(subject);

        passedExamsRepo.save(passedExams);
    }

    public void addNewPassedExamAsProfesor(PassedExams passedExams, Integer studentID, Integer subjectID, Integer eventID, String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        Integer userId = claims.get("id", Integer.class);

        Employee employee = employeeRepo.findById(userId).orElse(null);
        Events events = eventsRepo.findById(eventID).orElse(null);
        Student student = studentRepo.findById(studentID).orElse(null);
        Subjects subject = subjectsRepo.findById(subjectID).orElse(null);

        PassedExams passedExamCheck = passedExamsRepo.checkIfExamAlreadyPassed(subjectID, studentID);

        if(events.getType() != Type_of_event.EXAM_PERIOD && events.getType() != Type_of_event.EXTRA_EXAM_PERIOD){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are trying to add passed exam for a non Exam period event");
        }
        if(passedExamCheck != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student already passed that exam");
        }

        if(subject.getCourse() != student.getCourseOfStudies()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong input");
        }
        passedExams.setEvent(events);
        passedExams.setProfesor(employee);
        passedExams.setStudent(student);
        passedExams.setSubject(subject);

        passedExamsRepo.save(passedExams);
    }
}
