package pro.sky.java.course2.service;

import pro.sky.java.course2.model.Question;

import java.util.Collection;

public interface ExaminerService {
    Collection<Question> getQuestions(int amount);
}
