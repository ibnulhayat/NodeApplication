package com.nodecloths.nodeapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nodecloths.nodeapplication.R;
import com.nodecloths.nodeapplication.buyer.BuyerPostFragment;
import com.nodecloths.nodeapplication.buyer.OwnPostFragment;
import com.nodecloths.nodeapplication.factory.FactoryListFragment;
import com.nodecloths.nodeapplication.factory.FactoryProfileFragment;
import com.nodecloths.nodeapplication.helper.Apis;
import com.nodecloths.nodeapplication.helper.AppController;
import com.nodecloths.nodeapplication.helper.Common;
import com.nodecloths.nodeapplication.quote.OwnQuoteListFragment;
import com.nodecloths.nodeapplication.quote.QuoteListFragment;
import com.nodecloths.nodeapplication.seller.SellerPostFragment;
import com.nodecloths.nodeapplication.stock_lot.OwnStockLotFragment;
import com.nodecloths.nodeapplication.stock_lot.StockLotListFragment;

import static com.nodecloths.nodeapplication.helper.Common.fab;
import static com.nodecloths.nodeapplication.helper.Common.fac;
import static com.nodecloths.nodeapplication.helper.Common.quo;
import static com.nodecloths.nodeapplication.helper.Common.selectFragment;
import static com.nodecloths.nodeapplication.helper.Common.sto;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView nav_view;
    private ActionBarDrawerToggle toggle;
    private TextView tvMenu;
    private TextView tvNavUserName, tvNavPhone;
    private TextView tvBuyingPost, tvSellingPost, tvStockLotPost, tvFactoryPost, tvQuotePost;
    private TextView tvNotificationSetting, tvLogOut, tvHelpLine;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String userType;
    private TextView tvFabric, tvStockLot, tvFactory, tvQuote;
    private LinearLayout layoutUserType;
    private TextView tvListOfBuy, tvListOfSell;
    private String isShow = "false";
    private String selectItem = "buy";
    private int count = 0;
    private String onOff, fabrics, stock, factory, quote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        // By default loaded page
        defaultPage(selectFragment);

        // work in SharedPreferences
        sharedPreferences = getSharedPreferences(Common.MYPRA_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userType = sharedPreferences.getString(Common.USER_TYPE, "");
        Common.phoneNum = sharedPreferences.getString(Common.PHONE_NUMBER, "");
        Common.p_name = sharedPreferences.getString(Common.USER_NAME, "");
        Common.user_loca = sharedPreferences.getString(Common.USER_Location, "");
        Common.userType = userType;

        // notification
        onOff = sharedPreferences.getString(Common.ON_OFF, "");
        fabrics = sharedPreferences.getString(Common.FABRICS, "");
        stock = sharedPreferences.getString(Common.STOCKLOT, "");
        factory = sharedPreferences.getString(Common.FACTORY, "");
        quote = sharedPreferences.getString(Common.QUOTE, "");
        fab = fabrics;
        sto = stock;
        fac = factory;
        quo = quote;

        /* Set Onclick Navigation Drawer Layout */
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // user information
        tvNavUserName.setText(Common.p_name);
        tvNavPhone.setText(Common.phoneNum);

        // set Notification
        setNotification(onOff);

        // NavigationDrawer Button On Click below and show/ hide
        // ivMenu view On Click
        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        /**start select Product List bellow  */
        tvFabric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectItem.equals("buy")) {
                    selectItem = "buy";
                    mChangeFragment(new SellerPostFragment());
                    selectItem(R.drawable.select_item, R.drawable.unselect_item);
                } else {
                    selectItem = "sell";
                    mChangeFragment(new BuyerPostFragment());
                    selectItem(R.drawable.unselect_item, R.drawable.select_item);
                }

                layoutUserType.setVisibility(View.VISIBLE);
                selectView("fabrics");
            }
        });

        /**  Select buy or sell list here */
        tvListOfBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangeFragment(new SellerPostFragment());
                selectItem(R.drawable.select_item, R.drawable.unselect_item);
                count = 1;
                selectItem = "buy";
            }
        });
        tvListOfSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangeFragment(new BuyerPostFragment());
                selectItem(R.drawable.unselect_item, R.drawable.select_item);
                count = 1;
                selectItem = "sell";
            }
        });


        tvStockLot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangeFragment(new StockLotListFragment());
                count = 2;
                layoutUserType.setVisibility(View.GONE);
                selectView("stockLot");
            }
        });

        tvFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangeFragment(new FactoryListFragment());
                count = 3;
                layoutUserType.setVisibility(View.GONE);
                selectView("factory");
            }
        });

        tvQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangeFragment(new QuoteListFragment());
                count = 4;
                layoutUserType.setVisibility(View.GONE);
                selectView("quote");
            }
        });

        /** end select Product List here  */


        //=====For Buying own post On click======
        tvBuyingPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                mChangeFragment(new OwnPostFragment());
                count = 2;
                selectView("fabrics");
                layoutUserType.setVisibility(View.GONE);
            }
        });

        //=====For Selling own post On click======
        tvSellingPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                mChangeFragment(new com.nodecloths.nodeapplication.seller.OwnPostFragment());
                count = 2;
                selectView("fabrics");
                layoutUserType.setVisibility(View.GONE);
            }
        });

        //=====StockLot Own post List On click======
        tvStockLotPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                mChangeFragment(new OwnStockLotFragment());
                selectView("stockLot");
                count = 2;
                layoutUserType.setVisibility(View.GONE);
            }
        });

        //=====Factory profile post On click======
        tvFactoryPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                mChangeFragment(new FactoryProfileFragment());
                selectView("factory");
                count = 2;
                layoutUserType.setVisibility(View.GONE);
            }
        });

        //=====Quote Own post List  On click======
        tvQuotePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                mChangeFragment(new OwnQuoteListFragment());
                selectView("quote");
                count = 2;
                layoutUserType.setVisibility(View.GONE);
            }
        });

        //=====Quote Own post List  On click======
        tvNotificationSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            }
        });

        //=====Log Out On click======
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                logOut();
            }
        });

        //=====HelpLine On click======
        tvHelpLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + Common.help_line_num));
                startActivity(callIntent);
                Toast.makeText(MainActivity.this, "Please Wait......", Toast.LENGTH_SHORT).show();
            }
        });

        getHelpLine();

    }

    private void setNotification(String onOff) {
        if (onOff.equals("ON")) {

            if (!fabrics.isEmpty()) {

                FirebaseMessaging.getInstance().subscribeToTopic("FAbrics");
            }
            if (!stock.isEmpty()) {

                FirebaseMessaging.getInstance().subscribeToTopic("STockLot");
            }
            if (!factory.isEmpty()) {

                FirebaseMessaging.getInstance().subscribeToTopic("FActory");
            }
            if (!quote.isEmpty()) {

                FirebaseMessaging.getInstance().subscribeToTopic("QUote");
            }
        }

    }

    private void logOut() {
        editor.putString(Common.PHONE_NUMBER, "");
        editor.putString(Common.CODE_NUMBER, "");
        editor.putString(Common.USER_TYPE, "");
        editor.commit();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();

    }

    private void initComponent() {

        drawer = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        tvMenu = findViewById(R.id.tvMenu);
        layoutUserType = findViewById(R.id.layoutUserType);
        tvListOfBuy = findViewById(R.id.tvListOfBuy);
        tvListOfSell = findViewById(R.id.tvListOfSell);

        tvFabric = findViewById(R.id.tvFabric);
        tvStockLot = findViewById(R.id.tvStockLot);
        tvFactory = findViewById(R.id.tvFactory);
        tvQuote = findViewById(R.id.tvQuote);

        tvNavUserName = findViewById(R.id.tvNavUserName);
        tvNavPhone = findViewById(R.id.tvNavPhone);
        tvBuyingPost = findViewById(R.id.tvBuyingPost);
        tvSellingPost = findViewById(R.id.tvSellingPost);
        tvStockLotPost = findViewById(R.id.tvStockLotPost);
        tvFactoryPost = findViewById(R.id.tvFactoryPost);
        tvQuotePost = findViewById(R.id.tvQuotePost);
        tvNotificationSetting = findViewById(R.id.tvNotificationSetting);
        tvLogOut = findViewById(R.id.tvLogOut);
        tvHelpLine = findViewById(R.id.tvHelpLine);
    }

    public void mChangeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (count != 1) {
                mChangeFragment(new SellerPostFragment());
                selectView("fabrics");
                layoutUserType.setVisibility(View.VISIBLE);
                count = 1;
            } else if (count == 1) {
                super.onBackPressed();
            }
        }
    }


    private void getHelpLine() {

        StringRequest request = new StringRequest(Request.Method.GET, Apis.help_line, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String number = response.toLowerCase().toString();
                Common.help_line_num = number;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("VOLLEY", error.getMessage());
            }
        });
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(request);

    }


    private void selectBackgroundColor(int C_1, int C_2, int C_3, int C_4, int C_5, int C_6, int C_7, int C_8) {
        tvFabric.setTextColor(getResources().getColor(C_1));
        tvFabric.setBackgroundColor(getResources().getColor(C_2));
        tvStockLot.setTextColor(getResources().getColor(C_3));
        tvStockLot.setBackgroundColor(getResources().getColor(C_4));
        tvFactory.setTextColor(getResources().getColor(C_5));
        tvFactory.setBackgroundColor(getResources().getColor(C_6));
        tvQuote.setTextColor(getResources().getColor(C_7));
        tvQuote.setBackgroundColor(getResources().getColor(C_8));
    }

    private void selectItem(int resId_1, int resId_2) {
        tvListOfBuy.setBackgroundResource(resId_1);
        tvListOfSell.setBackgroundResource(resId_2);
    }

    private void defaultPage(String name) {
        count = 1;

        if (name.equals("fabrics")) {
            layoutUserType.setVisibility(View.VISIBLE);
            if (selectItem.equals("buy")) {
                mChangeFragment(new SellerPostFragment());
                selectItem(R.drawable.select_item, R.drawable.unselect_item);
            } else {
                mChangeFragment(new BuyerPostFragment());
                selectItem(R.drawable.unselect_item, R.drawable.select_item);
            }

        } else if (name.equals("stockLot")) {
            layoutUserType.setVisibility(View.GONE);
            mChangeFragment(new StockLotListFragment());
        } else if (name.equals("factory")) {
            layoutUserType.setVisibility(View.GONE);
            mChangeFragment(new FactoryListFragment());
        } else {
            layoutUserType.setVisibility(View.GONE);
            mChangeFragment(new QuoteListFragment());
        }
        selectView(name);
    }

    private void selectView(String name) {
        if (name.equals("fabrics")) {
            selectBackgroundColor(R.color.selText, R.color.selBack, R.color.unSelT, R.color.unSelB, R.color.unSelT, R.color.unSelB, R.color.unSelT, R.color.unSelB);
        } else if (name.equals("stockLot")) {
            selectBackgroundColor(R.color.unSelT, R.color.unSelB, R.color.selText, R.color.selBack, R.color.unSelT, R.color.unSelB, R.color.unSelT, R.color.unSelB);
        } else if (name.equals("factory")) {
            selectBackgroundColor(R.color.unSelT, R.color.unSelB, R.color.unSelT, R.color.unSelB, R.color.selText, R.color.selBack, R.color.unSelT, R.color.unSelB);
        } else {
            selectBackgroundColor(R.color.unSelT, R.color.unSelB, R.color.unSelT, R.color.unSelB, R.color.unSelT, R.color.unSelB, R.color.selText, R.color.selBack);
        }
    }

}
