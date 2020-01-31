package com.test.imageload.kotlin

import android.view.Gravity
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.test.imageload.R
import com.test.imageload.base.BaseActivity
import com.test.imageload.base.ItemClickListener
import com.test.imageload.utils.DataUtil
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.recyclerView
import kotlinx.android.synthetic.main.activity_main.*


class DetailActivity : BaseActivity() {


    private var adapter: RecyclerViewAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }


    override fun initData() {
        super.initData()

        initRecyclerView()
        changeWindowHeight((ScreenUtils.getScreenHeight() * 0.9).toInt())

    }

    private fun initRecyclerView() {

        var linearLayoutManager = LinearLayoutManager(act)
        recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter

        //匿名内部类
        adapter?.itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {

            }
        }

        adapter?.setData(DataUtil.get().generateData())
    }

    private fun changeWindowHeight(height: Int) {
        val p = window.attributes
        p.height = height
        p.width = ScreenUtils.getScreenWidth()
        p.gravity = Gravity.BOTTOM
        window.attributes = p
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun finish() {
        super.finish()

    }
}
