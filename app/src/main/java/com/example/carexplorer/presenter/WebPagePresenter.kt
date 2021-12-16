package com.example.carexplorer.presenter

import android.graphics.Bitmap
import com.example.carexplorer.ui.base.ErrorHandler
import com.example.carexplorer.view.WebPageView
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import moxy.InjectViewState
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.exitDefault
import java.io.*

@AutoFactory
@InjectViewState
class WebPagePresenter(
    @Provided override val errorHandler: ErrorHandler,
    private val router: Router
) : BasePresenter<WebPageView>(errorHandler) {

    override fun onBackPressed() {
        router.exitDefault()
    }

    fun loadUrl(url: String) {
        viewState.loadUrl(url)
    }

    fun readJavaScript(inputStream: InputStream): String = readRawResource(inputStream)

    fun getBitmapInputStream(bitmap: Bitmap, compressFormat: Bitmap.CompressFormat): InputStream =
        ByteArrayInputStream(
            ByteArrayOutputStream().apply {
                bitmap.compress(compressFormat, 80, this)
            }.toByteArray()
        )

    private fun readRawResource(inputStream: InputStream): String {
        val builder = StringBuilder()
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        try {
            while (true) {
                val line = bufferedReader.readLine() ?: break
                builder.append(line)
                builder.append("\n")
            }
        } catch (e: Exception) {
        }
        return builder.toString()
    }

}