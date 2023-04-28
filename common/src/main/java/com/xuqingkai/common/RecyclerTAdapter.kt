package com.xuqingkai.common
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

open class RecyclerTAdapter<T>() : RecyclerView.Adapter<RecyclerTAdapter<T>.MyViewHolder>() {

    //数据
    protected var mDatalist: ArrayList<T> = arrayListOf();
    //布局id
    protected var mLayoutId: Int? = null
    //绑定事件的lambda放发
    protected var addBindView: ((itemView: View, itemData: T, itemIndex: Int) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(mLayoutId!!, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDatalist.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        addBindView?.invoke(myViewHolder.itemView, mDatalist[i]!!, i)
    }
    fun resetData(arrayList: ArrayList<T>) {
        mDatalist = arrayList;
        notifyDataSetChanged()
    }

    fun appendData(arrayList: ArrayList<T>) {
        if (arrayList.size>0) {
            for(i in 0 until arrayList.size){
                mDatalist.add(arrayList[i]);
                notifyItemInserted(i);
            }
            notifyDataSetChanged()
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * 建造者，用来完成adapter的数据组合
     */
    open class Builder<B> {
        private var adapter: RecyclerTAdapter<B> = RecyclerTAdapter()

        /**
         * 设置数据
         */
        fun setData(lists: ArrayList<B>): Builder<B> {
            adapter.mDatalist = lists
            return this
        }

        /**
         * 设置布局id
         */
        open fun setLayoutId(layoutId: Int): Builder<B> {
            adapter.mLayoutId = layoutId
            return this
        }

        /**
         * 绑定View和数据
         */
        open fun addBindView(itemBind: ((itemView: View, itemData: B, itemIndex: Int) -> Unit)): Builder<B> {
            adapter.addBindView = itemBind
            return this
        }

        open fun create(): RecyclerTAdapter<B> {
            return adapter
        }
    }

}
