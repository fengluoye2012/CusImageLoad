package com.test.imageload.kotlin

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.test.imageload.R
import com.test.imageload.base.ItemClickListener
import kotlinx.android.synthetic.main.layout_konlin.*

/**
 * 使用kotlin 中替换findViewById的方式
 */
class KotlinActivity : AppCompatActivity(), View.OnClickListener, ItemClickListener {


    //可变变量定义：var 关键字
    private val age: Int = 12

    //不可变变量定义：val 关键字，只能赋值一次的变量(类似Java中final修饰的变量)
    private val name: String = "风落叶"

    private var act: Activity? = null
    private var adapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_konlin)
        act = this

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
        user.name = "风落叶"

        var userInfoBean: UserInfoBean = UserInfoBean("风落叶", 12)
        LogUtils.i(user.name + "，，，" + userInfoBean.name)
    }

    private fun initRecyclerView() {

        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(act)
        recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
        adapter?.itemClickListener = this
        adapter?.setData(initData())
    }

    private fun initData(): MutableList<UserInfoBean> {
        var list: ArrayList<UserInfoBean> = ArrayList()
        for (i in 1..20) {
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
        ToastUtils.showShort("我是Item%d", pos)
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
