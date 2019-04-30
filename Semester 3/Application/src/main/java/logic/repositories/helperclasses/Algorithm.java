package logic.repositories.helperclasses;

import domain.Answer;
import domain.Question;

public class Algorithm {
    private Integer[] counters;


    public Algorithm(Integer[] counters) {
        this.counters = counters;
    }

    public Question algorithmButtons(Question question){
        int max = Math.max(Math.max(counters[0],counters[1]),Math.max(counters[2],counters[3]));
        if(max > 0){
            Answer correctAnswer = new Answer();
                for (Answer answer: question.getAnswers()) {
                    if(answer.isCorrect()){
                        correctAnswer = answer;
                    }
                }
                question.getAnswers().remove(correctAnswer);
                question.getAnswers().add(newIndex(max), correctAnswer);

            return question;
        }else{
            return question;
        }
    }

    private int newIndex(int max){
        int index = -1;
        if(max == counters[0] || max == counters[1]){
            index = 3;
        } else if(max == counters[2] || max == counters[3]){
            index = 0;
        }
        return index;
    }
}
