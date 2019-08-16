package com.lyl.game.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lyl.game.GameBodyFactory;
import com.lyl.game.R;
import com.lyl.game.enums.GameDirection;
import com.lyl.game.enums.GameOperate;
import com.lyl.game.interfaces.ISnakeCallback;
import com.lyl.game.view.ControlView;
import com.lyl.game.view.GameView;
import com.lyl.game.view.SteeringWheelView;

/**
 * create lyl on 2019-08-16
 * </p>
 * 游戏界面
 */
public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_TYPE = "extra_game_type";

    private GameView gameView;
    private ControlView controlView;
    private TextView tvScore;
    private Toolbar toolbar;
    private int gameType;

    public static void start(Activity activity, int gameType) {
        Intent intent = new Intent(activity, GameActivity.class);
        intent.putExtra(EXTRA_GAME_TYPE, gameType);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initExtra(savedInstanceState == null ? getIntent().getExtras() : savedInstanceState);
        if (isFinishing()) return;

        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_GAME_TYPE, gameType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameView.destroy();
    }

    private void initExtra(Bundle bundle) {
        if (bundle != null) {
            gameType = bundle.getInt(EXTRA_GAME_TYPE);
            return;
        }
        finish();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        gameView = findViewById(R.id.gameView);
        controlView = findViewById(R.id.controlView);
        tvScore = findViewById(R.id.tvScore);

        toolbar.setTitle(getToolbarTitle());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        gameView.setGameBody(GameBodyFactory.createGameBody(gameType, new ISnakeCallback() {

            @Override
            public void reDraw() {
                if (isFinishing()) return;
                gameView.invalidate();
            }

            @Override
            public void outBorderLine() {
                if (isFinishing()) return;
                new AlertDialog.Builder(GameActivity.this).setTitle("Tip").setMessage("Game Over").setNegativeButton("ok", null).create().show();
            }

            @Override
            public void score(int score) {
                if (isFinishing()) return;
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

    private String getToolbarTitle() {
        String title = "";
        switch (gameType) {
            case GameBodyFactory.GAME_SNAKE:
                title = "贪吃蛇";
                break;
            default:
                break;
        }
        return title;
    }
}
