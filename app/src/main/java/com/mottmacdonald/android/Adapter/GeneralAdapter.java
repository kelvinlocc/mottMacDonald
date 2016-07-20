package com.mottmacdonald.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mottmacdonald.android.Models.SectionsDataModel;
import com.mottmacdonald.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/17 22:37
 * 备注：
 */
public class GeneralAdapter extends BaseAdapter{

    private List<SectionsDataModel> datas;
    private LayoutInflater mInflater;
    private List<Boolean> yesList = new ArrayList<>();

    private class ViewHolder{
        private TextView refTextView;
        private TextView itemTextView;
        private EditText closeOut;
        private EditText remarks;
        private CheckBox checkBox;
        private RadioGroup radioGroup;
        private ImageView takePhoto;
        private ImageView yesBtn;

        public ViewHolder(View convertView) {
            refTextView = (TextView)convertView.findViewById(R.id.ref_text);
            itemTextView = (TextView)convertView.findViewById(R.id.item_text);
            closeOut = (EditText)convertView.findViewById(R.id.close_out_edittext);
            remarks = (EditText)convertView.findViewById(R.id.remark_editext);
            checkBox = (CheckBox)convertView.findViewById(R.id.checkbox);
//            radioGroup = (RadioGroup)convertView.findViewById(R.id.radiogroup);
            takePhoto = (ImageView)convertView.findViewById(R.id.takephoto_btn);
            yesBtn = (ImageView)convertView.findViewById(R.id.yes_btn);
        }
    }

    public GeneralAdapter(Context context, List<SectionsDataModel> sectionDatas) {
        mInflater = LayoutInflater.from(context);
        datas = sectionDatas;
        for (int i = 0; i < 100; i++) {
            yesList.add(false);
        }
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_general_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (yesList.get(position)){
            holder.yesBtn.setImageResource(R.mipmap.chkbox_selected);
        }else{
            holder.yesBtn.setImageResource(R.mipmap.chkbox_unselected);
        }
        SectionsDataModel temp = datas.get(0);
        holder.refTextView.setText(temp.items.get(0).ref);
        holder.itemTextView.setText(temp.items.get(0).content);
        holder.yesBtn.setTag(position);
        holder.yesBtn.setOnClickListener(listener);
//        holder.radioGroup.setOnCheckedChangeListener(changeListener);
        return convertView;
    }

    private RadioGroup.OnCheckedChangeListener changeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int position = (int) group.getTag();
            yesList.set(position, true);
            System.out.println("发生改变的位置： "+ position);
            notifyDataSetChanged();
        }
    };

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            yesList.set(position, true);
            System.out.println("发生改变的位置： "+ position);
            notifyDataSetChanged();
        }
    };
}
