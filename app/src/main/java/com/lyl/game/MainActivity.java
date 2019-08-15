package com.lyl.game;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lyl.game.enums.GameDirection;
import com.lyl.game.impl.SnakeImpl;
import com.lyl.game.interfaces.ISnakeCallback;
import com.lyl.game.view.GameView;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        tvScore = findViewById(R.id.tvScore);

        gameView.setGameBody(new SnakeImpl(new ISnakeCallback() {

            @Override
            public void reDraw() {
                gameView.invalidate();
            }

            @Override
            public void outBorderLine() {
                Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void score(int score) {
                tvScore.setText("your score: " + score);
            }
        }));

        findViewById(R.id.btnLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.actionDirection(GameDirection.Left);
            }
        });

        findViewById(R.id.btnUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.actionDirection(GameDirection.Up);
            }
        });

        findViewById(R.id.btnRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.actionDirection(GameDirection.Right);
            }
        });

        findViewById(R.id.btnDown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.actionDirection(GameDirection.Down);
            }
        });

        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.startGame();
            }
        });
    }
}
