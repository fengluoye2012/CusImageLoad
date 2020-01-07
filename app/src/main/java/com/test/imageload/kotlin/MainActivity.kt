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
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 使用kotlin 中替换findViewById的方式
 */
class MainActivity : BaseActivity(), View.OnClickListener, ItemClickListener {

    //可变变量定义：var 关键字
    private val age: Int = 12

    //不可变变量定义：val 关键字，只能赋值一次的变量(类似Java中final修饰的变量)
    private val name: String = "风落叶"

    private var adapter: RecyclerViewAdapter? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        super.initData()

        textView.text = name
        tv_name.text = name

        //将int 类型转换为String
        tv_age.text = age.toString()
        initRecyclerView()

        textView.setOnClickListener(View.OnClickListener {
            ToastUtils.showShort(name)
        })

        tv_name.setOnClickListener(this)
        tv_age.setOnClickListener(this)

        var user: User = User()
        user.name = "fengluoue"

        var userInfoBean: UserInfoBean = UserInfoBean("风落叶", 12)
        LogUtils.i(user.name + "，，，" + userInfoBean.name)
    }


    private fun initRecyclerView() {

        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(act)
        recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
        adapter?.itemClickListener = this

        adapter?.setData(generateData())
    }

    private fun generateData(): MutableList<UserInfoBean> {
        var list: ArrayList<UserInfoBean> = ArrayList()
        for (i in 1..15) {
            var str = "风落叶$i"
            list.add(UserInfoBean(str))
        }
        return list
    }

    //when 表达式类型于 switch
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.textView -> {
                ToastUtils.showShort("textView")

            }
            R.id.tv_name -> {
                ToastUtils.showShort("tv_name")
            }

            R.id.tv_age -> {
                ToastUtils.showShort("tv_age")
            }
            else -> {
                ToastUtils.showShort("else")
            }
        }
    }

    /**
     * 条目点击事件
     */
    override fun onItemClick(pos: Int) {
        val posBean = adapter?.getPosBean(pos) ?: return

        var intent = when {
            pos < adapter?.itemCount?.div(3) ?: 0 -> Intent(act, ImageLoadLongPicActivity::class.java)
            pos < (adapter?.itemCount?.div(3)
                    ?: 0) * 2 -> Intent(act, CusZoomViewActivity::class.java)
            else -> Intent(act, BrowPicActivity::class.java)
        }
        act?.startActivity(intent)

        ToastUtils.showShort("我是Item%d,,%s", pos, posBean.name)
    }

    //参数是Int 类型的a,b相加 返回值为Int
    fun add(a: Int, b: Int): Int {
        return a + b
    }

    //无返回值 方法
    fun printStr(a: Int) {
        var str: String = "" + a
        LogUtils.i(str)
    }

    override fun onResume() {
        super.onResume()

    }
}

