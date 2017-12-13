/*
 * 杭州绿漫科技有限公司
 * Copyright (c) 16-6-27 下午12:44.
 */

package uama.hangzhou.image.photochoose;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import uama.hangzhou.image.R;
import uama.hangzhou.image.browse.ImagePagerActivity;
import uama.hangzhou.image.util.PhotoToastUtil;
import uama.hangzhou.image.util.SDCardImageLoader;


/**
 * PhotoWall中GridView的适配器
 *
 * @author gujiajia
 */

public class PhotoWallAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePathList = null;
    private int maxNum;
    private ArrayList<String> selectedImageList;
    private int customCheckBoxBg;
    private int cameraBg;
    private int cameraSrc;
    private boolean firstIsCamera;

    PhotoWallAdapter(Context context, ArrayList<String> imagePathList, ArrayList<String> selectedImageList,
                     int maxNum) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.selectedImageList = selectedImageList;
        this.maxNum = maxNum;
    }

    PhotoWallAdapter(Context context, ArrayList<String> imagePathList, ArrayList<String> selectedImageList,
                     int maxNum, int customCheckBoxBg) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.selectedImageList = selectedImageList;
        this.maxNum = maxNum;
        this.customCheckBoxBg = customCheckBoxBg;
    }

    PhotoWallAdapter(Context context, ArrayList<String> imagePathList, ArrayList<String> selectedImageList,
                     int maxNum, int customCheckBoxBg, int cameraBg, int cameraSrc) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.selectedImageList = selectedImageList;
        this.maxNum = maxNum;
        this.customCheckBoxBg = customCheckBoxBg;
        this.cameraBg = cameraBg;
        this.cameraSrc = cameraSrc;
        firstIsCamera = true;
        selectedImageList.add(0, "camera");
    }


    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String filePath = (String) getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.uimage_photo_wall_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.photo_wall_item_photo);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.photo_wall_item_cb);
            try {
                if (customCheckBoxBg > 0) {
                    holder.checkBox.setBackgroundResource(customCheckBoxBg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        boolean bSelected = isSelected(filePath);

        if (bSelected) {
            holder.imageView.setColorFilter(context.getResources().getColor(R.color.uimage_image_checked_bg));
        } else {
            holder.imageView.setColorFilter(null);
        }

        holder.checkBox.setChecked(bSelected);
        if (!TextUtils.isEmpty(filePath)) {
            if (firstIsCamera && position == 0) {
                holder.imageView.setTag(filePath);
                holder.imageView.setBackgroundResource(cameraBg <= 0 ? R.mipmap.camera_bg_blur : cameraBg);
                holder.imageView.setImageResource(cameraSrc <= 0 ? R.mipmap.camera_icon_88black : cameraSrc);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
                holder.checkBox.setVisibility(View.GONE);
            } else {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.imageView.setTag(filePath);
                SDCardImageLoader.getInstance(context).loadImage(4, filePath, holder.imageView);
            }
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelected(filePath)) {
                    if (selectedImageList.size() > (firstIsCamera ? maxNum : maxNum - 1)) {
                        PhotoToastUtil.showErrorDialog(context, maxNum);
                        holder.checkBox.setChecked(false);
                        return;
                    } else {
                        selectedImageList.add(filePath);
                        holder.checkBox.setChecked(true);
                        holder.imageView.setColorFilter(context.getResources().getColor(R.color.uimage_image_checked_bg));
                    }
                } else {
                    selectedImageList.remove(filePath);
                    holder.imageView.setColorFilter(null);
                    holder.checkBox.setChecked(false);
                }

                ((PhotoWallActivity) context).setChooseCounts(firstIsCamera ? selectedImageList.size() - 1 : selectedImageList.size());
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstIsCamera && position == 0) {
                    if (selectedImageList.size() > maxNum) {
                        PhotoToastUtil.showErrorDialog(context, maxNum);
                        return;
                    }
                    //调起拍照
                    ((PhotoWallActivity) context).goToTakePhoto();
                    return;
                }
                List<String> imageList = new ArrayList<>();
                imageList.add("file://" + filePath);
                Intent intent = new Intent(context, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                intent.putExtra(ImagePagerActivity.CAN_SAVE, false);
                intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) imageList);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
        //是否使用自定义的checkbox背景
    }


    public ArrayList<String> getSelectList() {
        return selectedImageList;
    }

    private boolean isSelected(String path) {
        int selectCounts = selectedImageList.size();
        for (int i = 0; i < selectCounts; i++) {
            if (path.equals(selectedImageList.get(i))) {
                return true;
            }
        }
        return false;
    }
}
