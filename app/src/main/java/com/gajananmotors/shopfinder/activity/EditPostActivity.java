package com.gajananmotors.shopfinder.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gajananmotors.shopfinder.R;
import com.gajananmotors.shopfinder.apiinterface.RestInterface;
import com.gajananmotors.shopfinder.common.APIClient;
import com.gajananmotors.shopfinder.common.ViewShopList;
import com.gajananmotors.shopfinder.model.CategoryListModel;
import com.gajananmotors.shopfinder.model.CategoryModel;
import com.gajananmotors.shopfinder.model.SubCategoryModel;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditPostActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewShopList viewShopList;
    private int shop_id;
    private EditText etShopName, etAddress, etMobileNumber, etServicesOffered, etShopOpeningHours, etWebsite;
    private EditText etCategory, etSubcategory;
    private Button btncategory;
    private String Category, SubCategory;
    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayList<CategoryModel> category_Model_list = new ArrayList<>();
    private ArrayList<SubCategoryModel> sub_category_list = new ArrayList<>();
    private ListView listView;
    private Dialog listDialog;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etShopName = findViewById(R.id.etShopName);
        etAddress = findViewById(R.id.etAddress);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etServicesOffered = findViewById(R.id.etServicesOffered);
        etShopOpeningHours = findViewById(R.id.etShopOpeningHours);
        etWebsite = findViewById(R.id.etWebsite);
        btncategory = findViewById(R.id.etCategory);
        etSubcategory = findViewById(R.id.etSubcategory);
        viewShopList = getIntent().getParcelableExtra("shop_list");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        etShopName.setText(viewShopList.getStrShop_name());
        etAddress.setText(viewShopList.getStrAddress());
        etMobileNumber.setText(viewShopList.getStrMobile());
        btncategory.setText(viewShopList.getStrCategory());
        etSubcategory.setText(viewShopList.getStrSub_category());
        etServicesOffered.setText(viewShopList.getStrShop_services());
        etShopOpeningHours.setText(viewShopList.getStrShop_time());
        etWebsite.setText(viewShopList.getStrWeburl());

        Retrofit retrofit = APIClient.getClient();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        Call<CategoryListModel> call = restInterface.getCategoryList();
        call.enqueue(new Callback<CategoryListModel>() {
            @Override
            public void onResponse(Call<CategoryListModel> call, Response<CategoryListModel> response) {
                if (response.isSuccessful()) {
                    CategoryListModel categoryListModel = response.body();
                    category_Model_list = categoryListModel.getCategories();
                    for (int i = 0; i < category_Model_list.size(); i++) {
                        categoryNames.add(category_Model_list.get(i).getName().toString());
                        flag = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryListModel> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, "Fail to Load Category", Toast.LENGTH_SHORT).show();
            }
        });

        btncategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
      /*  etCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialog();
                return false;
            }
        });*/
    }

    public void setCategories() {
        final String[] categories = categoryNames.toArray(new String[categoryNames.size()]);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categories);
        LayoutInflater inflater = LayoutInflater.from(EditPostActivity.this);
        final View dialog = inflater.inflate(R.layout.category_list, null);
        final ListView listView = dialog.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Select Categories:");
        dialogBuilder.setView(dialog);
        dialogBuilder.setCancelable(true);
        //final AlertDialog alertDialogObject = dialogBuilder.create();
        final Dialog alertDialogObject = dialogBuilder.show();
        //Show the dialog
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //    Toast.makeText(EditPostActivity.this, categories[position], Toast.LENGTH_SHORT).show();
                alertDialogObject.cancel();
                etCategory.setText(categories[position]);

            }
        });

        //Create alert dialog object via builder
    }

    private void showDialog() {
        final String[] categories = categoryNames.toArray(new String[categoryNames.size()]);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categories);
        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.category_list, null);
        ListView lv = (ListView) view.findViewById(R.id.listview);
        lv.setAdapter(adapter);
        // Change MyActivity.this and myListOfItems to your own values
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.cancel();
                btncategory.setText(categories[position]);
            }
        });

        dialog.setContentView(view);
        dialog.show();

    }
}
