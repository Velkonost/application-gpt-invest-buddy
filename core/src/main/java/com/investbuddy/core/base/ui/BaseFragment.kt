package com.investbuddy.core.base.ui

import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.investbuddy.core.base.mvvm.DefaultViewCommand
import com.investbuddy.core.base.mvvm.ViewCommand

open class BaseFragment(@LayoutRes val layoutId: Int) : Fragment(layoutId) {

    fun showMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    /**
     * Обработка дефолтных вьюКоманд, вроде отображения сообщения или снекбара.
     * Вызывать, если не подразумевается особенное поведение.
     */
    fun handleDefaultViewCommand(viewCommand: ViewCommand) {
        when (viewCommand) {
            is DefaultViewCommand.ShowError -> {
                showMessage(viewCommand.msg)
            }

            is DefaultViewCommand.ShowMessage -> {
                showMessage(viewCommand.msg)
            }
        }
    }
}
