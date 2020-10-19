package com.example.notes.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T, S: BaseViewState<T>> : AppCompatActivity(){
    abstract val viewModel:BaseViewModel<T, S>
    abstract val layoutResource: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
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
        error.message?.let {
            showError(it)
        }
    }

    protected fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}