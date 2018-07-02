package com.example.chenzexuan.myapplication.listview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hugo.weaving.DebugLog;

public abstract class CommonRecyclerAdapter<T>
    extends RecyclerView.Adapter<CommonRecyclerViewHolder> {

  protected Context mContext;
  protected LayoutInflater mInflater;
  protected List<T> mData;
  private int mLayoutId;

  // 多布局支持
  private MultiTypeSupport mMultiTypeSupport;

  public CommonRecyclerAdapter(Context context, List<T> data, int layoutId) {
    this.mContext = context;
    this.mInflater = LayoutInflater.from(mContext);
    this.mData = data;
    this.mLayoutId = layoutId;
  }

  public CommonRecyclerAdapter(
      Context context, List<T> data, MultiTypeSupport<T> multiTypeSupport) {
    this(context, data, -1);
    this.mMultiTypeSupport = multiTypeSupport;
  }

  @Override
  public int getItemViewType(int position) {
    // 多布局支持
    if (mMultiTypeSupport != null) {
      return mMultiTypeSupport.getLayoutId(mData.get(position), position);
    }
    return super.getItemViewType(position);
  }

  @Override
  @DebugLog
  public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // 多布局支持
    if (mMultiTypeSupport != null) {
      mLayoutId = viewType;
    }
    // 先inflate数据
    View itemView = mInflater.inflate(mLayoutId, parent, false);
    // 返回ViewHolder
    CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(itemView);

    return holder;
  }

  @Override
  @DebugLog
  public void onBindViewHolder(CommonRecyclerViewHolder holder, final int position) {
    // 设置点击和长按事件
    if (mItemClickListener != null) {
      holder.itemView.setOnClickListener(v -> mItemClickListener.onItemClick(position));
    }
    if (mLongClickListener != null) {
      holder.itemView.setOnLongClickListener(v -> mLongClickListener.onLongClick(position));
    }

    convert(holder, mData.get(position));
  }

  /**
   * 利用抽象方法回传出去，每个不一样的Adapter去设置
   *
   * @param item 当前的数据
   */
  public abstract void convert(CommonRecyclerViewHolder holder, T item);

  @Override
  public int getItemCount() {
    return mData.size();
  }

  private OnItemClickListener mItemClickListener;

  private OnLongClickListener mLongClickListener;

  public void setOnItemClickListener(OnItemClickListener itemClickListener) {
    this.mItemClickListener = itemClickListener;
  }

  public void setOnLongClickListener(OnLongClickListener longClickListener) {
    this.mLongClickListener = longClickListener;
  }

  private interface OnItemClickListener {
    void onItemClick(int position);
  }

  private interface OnLongClickListener {
    boolean onLongClick(int position);
  }

  public interface MultiTypeSupport<T> {
    // 根据当前位置或者条目数据返回布局
    int getLayoutId(T item, int position);
  }
}
