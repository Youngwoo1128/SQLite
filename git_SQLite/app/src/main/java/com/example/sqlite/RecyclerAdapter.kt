package com.example.sqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.text.SimpleDateFormat

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.Holder>(){

    var helper: SQLiteOpenHelper? = null
    var listData = mutableListOf<Memo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var textNo: TextView = itemView.findViewById(R.id.textNo)
        var textContent: TextView = itemView.findViewById(R.id.textContent)
        var textDatetime: TextView = itemView.findViewById(R.id.textDatetime)
        var buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)

        var mMemo: Memo? = null

        fun setMemo(memo: Memo){
            textNo.text = "${memo.no}"
            textContent.text = memo.content
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
            //날짜 포맷은 SimpleDateFormat으로 설정함
            textDatetime.text = "${sdf.format(memo.datetime)}"

            this.mMemo = memo
        }

        init {
            buttonDelete.setOnClickListener {
                helper?.deleteMemo(mMemo!!)
                listData.remove(mMemo)
                notifyDataSetChanged()
            }
        }
    }
}