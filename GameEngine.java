package zor07.develop.com.calcgame;


import android.os.Handler;

import java.util.Random;

/**
 * Класс отвечающий за логику игры
 * Синглтон
 */
public class GameEngine {
    /**
     * Инстанс
     */
    private static GameEngine instance;

    /**
     * Текущее число, отображаемое на экране
     */
    private static int number;

    /**
     * Количество миллисекунд на игру
     */
    private int milliseconds, millisecondsPrev;

    /**
     * Заработанные очки, текущий уровень
     */
    private int score, level;

    /**
     * Оставшееся время
     */
    private String time;

    /**
     * Индикатор того, идет ли в данный момент игра
     */
    private boolean playing;

    /**
     * Количество правильных ответов подряд
     */
    private int rightAnswersInARow;

    /**
     * Закрытый конструктор класса
     */
    private GameEngine(){
    }


    public boolean isPlaying() {
        return playing;
    }

    public int getScore() {
        return score;
    }

    public int getRightAnswersInARow() {
        return rightAnswersInARow;
    }

    public int getLevel() {
        return level;
    }

    public String getTime() {
        return time;
    }


    public void newGame(){
        resetTimer();
        playing = true;
        millisecondsPrev = milliseconds;
        rightAnswersInARow = 0;
        score = 0;
    }

    private void resetTimer(){
        milliseconds = 20000;
    }

    public void runTimer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int seconds = milliseconds / 1000;
                int minutes = seconds / 60;

                time = String.format("%d:%02d:%03d", minutes, seconds, milliseconds);
                if (milliseconds >= 10){
                    milliseconds -= 10;
                } else {
                    playing = false;
                    //number.setText("Game Over");
                }
                handler.postDelayed(this, 10);
            }
        });
    }

    /**
     * функция для получения инстанса игры
     * @return инстанс игры
     */
    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    /**
     * Функция проверяет является ли введенный пользователем ответ правильным
     * @param answer введенный пользователем ответ
     * @return true, если ответ верный, иначе false
     */
    public boolean checkAnswer(int answer){
        boolean rightAnswer = answer == totalSum(number);
        updateData(rightAnswer);
        return rightAnswer;
    }


    private void updateData(boolean rightAnswer){
        level = (rightAnswersInARow / 4) + 1;
        if (rightAnswer){
            int points = ((millisecondsPrev - milliseconds) / 100 ) * level;
            score += points;

            rightAnswersInARow ++;
            milliseconds += 2000 * level;
            millisecondsPrev = milliseconds;
        } else {
            rightAnswersInARow = 0;
            milliseconds -= 2000;
            if (milliseconds < 0) milliseconds = 0;
        }


    }

    /**
     * Функция для получения следующего числа для вычисления
     * @return число для вычисления или 0, если playing = false;
     */
    public int getNextNo() {
        if (!playing) return 0;
        Random r = new Random(System.currentTimeMillis());
        int complexity = calcComplexity();

        number = r.nextInt(complexity * 9) + complexity;
        return number;
    }

    /**
     * Функция вычисляет сложность (разрядность) числа,
     * зависит от количества правильных ответов подряд
     * По умолчанию разрядность числа = 4
     * Через каждые 5 правильных ответов подряд разрядность увеличивается на 1
     * @return
     */
    private int calcComplexity(){
        int complexityIndex = rightAnswersInARow / 5;
        int numberRange = 1000;

        for (int i = 0; i < complexityIndex; i++) {
            numberRange *= 10;
        }

        return numberRange;
    }

    /**
     * Функция вычисляет конечную сумму цифр числа.
     * Например число - 4567
     * Сумма цифр 4567: 4 + 5 + 6 + 7 = 22
     * Сумма цифр 22:   2 + 2 = 4
     * Конечная сумма цифр 4567 = 4
     * @param number текущее число
     * @return конечную сумму цифр числа
     */
    private int totalSum(int number){
        int sum = 0;
        while(number != 0){
            sum = sum + (number % 10);
            number/=10;
        }
        if (sum > 9)
            return totalSum(sum);
        else
            return sum;
    }
}
