package com.lyl.game

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lyl.game.ui.GameActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "小游戏集合"
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp)
        toolbar.setNavigationOnClickListener { drawerLayout!!.openDrawer(Gravity.LEFT) }

        findViewById<View>(R.id.btnSnake).setOnClickListener { GameActivity.start(this@MainActivity, GameBodyFactory.GAME_SNAKE) }
    }
}
