package com.example.notes.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.data.errors.NoAuthException
import com.firebase.ui.auth.AuthUI

abstract class BaseActivity<T, S: BaseViewState<T>> : AppCompatActivity(){

    companion object{
        private const val REQUEST_CODE_SIGN_IN = 1105
    }

    abstract val viewModel:BaseViewModel<T, S>
    abstract val layoutResource: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutResource?.let { setContentView(it) }

        viewModel.getViewStateLiveData().observe(this, {value ->
            value ?: return@observe
            value.error?.let {
                renderError(it)
                return@observe
            }
            renderData(value.data)
        })
    }

    abstract fun renderData(data: T)

    protected fun renderError(error: Throwable) {
        when(error){
            is NoAuthException -> startLogin()
            else -> error.message?.let {
                showError(it)
            }
        }
    }

    protected fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun startLogin() {
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setLogo()
            .setTheme()
            .setAvailableProviders(providers)
            .build()

        startActivityForResult(intent, REQUEST_CODE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}