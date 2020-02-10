package com.test.imageload.kotlin

import android.media.MediaPlayer
import android.view.SurfaceHolder
import com.blankj.utilcode.util.LogUtils
import com.danikula.videocache.HttpProxyCacheServer
import com.test.imageload.MyApplication
import com.test.imageload.R
import com.test.imageload.base.BaseActivity
import kotlinx.android.synthetic.main.activity_video_cache.*

class VideoCacheActivity : BaseActivity() {

    var mediaPlayer: MediaPlayer? = null
    var isInitFinish = false

    override fun getLayoutId(): Int {
        return R.layout.activity_video_cache
    }

    override fun initData() {
        super.initData()

        mediaPlayer = MediaPlayer()

        val surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                mediaPlayer?.setDisplay(holder)
                setPlayVideo()
            }

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }
        })
    }


    private fun setPlayVideo() {

        var videoUrl = "https://assets.yyuehd.com/Fhev_4GB6fmylVxKvT60clrbPOCI"
        val proxyUrl = getProxy()?.getProxyUrl(videoUrl)
        //http://127.0.0.1:38415/https%3A%2F%2Fassets.yyuehd.com%2FFhev_4GB6fmylVxKvT60clrbPOCI
        LogUtils.i("proxyUrl：$proxyUrl")

        mediaPlayer?.setDataSource(proxyUrl)
        mediaPlayer?.isLooping = true
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener { mp ->
            isInitFinish = true
            mp?.start()
            LogUtils.i("初始化成功")
        }
    }

    public fun getProxy(): HttpProxyCacheServer? {
        return MyApplication.app?.getProxyCache()
    }

    override fun onResume() {
        super.onResume()

        if (isInitFinish) {
            startPlay()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlay()
    }

    override fun onStop() {
        super.onStop()
        stopPlay()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            stopPlay()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun startPlay() {
        //空判断
        if (mediaPlayer?.isPlaying != true) {
            mediaPlayer?.start()
        }
    }


    private fun pausePlay() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    private fun stopPlay() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
        }
    }
}
