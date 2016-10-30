package zor07.develop.com.calcgame;


import java.util.Date;
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
     * Текущее число, отображаемое на экране;
     * Заработанные очки;
     * Текущий уровень;
     * Количество правильных ответов подряд;
     * Количество жизней;
     */
    private int number,
                score,
                level,
                rightAnswersInARow,
                lifes;

    /**
     * Время последнего ответа (Используется для вычисления количества заработанных очков)
     */
    private Date lastAnswerTime;

    /**
     * Закрытый конструктор класса
     */
    private GameEngine(){
    }

    /**
     * Геттер для счета игры
     * @return счет игры
     */
    public int getScore() {
        return score;
    }

    /**
     * Геттер для уровня игры
     * @return уровень игры
     */
    public int getLevel() {
        return level;
    }

    /**
     * Геттер для количества жизней
     * @return количество жизней
     */
    public  int getLifes() {
        return lifes;
    }

    /**
     * Функция для начала новой игры,
     * Переводит все переменные в изначальное состояние
     */
    public void newGame(){
        lastAnswerTime = new Date();
        lifes = 4;
        rightAnswersInARow = 0;
        score = 0;
    }

    /**
     * Функция проверяет продолжается ли еще игра
     * @return true, если количество жизней > 0, иначе false
     */
    public boolean isPlaying(){
        return lifes > 0;
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


    /**
     * Функция обновляет все переменные игры после каждого ответа игрока
     * @param rightAnswer - true, если последний ответ игрока был верным, иначе false
     * Функция вычисляет:
     *                    Количество очков набранных за ответ.
     *                    Общий счет за игру.
     *                    Количество жизней (-1 если rightAnswer = false)
     *                    И текущий уровень
     *                    Также запоминается время последнего ответа (участвует в расчете количества очков за ответ)
     */
    private void updateData(boolean rightAnswer){
        int points = (int) (10000 - (new Date().getTime() - lastAnswerTime.getTime())) * level / 100;
        if (rightAnswer){

            score += points;
            rightAnswersInARow ++;
        } else {
            score -= points;
            rightAnswersInARow = 0;
            lifes --;
        }
        level = (rightAnswersInARow / 5) + 1;
        lastAnswerTime = new Date();
    }

    /**
     * Функция для получения следующего числа
     * @return число или 0, если количество жизней = 0;
     */
    public int getNextNo() {
        if (lifes == 0) return 0;
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
