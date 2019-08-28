package com.lyl.snake.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.lyl.base.enums.GameDirection
import com.lyl.base.enums.GameOperate
import com.lyl.base.interfaces.ISnakeCallback
import com.lyl.base.view.SteeringWheelView
import com.lyl.snake.R
import com.lyl.snake.impl.SnakeImpl
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Exception

/**
 * create lyl on 2019-08-28
 *
 */
@Route(path = "/snake/main")
class SnakeGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_game)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        gameView.destroy()
    }

    private fun initView() {
        toolbar.title = "贪吃蛇"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        gameView.setGameBody(SnakeImpl(object : ISnakeCallback {

            override fun onPause() {
                if (isFinishing) return
                AlertDialog.Builder(this@SnakeGameActivity).setTitle("提示").setMessage("游戏暂停").setCancelable(false)
                        .setNegativeButton("继续") { _, _ -> gameView!!.actionOperate(GameOperate.Select) }
                        .setPositiveButton("退出") { _, _ -> finish() }.create().show()
            }

            override fun reDraw() {
                if (isFinishing) return
                gameView!!.invalidate()
            }

            override fun outBorderLine() {
                if (isFinishing) return
                AlertDialog.Builder(this@SnakeGameActivity).setTitle("提示").setMessage("游戏结束").setNegativeButton("好的", null).create().show()
            }

            override fun score(score: Int) {
                if (isFinishing) return
                var level = score / 2
                if (level >= 25) level = 25
                tvScore!!.text = "你已获得: ${score}分, 速度等级${level}"
            }
        }))
        controlView.setDirectionListener(object : SteeringWheelView.SteeringWheelListener {
            override fun onStatusChanged(view: SteeringWheelView, angle: Int, power: Int, direction: GameDirection) {
                gameView!!.actionDirection(direction)
            }
        })

        controlView.setActionListener(View.OnClickListener { v ->
            when (v!!.id) {
                R.id.btnStart -> gameView!!.actionOperate(GameOperate.Start)
                R.id.btnSelect -> gameView!!.actionOperate(GameOperate.Select)
                R.id.btnA -> gameView!!.actionOperate(GameOperate.A)
                R.id.btnB -> {
                }
                else -> {
                }
            }
        })
    }
}
