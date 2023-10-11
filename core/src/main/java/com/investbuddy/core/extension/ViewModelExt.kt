package com.investbuddy.core.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.investbuddy.core.base.mvvm.CommandsLiveData

fun <T> MutableLiveData<T>.onNext(next: T) {
    value = next
}

inline fun <reified T : Any> LiveData<T>.requireValue(): T {
    return requireNotNull(value)
}

inline fun <reified T : Any> MutableLiveData<T>.update(
    update: (T) -> T
) {
    value = update.invoke(requireValue())
}

inline fun <reified T : Any, reified L : LiveData<T>> Fragment.observe(
    liveData: L,
    noinline block: (T) -> Unit
) {
    liveData.observe(this.viewLifecycleOwner, Observer { it?.let { block.invoke(it) } })
}

inline fun <reified T : Any, reified L : LiveData<T>> LifecycleOwner.observeInDialog(
    liveData: L,
    noinline block: (T) -> Unit
) {
    liveData.observe(this, Observer { it?.let { block.invoke(it) } })
}


inline fun <reified T : Any, reified L : CommandsLiveData<T>> LifecycleOwner.observeCommands(
    liveData: L,
    noinline block: (T) -> Unit
) {
    liveData.observe(this, Observer { commands ->
        if (commands == null) {
            return@Observer
        }
        var command: T?
        do {
            command = commands.poll()
            if (command != null) {
                block.invoke(command)
            }
        } while (command != null)
    })
}
