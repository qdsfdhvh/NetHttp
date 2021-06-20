package com.seiko.okhttp.flow.vm

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.seiko.net.download.downloadFlow
import com.seiko.net.download.downloadRx
import com.seiko.okhttp.flow.http.DownloadNetHttp
import com.seiko.okhttp.flow.util.defaultDownloadDir
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class DownloadViewModel : BaseRxViewModel() {

  val body = mutableStateOf("")

  fun downloadRx(context: Context) {
    DownloadNetHttp
      .downloadRx(
        url = "https://dldir1.qq.com/weixin/android/weixin706android1460.apk",
        savePath = context.defaultDownloadDir(),
        isClearCache = true,
      )
      .map { "${it.downloadSizeFormat}/${it.totalSizeFormat}" }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ showBody(it) }, { Timber.w(it) })
      .addToDisposables()
  }

  fun downloadFlow(context: Context) {
    DownloadNetHttp
      .downloadFlow(
        url = "https://dldir1.qq.com/weixin/android/weixin706android1460.apk",
        savePath = context.defaultDownloadDir(),
        isClearCache = true,
      )
      .map { "${it.downloadSizeFormat}/${it.totalSizeFormat}" }
      .catch { Timber.w(it) }
      .onEach { showBody(it) }
      .launchIn(viewModelScope)
  }

  private fun showBody(body: String) {
    this.body.value = body
  }
}