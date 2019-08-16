package com.lyl.game.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lyl.game.GameBodyFactory
import com.lyl.game.R
import com.lyl.game.enums.GameDirection
import com.lyl.game.enums.GameOperate
import com.lyl.game.interfaces.ISnakeCallback
import com.lyl.game.view.SteeringWheelView
import kotlinx.android.synthetic.main.activity_game.*

/**
 * create lyl on 2019-08-16
 *
 * 游戏界面
 */
class GameActivity : AppCompatActivity() {

    private var gameType: Int = 0

    private val toolbarTitle: String
        get() {
            var title = ""
            when (gameType) {
                GameBodyFactory.GAME_SNAKE -> title = "贪吃蛇"
                else -> {
                }
            }
            return title
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        initExtra(savedInstanceState ?: intent.extras)
        if (isFinishing) return

        initView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_GAME_TYPE, gameType)
    }

    override fun onDestroy() {
        super.onDestroy()
        gameView.destroy()
    }

    private fun initExtra(bundle: Bundle?) {
        if (bundle != null) {
            gameType = bundle.getInt(EXTRA_GAME_TYPE)
            return
        }
        finish()
    }

    private fun initView() {
        toolbar.title = toolbarTitle
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        gameView.setGameBody(GameBodyFactory.createGameBody(gameType, object : ISnakeCallback {

            override fun reDraw() {
                if (isFinishing) return
                gameView!!.invalidate()
            }

            override fun outBorderLine() {
                if (isFinishing) return
                AlertDialog.Builder(this@GameActivity).setTitle("Tip").setMessage("Game Over").setNegativeButton("ok", null).create().show()
            }

            override fun score(score: Int) {
                if (isFinishing) return
                tvScore!!.text = "your score: $score"
            }
        })!!)
        controlView.setDirectionListener(object : SteeringWheelView.SteeringWheelListener {
            override fun onStatusChanged(view: SteeringWheelView, angle: Int, power: Int, direction: GameDirection) {
                gameView!!.actionDirection(direction)
            }
        })

        controlView.setActionListener(View.OnClickListener { v ->
            when (v!!.id) {
                R.id.btnStart -> gameView!!.actionOperate(GameOperate.Start)
                R.id.btnSelect -> {
                }
                R.id.btnA -> gameView!!.actionOperate(GameOperate.A)
                R.id.btnB -> {
                }
                else -> {
                }
            }
        })
    }

    companion object {

        const val EXTRA_GAME_TYPE = "extra_game_type"

        fun start(activity: Activity, gameType: Int) {
            val intent = Intent(activity, GameActivity::class.java)
            intent.putExtra(EXTRA_GAME_TYPE, gameType)
            activity.startActivity(intent)
        }
    }
}
