package com.muslimtrivia.Trivia.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<Questions> addQuestion(@RequestBody Questions question) {
        Questions savedQuestion = questionService.addQuestion(question);
        return ResponseEntity.ok(savedQuestion);
    }

    @GetMapping
    public ResponseEntity<List<Questions>> getAllQuestions() {
        List<Questions> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/questions/{difficulty}/{numberOfQuestions}")
    public ResponseEntity<List<Questions>> getQuestionsByDifficulty(
            @PathVariable String difficulty,
            @PathVariable int numberOfQuestions
    ) {
        DifficultyLevel difficultyLevel = DifficultyLevel.valueOf(difficulty.toUpperCase());
        List<Questions> questions = questionService.getQuestionsByDifficulty(difficultyLevel, numberOfQuestions);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Questions> updateQuestion(@PathVariable Integer id, @RequestBody Questions question) {
        question.setId(id); // set the id to the question to update
        questionService.updateQuestion(question);
        return ResponseEntity.ok(question);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok().build();
    }


}
