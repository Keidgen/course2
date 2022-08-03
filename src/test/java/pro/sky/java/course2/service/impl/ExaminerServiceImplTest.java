package pro.sky.java.course2.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import pro.sky.java.course2.exception.NotEnoughQuestionsException;
import pro.sky.java.course2.model.Question;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExaminerServiceImplTest {

    @Mock
    private JavaQuestionService javaQuestionService;

    @Mock
    private MathQuestionService mathQuestionService;

    @InjectMocks
    private ExaminerServiceImpl examinerService;

    private final Set<Question> javaQuestions = new HashSet<>();
    private final Set<Question> mathQuestions = new HashSet<>();

    @BeforeEach
    public void beforeEach() {
        javaQuestions.clear();
        mathQuestions.clear();
        javaQuestions.addAll(
                Stream.of(new Question("Вопрос по Java - 1", "Ответ по Java - 1"),
                        new Question("Вопрос по Java - 2", "Ответ по Java - 2"),
                        new Question("Вопрос по Java - 3", "Ответ по Java - 3")
                ).collect(Collectors.toSet())
        );
        mathQuestions.addAll(
                Stream.of(new Question("Вопрос по Math - 1", "Ответ по Math - 1"),
                        new Question("Вопрос по Math - 2", "Ответ по Math - 2")
                ).collect(Collectors.toSet())
        );
        when(javaQuestionService.getAll()).thenReturn(javaQuestions);
        when(mathQuestionService.getAll()).thenReturn(mathQuestions);
    }

    @Test
    public void getQuestionsNegativeTest() {
        assertThatExceptionOfType(NotEnoughQuestionsException.class)
                .isThrownBy(() -> examinerService.getQuestions(-1));
        assertThatExceptionOfType(NotEnoughQuestionsException.class)
                .isThrownBy(() -> examinerService.getQuestions(javaQuestions.size() + mathQuestions.size() + 1));
    }

    @ParameterizedTest
    @MethodSource("params")
    public void getQuestionsPositiveTest(int amount) {
        Random random = new Random();
        ReflectionTestUtils.setField(javaQuestionService, "random", random);
        ReflectionTestUtils.setField(mathQuestionService, "random", random);
        lenient().when(javaQuestionService.getRandomQuestion()).thenCallRealMethod();
        lenient().when(mathQuestionService.getRandomQuestion()).thenCallRealMethod();
        assertThat(examinerService.getQuestions(amount))
                .hasSize(amount)
                .containsAnyElementsOf(Stream.concat(javaQuestions.stream(), mathQuestions.stream()).collect(Collectors.toSet()));
    }

    public static Stream<Arguments> params() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3),
                Arguments.of(4),
                Arguments.of(5)

        );
    }


}
