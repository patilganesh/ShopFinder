package com.gajananmotors.shopfinder.tedpicker;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.tedpicker.custom.adapter.BaseRecyclerViewAdapter;
public class Adapter_SelectedPhoto extends BaseRecyclerViewAdapter<Uri, Adapter_SelectedPhoto.SelectedPhotoHolder> {
    int closeImageRes;
    ImagePickerActivity imagePickerActivity;
    public Adapter_SelectedPhoto(ImagePickerActivity imagePickerActivity, int closeImageRes) {
        super(imagePickerActivity);
        this.imagePickerActivity = imagePickerActivity;
        this.closeImageRes = closeImageRes;
    }
    @Override
    public void onBindView(SelectedPhotoHolder holder, int position) {
        if (position <= 6) {
            Uri uri = getItem(position);
            Glide.with(imagePickerActivity)
                    .load(uri.toString())
                    .dontAnimate()
                    .centerCrop()
                    .error(R.drawable.no_image)
                    .into(holder.selected_photo);
            holder.iv_close.setTag(uri);
        } else
            Toast.makeText(imagePickerActivity, "You selected more than 7 images,Maximum 7 images allowed !", Toast.LENGTH_SHORT).show();
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
}
