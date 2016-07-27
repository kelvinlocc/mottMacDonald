package com.mottmacdonald.android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mottmacdonald.android.Models.ItemData;
import com.mottmacdonald.android.Models.SectionData;
import com.mottmacdonald.android.R;
import com.mottmacdonald.android.Utils.FileUtil;
import com.mottmacdonald.android.View.BaseActivity;
import com.mottmacdonald.android.View.GeneralSiteActivity;
import com.mottmacdonald.android.View.ObservationFormActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 * 创建人：Cipher
 * 创建日期：2016/4/20 0:08
 * 备注：
 */
public class GeneralExpandableAdapter extends BaseExpandableListAdapter{
    private final static String TAG = "GeneralExpandable";

    private Context mContext;
    private final int CURRENT_CLOSEOUT = 1;
    private final int CURRENT_REMARK = 2;
    private List<SectionData> mGroupDatas = new ArrayList<>();
    private List<List<ItemData>> mChildrenDatas = new ArrayList<>();
    private List<List<Boolean>> yesList = new ArrayList<>();
    private List<List<Boolean>> noList = new ArrayList<>();
    private List<List<Boolean>> naOrNotObsList = new ArrayList<>();
    private List<List<String>> imagePaths = new ArrayList<>();
    private List<List<String>> closeOutList = new ArrayList<>();
    private List<List<String>> remarkList = new ArrayList<>();
    private LayoutInflater mInflater;
    private int imageSelectGroupPosition = -1;
    private int imageSelectChildrenPostion = -1;
    private AQuery aq;
    private String tempCloseOutTag = "";
    private String tempRemarkTag = "";
    private int currentEditText = 0;

    private class GroupViewHolder{
        TextView subTitle;

        public GroupViewHolder(View convertView) {
            subTitle = (TextView)convertView.findViewById(R.id.sub_title);
        }
    }

    private class ChildrenViewHolder{
        private TextView refTextView;
        private TextView itemTextView;
        private EditText closeOut;
        private EditText remarks;
        private ImageView naOrNotObs;
        private ImageView takePhoto;
        private ImageView yesBtn;
        private ImageView noBtn;

        public ChildrenViewHolder(View convertView) {
            refTextView = (TextView)convertView.findViewById(R.id.ref_text);
            itemTextView = (TextView)convertView.findViewById(R.id.item_text);
            closeOut = (EditText)convertView.findViewById(R.id.close_out_edittext);
            remarks = (EditText)convertView.findViewById(R.id.remark_editext);
            naOrNotObs = (ImageView) convertView.findViewById(R.id.na_ornot_obs);
            takePhoto = (ImageView)convertView.findViewById(R.id.takephoto_btn);
            yesBtn = (ImageView)convertView.findViewById(R.id.yes_btn);
            noBtn = (ImageView)convertView.findViewById(R.id.no_btn);
        }
    }

    public GeneralExpandableAdapter(Context context, List<SectionData> groupDatas,
                                    List<List<ItemData>> childrenDatas) {

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mGroupDatas = groupDatas;
        mChildrenDatas = childrenDatas;
        for (int i = 0; i < mChildrenDatas.size(); i++) {
            List<Boolean> tempYesList = new ArrayList<>();
            List<Boolean> tempNoList = new ArrayList<>();
            List<Boolean> tempNaOrNotObsList = new ArrayList<>();
            List<String> tempPathList = new ArrayList<>();
            List<String> tempCloseOutList = new ArrayList<>();
            List<String> tempRemarkList = new ArrayList<>();
            for (int j = 0; j < mChildrenDatas.get(i).size(); j++) {
                tempYesList.add(false);
                tempNoList.add(false);
                tempNaOrNotObsList.add(false);
                tempPathList.add("");
                tempCloseOutList.add("");
                tempRemarkList.add("");
            }
            yesList.add(tempYesList);
            noList.add(tempNoList);
            naOrNotObsList.add(tempNaOrNotObsList);
            imagePaths.add(tempPathList);
            closeOutList.add(tempCloseOutList);
            remarkList.add(tempRemarkList);
        }
    }

    @Override
    public int getGroupCount() {
        return mGroupDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildrenDatas.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildrenDatas.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_group_subtitle, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        SectionData data = mGroupDatas.get(groupPosition);
        if (!TextUtils.isEmpty(data.title))
            holder.subTitle.setText(data.title);
        Log.i(TAG,"create the unique ID for shared preference:data.title  "+data.title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildrenViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_general_list, null);
            aq = new AQuery(convertView);
            holder = new ChildrenViewHolder(convertView);
            holder.closeOut.setTag(groupPosition+ "-" + childPosition);
            holder.closeOut.addTextChangedListener(new CloseOutTextWatcher(holder));
            holder.remarks.setTag(groupPosition+ "-" + childPosition);
            holder.remarks.addTextChangedListener(new RemarkTextWatcher(holder));
            convertView.setTag(holder);
        }else {
            holder = (ChildrenViewHolder) convertView.getTag();
            holder.closeOut.setTag(groupPosition+ "-" + childPosition);
            holder.remarks.setTag(groupPosition+ "-" + childPosition);
        }
        if (yesList.get(groupPosition).get(childPosition)){
            holder.yesBtn.setImageResource(R.mipmap.chkbox_selected);
        }else{
            holder.yesBtn.setImageResource(R.mipmap.chkbox_unselected);
        }
        if (noList.get(groupPosition).get(childPosition)){
            holder.noBtn.setImageResource(R.mipmap.chkbox_selected);
        }else{
            holder.noBtn.setImageResource(R.mipmap.chkbox_unselected);
        }
        if (naOrNotObsList.get(groupPosition).get(childPosition)){
            holder.naOrNotObs.setImageResource(R.mipmap.chkbox_selected);
        }else{
            holder.naOrNotObs.setImageResource(R.mipmap.chkbox_unselected);
        }
        if (!TextUtils.isEmpty(imagePaths.get(groupPosition).get(childPosition))){
            aq.id(holder.takePhoto).image(imagePaths.get(groupPosition).get(childPosition));
            System.out.println("图片地址:" + imagePaths.get(groupPosition).get(childPosition));
        }

        Log.i(TAG,"create the unique ID for shared preference:groupPosition+ \"-\" + childPosition "+groupPosition+ "-" + childPosition);



        ItemData data = mChildrenDatas.get(groupPosition).get(childPosition);
        holder.refTextView.setText(data.ref);
        holder.itemTextView.setText("·"+ data.header);
        holder.takePhoto.setTag(groupPosition+ "-" + childPosition);
        holder.takePhoto.setOnClickListener(clickListener);
        holder.yesBtn.setTag(groupPosition+ "-" + childPosition);
        holder.yesBtn.setOnClickListener(clickListener);
        holder.noBtn.setTag(groupPosition+ "-" + childPosition);
        holder.noBtn.setOnClickListener(clickListener);
        holder.naOrNotObs.setTag(groupPosition+ "-" + childPosition);
        holder.naOrNotObs.setOnClickListener(clickListener);
        holder.closeOut.setOnTouchListener(closeOutListener);
        holder.closeOut.clearFocus();
        holder.remarks.setOnTouchListener(remarksListener);
        holder.remarks.clearFocus();
        if (currentEditText == CURRENT_CLOSEOUT){
            if (tempCloseOutTag.equals(groupPosition+ "-" + childPosition)) {
                holder.closeOut.requestFocus();
                currentEditText = 0;
                tempCloseOutTag = "";
            }
            holder.closeOut.setSelection(holder.closeOut .getText().length());
        }else if (currentEditText == CURRENT_REMARK){
            if (tempRemarkTag.equals(groupPosition+ "-" + childPosition)) {
                holder.remarks.requestFocus();
                currentEditText = 0;
                tempRemarkTag = "";
            }
            holder.remarks.setSelection(holder.remarks .getText().length());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] strings = ((String)v.getTag()).split("-");
            int groupPosition = Integer.valueOf(strings[0]);
            int childrenPosition = Integer.valueOf(strings[1]);
            switch (v.getId()){
                case R.id.takephoto_btn:
                    imageSelectGroupPosition = groupPosition;
                    imageSelectChildrenPostion = childrenPosition;
                    Log.i(TAG,"create the unique ID for shared preference:groupPosition + childPosition "+groupPosition+ "+" + childrenPosition);
                    String code_tail = groupPosition+"+"+childrenPosition;
                    SharedPreferences myPreference_UniqueCode = mContext.getSharedPreferences("uniqueCode",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPreference_UniqueCode.edit();
                    String code_head = myPreference_UniqueCode.getString("code_head","");
                    editor.putString(code_head,code_tail);
                    editor.commit();
                    Log.i(TAG, "childrenDatas.get(i) groupPosition+childrenPosition" + groupPosition + "+" + childrenPosition);
                    Log.i(TAG,"childrenDatas.get(i) "+mChildrenDatas.get(groupPosition).get(childrenPosition).item_id);

                    ObservationFormActivity
                            .start(mContext, mChildrenDatas.get(groupPosition).get(childrenPosition));
//                    takePhoto();
                    break;

                case R.id.yes_btn:

                    yesList.get(groupPosition).set(childrenPosition, true);
                    noList.get(groupPosition).set(childrenPosition, false);
                    naOrNotObsList.get(groupPosition).set(childrenPosition, false);
                    notifyDataSetChanged();
                    break;

                case R.id.no_btn:
                    yesList.get(groupPosition).set(childrenPosition, false);
                    noList.get(groupPosition).set(childrenPosition, true);
                    naOrNotObsList.get(groupPosition).set(childrenPosition, false);
                    notifyDataSetChanged();
                    break;

                case R.id.na_ornot_obs:
                    yesList.get(groupPosition).set(childrenPosition, false);
                    noList.get(groupPosition).set(childrenPosition, false);
                    naOrNotObsList.get(groupPosition).set(childrenPosition, true);
                    notifyDataSetChanged();
                    break;
            }
        }
    };

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(FileUtil.getFileRoot(mContext) + "/mottCacheImage.jpg");
//        File file = new File(FileUtil.getFileRoot(mContext) + "/mott_" + DeviceUtils.getCurrentTime("yyyyMMddHHmmssSSSS") + "jpg");
        System.out.println("保存地址：" + file.getAbsolutePath());
        Uri imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((BaseActivity)mContext).startActivityForResult(intent, GeneralSiteActivity.TAKE_PHOTO);
    }

    public void setImage(String path){
        imagePaths.get(imageSelectGroupPosition).set(imageSelectChildrenPostion, path);
        notifyDataSetChanged();
    }

    public String getAnswerId(int groupPosition, int childrenPosition){
        if (yesList.get(groupPosition).get(childrenPosition)){
            return "1";
        }else if (noList.get(groupPosition).get(childrenPosition)){
            return "2";
        }else if (naOrNotObsList.get(groupPosition).get(childrenPosition)){
            return "3";
        }else {
            return "4";
        }
    }

//    private TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };

    private View.OnTouchListener closeOutListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            currentEditText = 1;
            tempCloseOutTag = (String) v.getTag();
            return false;
        }
    };

    private View.OnTouchListener remarksListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            currentEditText = 2;
            tempRemarkTag = (String) v.getTag();
            return false;
        }
    };

    class CloseOutTextWatcher implements TextWatcher {
        public CloseOutTextWatcher(ChildrenViewHolder holder) {
            mHolder = holder;
        }

        private ChildrenViewHolder mHolder;

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !"".equals(s.toString())) {
                String[] strings = ((String)mHolder.closeOut.getTag()).split("-");
                int groupPosition = Integer.valueOf(strings[0]);
                int childrenPosition = Integer.valueOf(strings[1]);
                closeOutList.get(groupPosition).set(childrenPosition, s.toString());
            }
        }
    }

    class RemarkTextWatcher implements TextWatcher {
        public RemarkTextWatcher(ChildrenViewHolder holder) {
            mHolder = holder;
        }

        private ChildrenViewHolder mHolder;

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !"".equals(s.toString())) {
                String[] strings = ((String)mHolder.remarks.getTag()).split("-");
                int groupPosition = Integer.valueOf(strings[0]);
                int childrenPosition = Integer.valueOf(strings[1]);
                remarkList.get(groupPosition).set(childrenPosition, s.toString());
            }
        }
    }

    public List<List<String>> getCloseOutData(){
        return closeOutList;
    }

    public List<List<String>> getRemarkData(){
        return remarkList;
    }
}
