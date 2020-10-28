package com.example.notes.ui.splash

import androidx.lifecycle.ViewModelProvider
import com.example.notes.ui.base.BaseActivity
import com.example.notes.ui.main.MainActivity

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    override val viewModel by lazy { ViewModelProvider(this).get(SplashViewModel::class.java) }
    override val layoutResource = null

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}