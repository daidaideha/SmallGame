package com.lyl.game;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lyl.game.enums.GameDirection;
import com.lyl.game.enums.GameOperate;
import com.lyl.game.impl.SnakeImpl;
import com.lyl.game.interfaces.ISnakeCallback;
import com.lyl.game.view.ControlView;
import com.lyl.game.view.GameView;
import com.lyl.game.view.SteeringWheelView;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private ControlView controlView;
    private TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        controlView = findViewById(R.id.controlView);
        tvScore = findViewById(R.id.tvScore);

        gameView.setGameBody(new SnakeImpl(new ISnakeCallback() {

            @Override
            public void reDraw() {
                gameView.invalidate();
            }

            @Override
            public void outBorderLine() {
                new AlertDialog.Builder(MainActivity.this).setTitle("Tip").setMessage("Game Over").setNegativeButton("ok", null).create().show();
            }

            @Override
            public void score(int score) {
                tvScore.setText("your score: " + score);
            }
        }));
        controlView.setDirectionListener(new SteeringWheelView.SteeringWheelListener() {
            @Override
            public void onStatusChanged(SteeringWheelView view, int angle, int power, GameDirection direction) {
                gameView.actionDirection(direction);
            }
        });

        controlView.setActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnStart:
                        gameView.actionOperate(GameOperate.Start);
                        break;
                    case R.id.btnSelect:
                        break;
                    case R.id.btnA:
                        gameView.actionOperate(GameOperate.A);
                        break;
                    case R.id.btnB:
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
