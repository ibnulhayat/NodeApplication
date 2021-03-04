package com.nodecloths.nodeapplication.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.buyer.BuyerCreatePostFragment;
import com.nodecloths.nodeapplication.factory.CreateFactoryProfileFragment;
import com.nodecloths.nodeapplication.quote.CreateQuoteFragment;
import com.nodecloths.nodeapplication.seller.SellerCreatePostFragment;
import com.nodecloths.nodeapplication.stock_lot.CreateStockLotFragment;

public class CreatPost extends AppCompatActivity {

    private String selectFarame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_post);


        selectFarame = getIntent().getStringExtra("Frame");
        if (selectFarame.equals("buying")){
            mChangeFragment(new BuyerCreatePostFragment());
        }else if (selectFarame.equals("selling")){
            mChangeFragment(new SellerCreatePostFragment());
        }else if (selectFarame.equals("stockLot")){
            mChangeFragment(new CreateStockLotFragment());
        }else if (selectFarame.equals("factory")){
            mChangeFragment(new CreateFactoryProfileFragment());
        }else if (selectFarame.equals("quote")){
            mChangeFragment(new CreateQuoteFragment());
        }
    }

    private void mChangeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.createPostFrame, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreatPost.this, MainActivity.class));
        finish();

    }
}
