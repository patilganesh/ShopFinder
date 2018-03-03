package com.gajananmotors.shopfinder.tedpicker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.common.RotateTransformation;
import com.gajananmotors.shopfinder.tedpicker.custom.adapter.BaseRecyclerViewAdapter;

import java.io.File;
import java.io.IOException;

public class Adapter_SelectedPhoto extends BaseRecyclerViewAdapter<Uri, Adapter_SelectedPhoto.SelectedPhotoHolder> {

    int closeImageRes;
    Context context;

    ImagePickerActivity imagePickerActivity;

    public Adapter_SelectedPhoto(ImagePickerActivity imagePickerActivity, int closeImageRes) {
        super(imagePickerActivity);
        this.imagePickerActivity = imagePickerActivity;
        this.closeImageRes = closeImageRes;
    }

    @Override
    public void onBindView(SelectedPhotoHolder holder, int position) {
        Uri uri = getItem(position);
        String filepath = uri.toString();

        int rotateImage = getCameraPhotoOrientation(context, uri, filepath);

        // rotation= exif.getAttribute(ExifInterface.TAG_ORIENTATION);

            Glide.with(imagePickerActivity)
                    .load(uri.toString())
                    .dontAnimate()
                    .centerCrop()
                    .error(R.drawable.no_image)
                    .into(holder.selected_photo);
        holder.iv_close.setTag(uri);
    }

    @Override
    public SelectedPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(R.layout.picker_list_item_selected_thumbnail, parent, false);
        return new SelectedPhotoHolder(view);
    }

    class SelectedPhotoHolder extends RecyclerView.ViewHolder {


        ImageView selected_photo;
        ImageView iv_close;


        public SelectedPhotoHolder(View itemView) {
            super(itemView);
            selected_photo = itemView.findViewById(R.id.selected_photo);
            iv_close = itemView.findViewById(R.id.iv_close);
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = (Uri) view.getTag();
                    imagePickerActivity.removeImage(uri);

                }
            });

        }
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
}
