package com.test.imageload.kotlin

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.test.imageload.CusZoomViewActivity
import com.test.imageload.R
import com.test.imageload.base.BaseActivity
import com.test.imageload.base.ItemClickListener
import com.test.imageload.utils.DataUtil
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 使用kotlin 中替换findViewById的方式
 */
class MainActivity : BaseActivity(), ItemClickListener {

    private var adapter: RecyclerViewAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        super.initData()
        initRecyclerView()
    }

    private fun initRecyclerView() {

        var linearLayoutManager = LinearLayoutManager(act)
        recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
        adapter?.itemClickListener = this

        adapter?.setData(DataUtil.getInstance().generateData())
    }



    /**
     * 条目点击事件
     */
    override fun onItemClick(pos: Int) {
        val posBean = adapter?.getPosBean(pos) ?: return

        var intent = when {
            pos < adapter?.itemCount?.div(4) ?: 0 -> Intent(act, ImageLoadLongPicActivity::class.java)
            pos < (adapter?.itemCount?.div(4)
                    ?: 0) * 2 -> Intent(act, CusZoomViewActivity::class.java)
            pos < (adapter?.itemCount?.div(4)
                    ?: 0) * 3 -> Intent(act, DetailActivity::class.java)
            else -> Intent(act, BrowPicActivity::class.java)
        }
        act?.startActivity(intent)

        ToastUtils.showShort("我是Item%d,,%s", pos, posBean.name)
    }

    override fun onResume() {
        super.onResume()

    }
}

