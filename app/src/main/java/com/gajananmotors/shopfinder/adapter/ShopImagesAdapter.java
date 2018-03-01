package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.activity.AddPostActivity;
import com.gajananmotors.shopfinder.activity.AllPostsActivity;
import com.gajananmotors.shopfinder.activity.EditPostActivity;
import com.gajananmotors.shopfinder.activity.RegisterActivity;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.helper.CircleImageView;
import com.gajananmotors.shopfinder.helper.Constant;
import com.gajananmotors.shopfinder.model.DeleteShopImagesModel;
import com.gajananmotors.shopfinder.model.ShopsListModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ashwin on 2/12/2018.
 */
public class ShopImagesAdapter extends RecyclerView.Adapter<ShopImagesAdapter.MyViewHolder> {
    private Activity activity;
    private Uri mImageCaptureUri;
    private File outPutFile;
    private int shop_id;
    private String shop_image = "", column_name = "", image_name = "";
    private int shop_position;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private Context context;

    public ShopImagesAdapter(Activity activity, int shop_id, int shop_position) {
        this.activity = activity;
        this.shop_id = shop_id;
        this.shop_position = shop_position;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.shops_images_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        shop_image = "http://findashop.in/images/shop_profile/" + shop_id + "/" + EditPostActivity.images.get(position);
        if (position == 0)
            holder.tvimageinfo.setText("Shop Cover");
        else
            holder.tvimageinfo.setText("Shop Image " + position);
        if (TextUtils.isEmpty(EditPostActivity.images.get(position)) || EditPostActivity.images.get(position) == null) {
            Picasso.with(activity)
                    .load(R.drawable.icon_add_image)
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity, "Add", Toast.LENGTH_SHORT).show();
                }
            });
            holder.shopImageEdit.setVisibility(View.GONE);
        } else {
            Picasso.with(activity)
                    .load(shop_image)
                    .fit()
                    .placeholder(R.drawable.background_splashscreen)
                    .into(holder.imageView);
            holder.shopImageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            activity);
                    alertDialog.setTitle("Choose your option: ");
                    alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //  EditImages(position,shop_id);
                            selectImageOption(position, shop_id);
                        }
                    });
                    alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            image_name = EditPostActivity.images.get(position);
                            if (position == 0)
                                column_name = "shop_pic";
                            else
                                column_name = "image" + (position);
                            deleteImages(image_name, shop_id, column_name, position);
                        }
                    });
                    alertDialog.show();
                }
            });
        }
    }

    public void EditImages(int position, int shop_id) {
        // Log.i("position", "EditImages: "+position+"\n"+shop_id);
        selectImageOption(position, shop_id);
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        restInterface.updateShopImages(shop_id, "update", position);
    }

    public void selectImageOption(int position, int shop_id) {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Capture Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                    mImageCaptureUri = Uri.fromFile(outPutFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    // ((Activity) activity).startActivityForResult(intent, CAMERA_CODE);
                    //Activity con;
                    //Intent intent_= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, CAMERA_CODE);
                    //activity.startActivityForResult(intent, CAMERA_CODE);
                    //cameraIntent();
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_CODE);
                    // galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public int getItemCount() {
        return EditPostActivity.images.size();
    }

    public void startActivityForResult(Intent i, int code) {
        if (code == CAMERA_CODE) {
            Toast.makeText(context, "In Activity", Toast.LENGTH_SHORT).show();
        }

        Log.i("", "insede method startActivityForResult()");
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public ImageView shopImageEdit;
        public TextView tvimageinfo;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shop_img);
            shopImageEdit = itemView.findViewById(R.id.shop_img_edit);
            tvimageinfo = itemView.findViewById(R.id.tvimageinfo);

        }
    }

    public void deleteImages(final String image_name, int shop_id, String column_name, final int position) {
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<DeleteShopImagesModel> call = restInterface.deleteShopImage(shop_id, image_name, column_name);
        call.enqueue(new Callback<DeleteShopImagesModel>() {
            @Override
            public void onResponse(Call<DeleteShopImagesModel> call, Response<DeleteShopImagesModel> response) {
                if (response.isSuccessful()) {
                    DeleteShopImagesModel deleteShopImagesModel = response.body();
                    String msg = deleteShopImagesModel.getMsg();
                    Toast.makeText(activity, "Image Deleted!" + msg, Toast.LENGTH_LONG).show();
                    ShopsListModel shopsListModel = AllPostsActivity.shops_list.get(shop_position);
                    if (TextUtils.equals(AllPostsActivity.shops_list.get(shop_position).getShop_pic(), EditPostActivity.images.get(position))) {
                        AllPostsActivity.shops_list.set(shop_position, shopsListModel).setShop_pic("");
                    } else if (TextUtils.equals(AllPostsActivity.shops_list.get(shop_position).getImage1(), EditPostActivity.images.get(position))) {
                        AllPostsActivity.shops_list.set(shop_position, shopsListModel).setImage1("");
                    } else if (TextUtils.equals(AllPostsActivity.shops_list.get(shop_position).getImage2(), EditPostActivity.images.get(position))) {
                        AllPostsActivity.shops_list.set(shop_position, shopsListModel).setImage2("");
                    } else if (TextUtils.equals(AllPostsActivity.shops_list.get(shop_position).getImage3(), EditPostActivity.images.get(position))) {
                        AllPostsActivity.shops_list.set(shop_position, shopsListModel).setImage3("");
                    } else if (TextUtils.equals(AllPostsActivity.shops_list.get(shop_position).getImage4(), EditPostActivity.images.get(position))) {
                        AllPostsActivity.shops_list.set(shop_position, shopsListModel).setImage4("");
                    } else if (TextUtils.equals(AllPostsActivity.shops_list.get(shop_position).getImage5(), EditPostActivity.images.get(position))) {
                        AllPostsActivity.shops_list.set(shop_position, shopsListModel).setImage5("");
                    } else if (TextUtils.equals(AllPostsActivity.shops_list.get(shop_position).getImage6(), EditPostActivity.images.get(position))) {
                        AllPostsActivity.shops_list.set(shop_position, shopsListModel).setImage6("");
                    }
                    EditPostActivity.images.set(position, "");
                    EditPostActivity.refresh();
                }
            }

            @Override
            public void onFailure(Call<DeleteShopImagesModel> call, Throwable t) {
                Toast.makeText(activity, "Error in Image Deletetion!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
