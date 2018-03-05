package com.gajananmotors.shopfinder.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.gajananmotors.shopfinder.activity.MainActivity;
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
    //private Activity activity;

    private int shop_id;
    private String shop_image = "", column_name = "", image_name = "";
    private int shop_position;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private Context context;

    public ShopImagesAdapter(Context activity, int shop_id, int shop_position) {
        this.context = activity;
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
            Picasso.with(context)
                    .load(R.drawable.ic_action_done)
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Add", Toast.LENGTH_SHORT).show();
                }
            });
            holder.shopImageEdit.setVisibility(View.GONE);
        } else {
            Picasso.with(context)
                    .load(shop_image)
                    .fit()
                    .placeholder(R.drawable.background_splashscreen)
                    .into(holder.imageView);
            holder.shopImageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            context);
                    alertDialog.setTitle("Choose your option: ");
                    alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //  EditImages(position,shop_id);
                            image_name = EditPostActivity.images.get(position);
                            //int a = Integer.parseInt(EditPostActivity.images.get(position));
                            ((EditPostActivity) context).selectImageOption(EditPostActivity.images.get(position).toString(), image_name);

                            //((Activity) context).startActivityForResult(Intent.createChooser(cameraIntent, "Select Picture"), CAMERA_CODE);
                            //selectImageOption(position, shop_id);
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

    public void setImageInItem(String position, File imageSrc, String imagePath) {


        Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
    }

    public void EditImages(int position, int shop_id) {
        // Log.i("position", "EditImages: "+position+"\n"+shop_id);
        //selectImageOption(position, shop_id);
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        restInterface.updateShopImages(shop_id, "update", position);
    }

   /* public void selectImageOption(int position, int shop_id) {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                ((Activity) context).startActivityForResult(Intent.createChooser(cameraIntent, "Select Picture"), 100);
                //onActivityResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });


        builder.show();
    }*/

    @Override
    public int getItemCount() {
        return EditPostActivity.images.size();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // imageView.setImageBitmap(photo);
        }
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

    public void deleteImages(final String image_name, int shop_id, final String column_name, final int position) {
        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<DeleteShopImagesModel> call = restInterface.deleteShopImage(shop_id, image_name, column_name);
        call.enqueue(new Callback<DeleteShopImagesModel>() {
            @Override
            public void onResponse(Call<DeleteShopImagesModel> call, Response<DeleteShopImagesModel> response) {
                if (response.isSuccessful()) {
                    DeleteShopImagesModel deleteShopImagesModel = response.body();
                    String msg = deleteShopImagesModel.getMsg();
                    Toast.makeText(context, "Image Deleted!" + msg, Toast.LENGTH_LONG).show();
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
                Toast.makeText(context, "Error in Image Deletetion!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
