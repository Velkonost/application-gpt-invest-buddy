package com.investbuddy.features.adviser.ui

import android.graphics.Paint
import android.net.Uri
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.util.Linkify
import android.text.util.Linkify.TransformFilter
import android.view.View
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.text.getSpans
import androidx.core.text.toSpannable
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.investbuddy.R
import com.investbuddy.common.room.chat.ChatDataDB
import com.investbuddy.core.extension.dpToPx
import com.investbuddy.databinding.ItemMessageBotBinding
import com.investbuddy.databinding.ItemMessageUserBinding
import com.investbuddy.features.adviser.data.network.Roles
import java.net.URI
import java.util.regex.Pattern


class AdviserAdapter(): EpoxyAdapter() {

    val messages = mutableListOf<ChatDataDB>()

    var appInstanceId = ""
    var gclid = ""

    fun getSize() = models.size

    fun createList(
        items: List<ChatDataDB>,
        appInstanceId: String,
        gclid: String
    ) {
        messages.addAll(items)
        this.appInstanceId = appInstanceId
        this.gclid = gclid

        removeAllModels()

        items.map {
            if (it.role == Roles.USER.title) {
                addModel(UserMessageModel(it.message))
            } else if (it.role == Roles.ASSISTANT.title) {
                addModel(BotMessageModel(it.message, appInstanceId, gclid))
            }
        }
    }

    fun addMessage(item: ChatDataDB) {
        messages.add(item)

        val newModel = when (item.role) {
            Roles.USER.title -> {
                UserMessageModel(item.message)
            }
            Roles.ASSISTANT.title -> {
                BotMessageModel(item.message, appInstanceId, gclid)
            }
            else -> null
        }

        newModel?.let { addModel(it) }
    }

    fun switchWritingBot(isWriting: Boolean) {
        val writingModel = models.firstOrNull { it ->
            it is BotWritingModel
        }

        if (writingModel == null && isWriting) {
            addModel(BotWritingModel())
        }
        else if (writingModel != null && !isWriting) {
            removeModel(writingModel)
        }
    }

    inner class BotWritingModel(): EpoxyModel<View>() {
        private var root: View? = null

        override fun bind(view: View) {
            super.bind(view)
            root = view

            with(view) {
            }
        }

        override fun getDefaultLayout(): Int = R.layout.item_message_bot_writing
    }

    inner class BotMessageModel(
        private val message: String,
        private val appInstanceId: String,
        private val gclid: String
    ): EpoxyModel<View>() {

        private var root: View? = null

        override fun bind(view: View) {
            super.bind(view)
            root = view

            with(view) {
                val binding = ItemMessageBotBinding.bind(view)
                binding.text.text = message

                binding.text.setTextWithLinks(message, appInstanceId, gclid)
            }
        }

        override fun getDefaultLayout(): Int = R.layout.item_message_bot

    }

    private fun String.toSpannableWithHtmlAndAutoLink(
        appInstanceId: String, gclid: String
    ) =
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).let { text ->
            text.toSpannable().apply {
                val URL_PATTERN: Pattern = Pattern.compile("[a-z]+://[^ \\n]*")

                val tf = TransformFilter { match, url ->
                    var newUrl = url

                    if (newUrl.contains("[") && newUrl.contains("]")) {
                        newUrl = newUrl.removeRange(newUrl.indexOf("["), newUrl.indexOf("]") + 1)
                    }
                    newUrl = newUrl.replace("[", "").replace("]", "")

                    if (newUrl.contains("(") && newUrl.contains(")")) {
                        newUrl = newUrl.removeRange(newUrl.indexOf("("), newUrl.indexOf(")") + 1)
                    }

                    newUrl = newUrl.replace("(", "").replace(")", "")

                    if (newUrl.last() == '.') {
                        newUrl = newUrl.removeSuffix(".")
                    }

                    if (newUrl.last() == '/') {
                        newUrl = newUrl.removeSuffix("/")
                    }

                    try {
                        val builder = Uri.Builder()
                        val uri = newUrl.toUri()

                        builder
                            .scheme(uri.scheme)
                            .authority(uri.authority)
                            .path(uri.path)
                            .appendQueryParameter("app_instance_id", appInstanceId)
                            .appendQueryParameter("referrer_gp", gclid)
                        val myUrl = builder.build().toString()
                        myUrl
                    } catch (e: Exception) {
                        "$newUrl?app_instance_id=$appInstanceId&referrer_gp=$gclid"
                    }
                }
                Linkify.addLinks(this, URL_PATTERN, null, null, tf)
                text.getSpans<URLSpan>().forEach {
                    setSpan(it, text.getSpanStart(it), text.getSpanEnd(it), text.getSpanFlags(it))


                }
            }
        }


    fun TextView.setTextWithLinks(
        message: String?,
        appInstanceId: String,
        gclid: String
    ) {
        text = message?.toSpannableWithHtmlAndAutoLink(appInstanceId, gclid)
        movementMethod = LinkMovementMethod.getInstance()
    }

    inner class UserMessageModel(private val message: String): EpoxyModel<View>() {

        private var root: View? = null

        override fun bind(view: View) {
            super.bind(view)
            root = view

            with(view) {
                val binding = ItemMessageUserBinding.bind(view)

                val textViewContentMaxWidth = context.dpToPx(270f).toInt()
                val sections = message.split(" ").toTypedArray();
                val formattedMessage = replaceBreakWordByNewLines(sections, binding.text.paint, textViewContentMaxWidth)

                binding.text.text = formattedMessage
            }
        }

        override fun getDefaultLayout(): Int = R.layout.item_message_user
    }

    fun replaceBreakWordByNewLines(_texts: Array<String>, _paint: Paint, maxWidth: Int): String {
        var formattedText = ""
        var workingText = ""

        for (section in _texts) {
            val newPart = (if (workingText.isNotEmpty()) " " else "") + section
            workingText += newPart
            val width = _paint.measureText(workingText, 0, workingText.length).toInt()
            if (width > maxWidth) {
                formattedText += (if (formattedText.isNotEmpty()) "\n" else "") +
                        workingText.substring(0, workingText.length - newPart.length)
                workingText = section
            }
        }
        if (workingText.isNotEmpty()) formattedText += (if (formattedText.isNotEmpty()) "\n" else "") + workingText
        return formattedText
    }
}