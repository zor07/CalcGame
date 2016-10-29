package zor07.develop.com.calcgame;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button[] buttons = new Button[9];
    private Button[] indicators = new Button[4];


    private TextView number, timer, level, score;
    private GameEngine game = GameEngine.getInstance();

    // создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        // добавляем пункты меню
        menu.add(0, 1, 0, "New Game");
        return super.onCreateOptionsMenu(menu);
    }

    // обработка нажатий меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            newGame();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        buttons[0] = (Button) findViewById(R.id.btn1);
        buttons[1] = (Button) findViewById(R.id.btn2);
        buttons[2] = (Button) findViewById(R.id.btn3);
        buttons[3] = (Button) findViewById(R.id.btn4);
        buttons[4] = (Button) findViewById(R.id.btn5);
        buttons[5] = (Button) findViewById(R.id.btn6);
        buttons[6] = (Button) findViewById(R.id.btn7);
        buttons[7] = (Button) findViewById(R.id.btn8);
        buttons[8] = (Button) findViewById(R.id.btn9);

        indicators[0] = (Button) findViewById(R.id.indicator1);
        indicators[1] = (Button) findViewById(R.id.indicator2);
        indicators[2] = (Button) findViewById(R.id.indicator3);
        indicators[3] = (Button) findViewById(R.id.indicator4);

        timer = (TextView) findViewById(R.id.timer);
        number = (TextView) findViewById(R.id.number);
        level = (TextView) findViewById(R.id.level);
        score = (TextView) findViewById(R.id.score);


        for (int i = 0; i < 9; i++) {
            buttons[i].setOnClickListener(this);
        }

        newGame();
        runTimer();
        game.runTimer();
    }

    @Override
    public void onClick(View v) {
        if (game.isPlaying()) {
            Button b = (Button) v;
            int answer = Integer.parseInt(b.getText().toString());
            game.checkAnswer(answer);
            number.setText(String.valueOf(game.getNextNo()));
            level.setText("Level: " + game.getLevel());
            score.setText("Score: " + game.getScore());
            updateIndicators();
        }
    }


    private void updateIndicators(){
        int indicatorID = game.getRightAnswersInARow() % 5;
        if (indicatorID == 0){
            for (int i = 0; i < 4; i++) {
                indicators[i].setBackgroundColor(Color.DKGRAY);
            }
        } else {
            indicators[indicatorID - 1].setBackgroundColor(Color.GREEN);
        }
    }

    private void runTimer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                timer.setText(game.getTime());
                if (!game.isPlaying()) gameOver();
                handler.postDelayed(this, 10);
            }
        });
        game.runTimer();
    }


    private void newGame(){
        game.newGame();
        number.setText(String.valueOf(game.getNextNo()));
        score.setText("Score: 0");
        level.setText("Level: 1");
        updateIndicators();
    }

    private void gameOver(){
        number.setText("Game Over");
    }
}
