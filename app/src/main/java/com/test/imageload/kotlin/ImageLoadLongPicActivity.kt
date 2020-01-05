package com.test.imageload.kotlin

import android.graphics.*
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.test.imageload.R
import com.test.imageload.base.BaseActivity
import kotlinx.android.synthetic.main.long_pic_activity.*


/**
 * ImageView 加载最大长度为4049
 * 加载超长图：使用BitmapRegionDecoder 分片加载
 * 如何避免OOM?  因为目前直接通过Glide 将图片转换成Bitmap,虽然可以通过分片加载，能够正常加载，但是占用内存大，容易造成OOM，如何解决呢
 *
 * 可以考虑先将图片资源缓存下来，获取到图片的宽高，然后在分块加载？
 *
 * https://blog.csdn.net/lmj623565791/article/details/49300989
 */
class ImageLoadLongPicActivity : BaseActivity() {
    var mRect: Rect = Rect()
    var bigBitmap: Bitmap? = null
    var paint: Paint = Paint()

    override fun getLayoutId(): Int {
        return R.layout.long_pic_activity
    }

    override fun initData() {
        super.initData()
        setBitmapToImg()
    }

    private fun setBitmapToImg() {

        try {
            val isBm = resources.assets.open("timg.jpeg")

            //BitmapRegionDecoder newInstance(InputStream is, boolean isShareable)
            //用于创建BitmapRegionDecoder，isBm表示输入流，只有jpeg和png图片才支持这种方式，
            // isShareable如果为true，那BitmapRegionDecoder会对输入流保持一个表面的引用，
            // 如果为false，那么它将会创建一个输入流的复制，并且一直使用它。即使为true，程序也有可能会创建一个输入流的深度复制。
            // 如果图片是逐步解码的，那么为true会降低图片的解码速度。如果路径下的图片不是支持的格式，那就会抛出异常
            val decoder = BitmapRegionDecoder.newInstance(isBm, false)

            val imgWidth = decoder.width
            val imgHeight = decoder.height
            LogUtils.i("width：：$imgWidth,,height：：$imgHeight")

            val opts = BitmapFactory.Options()
            opts.inJustDecodeBounds = true

            //计算图片要被切分成几个整块，
            // 如果sum=0 说明图片的长度不足3000px，不进行切分 直接添加
            // 如果sum>0 先添加整图，再添加多余的部分，否则多余的部分不足3000时底部会有空白
            val sum = imgHeight / 3000

            val redundant = imgHeight % 3000

            LogUtils.i("sum：：$sum,,redundant::$redundant")
            val bitmapList: ArrayList<Bitmap> = ArrayList()

            //说明图片的长度 < 3000
            if (sum == 0) {
                mRect.set(0, 0, imgWidth, imgHeight)
                val bm = decoder.decodeRegion(mRect, opts)
                //直接加载
                bitmapList.add(bm)
            } else {
                //说明需要切分图片
                for (i in 0 until sum) {
                    //需要注意：mRect.set(left, top, right, bottom)的第四个参数，
                    //也就是图片的高不能大于这里的4096
                    mRect.set(0, i * 3000, imgWidth, (i + 1) * 3000)
                    val bm = decoder.decodeRegion(mRect, opts)
                    bitmapList.add(bm)
                }

                //将多余的不足3000的部分作为尾部拼接
                if (redundant > 0) {
                    mRect.set(0, sum * 3000, imgWidth, imgHeight)
                    val bm = decoder.decodeRegion(mRect, opts)
                    bitmapList.add(bm)
                }
            }

            bigBitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap as Bitmap)

            var iHeight: Float = 0F

            //将之前的bitmap取出来拼接成一个bitmap
            for (i in bitmapList.indices) {
                var bmp: Bitmap = bitmapList.get(i)
                bigCanvas.drawBitmap(bmp, 0F, iHeight, paint)
                iHeight += bmp.height

                bmp.recycle()
            }

            imageView.setImageBitmap(bigBitmap)
        } catch (e: Exception) {
            LogUtils.e(Log.getStackTraceString(e))
        }
    }


    private fun glideTest() {
        var longPicUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578050814588&di=c8af6aec4ad1ce70946f81f32b9f9c48&imgtype=0&src=http%3A%2F%2Fa.vpimg3.com%2Fupload%2Fmerchandise%2Fpdc%2F738%2F028%2F107318355143028738%2F0%2F20689190-6.jpg"
        var longPicUrl2 = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2279103746,902502804&fm=15&gp=0.jpg"
        //790*6222
        var longPicUrl3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578050993921&di=1d98d9d77a2c864a14b903f4c0ba31aa&imgtype=0&src=http%3A%2F%2Fimg001.hc360.cn%2Fhb%2FMTQ2MTAwMzExNzE4Ni0xMzU0NjI2MTgw.jpg"
        //750*6787
        var longPicUrl4 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578051292452&di=37534d14ae4bde20308d1ab18ede9141&imgtype=0&src=http%3A%2F%2Fsp.vipshop.com%2Fupload%2Fmerchandise%2F257225%2FHONO-A1314391-6.jpg"

//        Glide.with(act as AppCompatActivity).load(url).into(imageView)

//        act?.let { Glide.with(it).load(url).into(imageView) }

        act?.let {
            Glide.with(it)
                    //匿名内部类
                    .addDefaultRequestListener(object : RequestListener<Any> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Any>?, isFirstResource: Boolean): Boolean {
                            LogUtils.i("onLoadFailed")
                            return true
                        }

                        override fun onResourceReady(resource: Any?, model: Any?, target: Target<Any>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            LogUtils.i("onResourceReady")
                            if (resource is Bitmap) {
                            }
                            return true
                        }
                    })
                    .asBitmap()
                    .fitCenter()
                    .load(longPicUrl4)
                    .into(imageView)
        }
    }

}

