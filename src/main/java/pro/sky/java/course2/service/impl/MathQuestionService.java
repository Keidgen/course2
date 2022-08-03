package pro.sky.java.course2.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pro.sky.java.course2.exception.QuestionAlreadyExistException;
import pro.sky.java.course2.exception.QuestionNotFoundException;
import pro.sky.java.course2.model.Question;
import pro.sky.java.course2.repository.QuestionRepository;
import pro.sky.java.course2.service.QuestionService;

import java.util.*;

@Service
public class MathQuestionService implements QuestionService {
    private final Random random;
    private final QuestionRepository questionRepository;

    public MathQuestionService(@Qualifier("mathQuestionRepository") QuestionRepository questionRepository) {
        this.random = new Random();
        this.questionRepository = questionRepository;
    }

    @Override
    public Question add(String question, String answer) {
        return questionRepository.add(question, answer);
    }

    @Override
    public Question add(Question question) {
        return questionRepository.add(question);
    }

    @Override
    public Question remove(Question question) {
        return questionRepository.remove(question);
    }

    @Override
    public Collection<Question> getAll() {
        return questionRepository.getAll();
    }

    @Override
    public Question getRandomQuestion() {
        Collection<Question> questions = getAll();
        return new ArrayList<>(questions).get(random.nextInt(questions.size()));
    }
}
