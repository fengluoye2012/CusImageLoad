package com.test.imageload.kotlin

import android.graphics.*
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.test.imageload.R
import com.test.imageload.base.BaseActivity
import kotlinx.android.synthetic.main.long_pic_activity.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException


class ImageLoadLongPicActivity : BaseActivity() {

    var mRect: Rect = Rect()

    override fun getLayoutId(): Int {
        return R.layout.long_pic_activity
    }


    override fun initData() {
        super.initData()

        var url: String = "http://img4.imgtn.bdimg.com/it/u=2852083094,372235004&fm=26&gp=0.jpg"

        act?.let {
            Glide.with(it)
                    //匿名内部类
                    .addDefaultRequestListener(object : RequestListener<Any> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Any>?, isFirstResource: Boolean): Boolean {
                            LogUtils.i("")
                            return false
                        }

                        override fun onResourceReady(resource: Any?, model: Any?, target: Target<Any>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            if (resource is Bitmap) {
                                setBitmapToImg(resource)
                            }

                            return true
                        }
                    })
                    .asBitmap()
                    .load(url)
        }
    }

    var paint: Paint = Paint()
    private fun setBitmapToImg(resource: Bitmap) {
        try {
            val baos = ByteArrayOutputStream()
            resource.compress(Bitmap.CompressFormat.PNG, 100, baos)

            val isBm = ByteArrayInputStream(baos.toByteArray())

            //BitmapRegionDecoder newInstance(InputStream is, boolean isShareable)
            //用于创建BitmapRegionDecoder，isBm表示输入流，只有jpeg和png图片才支持这种方式，
            // isShareable如果为true，那BitmapRegionDecoder会对输入流保持一个表面的引用，
            // 如果为false，那么它将会创建一个输入流的复制，并且一直使用它。即使为true，程序也有可能会创建一个输入流的深度复制。
            // 如果图片是逐步解码的，那么为true会降低图片的解码速度。如果路径下的图片不是支持的格式，那就会抛出异常
            val decoder = BitmapRegionDecoder.newInstance(isBm, true)

            val imgWidth = decoder.width
            val imgHeight = decoder.height

            val opts = BitmapFactory.Options()

            //计算图片要被切分成几个整块，
            // 如果sum=0 说明图片的长度不足3000px，不进行切分 直接添加
            // 如果sum>0 先添加整图，再添加多余的部分，否则多余的部分不足3000时底部会有空白
            val sum = imgHeight / 3000

            val redundant = imgHeight % 3000

            val bitmapList: ArrayList<Bitmap> = ArrayList()

            //说明图片的长度 < 3000
            if (sum == 0) {
                //直接加载
                bitmapList.add(resource)
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

            val bigBitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap)


            var iHeight: Float = 0F

            //将之前的bitmap取出来拼接成一个bitmap
            for (i in bitmapList.indices) {
                var bmp: Bitmap = bitmapList.get(i)
                bigCanvas.drawBitmap(bmp, 0F, iHeight, paint)
                iHeight += bmp.height

                bmp.recycle()
            }

            imageView.setImageBitmap(bigBitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}

