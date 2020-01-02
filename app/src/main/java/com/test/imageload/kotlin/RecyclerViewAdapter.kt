package com.test.imageload.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.imageload.R
import com.test.imageload.base.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_recycler.view.*

class RecyclerViewAdapter : BaseRecyclerViewAdapter<UserInfoBean>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val userInfoBean = list?.get(position)
        var viewHolder: Holder? = holder as?Holder
        viewHolder?.textView?.text = userInfoBean?.name ?: ""
    }

    class Holder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView? = null
        var view: View? = null

        init {
            this.view = view
            textView = view.textView
        }
    }
}