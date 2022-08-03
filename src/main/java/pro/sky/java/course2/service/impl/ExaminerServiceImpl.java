package pro.sky.java.course2.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pro.sky.java.course2.exception.NotEnoughQuestionsException;
import pro.sky.java.course2.model.Question;
import pro.sky.java.course2.service.ExaminerService;
import pro.sky.java.course2.service.QuestionService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExaminerServiceImpl implements ExaminerService {

    private final JavaQuestionService javaQuestionService;
    private final MathQuestionService mathQuestionService;
    private final Random random;

    public ExaminerServiceImpl(JavaQuestionService javaQuestionService,
                               MathQuestionService mathQuestionService) {
        this.javaQuestionService = javaQuestionService;
        this.mathQuestionService = mathQuestionService;
        this.random = new Random();
    }

    @Override
    public Collection<Question> getQuestions(int amount) {
        List<QuestionService> questionServices = List.of(javaQuestionService, mathQuestionService);
        if (amount <= 0 || amount > questionServices.stream().map(QuestionService::getAll).mapToInt(Collection::size).sum()) {
            throw new NotEnoughQuestionsException();
        }
        Set<Question> result = new HashSet<>(amount);
        while (result.size() < amount) {
            int indexOfService = random.nextInt(questionServices.size());
            result.add(questionServices.get(indexOfService).getRandomQuestion());
        }
        return result;
    }
}
