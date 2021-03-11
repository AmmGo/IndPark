package com.hl.indpark.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hl.indpark.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ContactsSortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> mList;
    private List<SortModel> mSelectedList;
    private Context mContext;
    LayoutInflater mInflater;

    private View.OnClickListener mListener;
    private SortModel sortModel;

    public ContactsSortAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        mSelectedList = new ArrayList<SortModel>();
        if (list == null) {
            this.mList = new ArrayList<SortModel>();
        } else {
            this.mList = list;
        }
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SortModel> list) {
        if (list == null) {
            this.mList = new ArrayList<SortModel>();
        } else {
            this.mList = list;
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortModel mContent = mList.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.llPhone = (LinearLayout) view.findViewById(R.id.ll_phone);
            viewHolder.imgTx = (ImageView) view.findViewById(R.id.img_tx);
            viewHolder.tvNumber = (TextView) view.findViewById(R.id.number);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.sortLetters);
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(this.mList.get(position).name);
        viewHolder.tvNumber.setText(this.mList.get(position).number);
        if (this.mList.get(position).sex == 1) {
            viewHolder.imgTx.setBackgroundResource(R.mipmap.user_img_men);
        } else {
            viewHolder.imgTx.setBackgroundResource(R.mipmap.user_img_nv);
        }
        return view;

    }

    public static class ViewHolder {
        public TextView tvLetter;
        public TextView tvTitle;
        public TextView tvNumber;
        public ImageView imgTx;
        public LinearLayout llPhone;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mList.get(position).sortLetters.charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    private boolean isSelected(SortModel model) {
        return mSelectedList.contains(model);
        //return true;
    }

    public SortModel toggleChecked(int position) {
        sortModel = mList.get(position);
        return sortModel;

    }

    private void setSelected(int position) {
        if (!mSelectedList.contains(mList.get(position))) {
            mSelectedList.add(mList.get(position));
        }
    }

    public List<SortModel> getSelectedList() {
        return mSelectedList;
    }
}