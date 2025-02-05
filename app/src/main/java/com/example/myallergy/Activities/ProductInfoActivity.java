package com.example.myallergy.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myallergy.DataBase.Medicine;
import com.example.myallergy.DataBase.MedicineDAO;
import com.example.myallergy.DataBase.UserDataBase;
import com.example.myallergy.R;
import com.example.myallergy.Retrofit2.MedicineVO;
import com.example.myallergy.Retrofit2.ProductVO;

import java.util.List;


public class ProductInfoActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView pname, prdkind, allergy, rawmtrl, nutrition, seller;

    private UserDataBase db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info_window);

        //view 초기화, 텍스트뷰 초기화
        initializeView();
        setTextView();
    }

    private void initializeView() {
        imageView = findViewById(R.id.product_image);
        pname = findViewById(R.id.product_pname);
        prdkind = findViewById(R.id.product_prdkind);
        allergy = findViewById(R.id.product_allergy);
        rawmtrl = findViewById(R.id.product_rawmtrl);
        nutrition = findViewById(R.id.product_nutrition);
        seller = findViewById(R.id.product_seller);

        //database, dao 초기화
        db = UserDataBase.getInstance(getApplicationContext());
    }

    private void setTextView() {
        //인텐트로 받아온 값들로 텍스트뷰 설정
        Intent intent = getIntent();
        ProductVO product = (ProductVO) intent.getSerializableExtra("product");

        setImageView(product.getImgurl1());
        pname.setText(product.getPname());
        prdkind.setText(product.getPrdkind());
        allergy.setText(product.getAllergy());
        rawmtrl.setText(product.getRawmtrl());
        nutrition.setText(product.getNutrition());
        seller.setText(product.getSeller());
    }

    private void setImageView(String url) {
        Glide.with(this).load(url).into(imageView);
    }

}
