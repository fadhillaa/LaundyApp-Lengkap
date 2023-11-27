package com.example.laundryapp.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.laundryapp.R;
import com.example.laundryapp.db.DatabaseHelper;
import com.example.laundryapp.util.FunctionHelper;

public class IroningActivity extends AppCompatActivity {

    public static final String DATA_TITLE = "TITLE";
    private AppCompatButton btnCheckout;
    private final int[] prices = {7000, 5000, 8000, 55000, 150000};
    private int[] itemCounts = new int[5];
    private int[] countItems = new int[5];
    private TextView[] priceViews = new TextView[5];
    private TextView[] itemPrices = new TextView[5];
    private TextView tvTitle, tvInfo, tvTotalItems, tvTotalPrice;
    private ImageView[] imageAdds = new ImageView[5];
    private ImageView[] imageMinuses = new ImageView[5];
    private String strTitle, email;
    private int totalItems;
    private int totalPrice;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);

        email = getIntent().getStringExtra("email");
        initializeViews();

        strTitle = getIntent().getStringExtra(DATA_TITLE);
        if (strTitle != null) {
            tvTitle.setText(strTitle);
        }
        dbHelper = new DatabaseHelper(this);

        setTextViewDefaults();
        setListeners();
        setStatusbar();
        setTotalPrice();
        setItemData();

    }

    private void initializeViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvInfo = findViewById(R.id.tvInfo);
        tvTotalItems = findViewById(R.id.tvJumlahBarang);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        int[] priceIds = {R.id.tvKaos, R.id.tvCelana, R.id.tvJaket, R.id.tvSprei, R.id.tvKarpet};
        int[] itemIds = {R.id.tvPriceKaos, R.id.tvPriceCelana, R.id.tvPriceJaket, R.id.tvPriceSprei, R.id.tvPriceKarpet};
        int[] addIds = {R.id.imageAdd1, R.id.imageAdd2, R.id.imageAdd3, R.id.imageAdd4, R.id.imageAdd5};
        int[] minusIds = {R.id.imageMinus1, R.id.imageMinus2, R.id.imageMinus3, R.id.imageMinus4, R.id.imageMinus5};

        for (int i = 0; i < 5; i++) {
            priceViews[i] = findViewById(priceIds[i]);
            itemPrices[i] = findViewById(itemIds[i]);
            imageAdds[i] = findViewById(addIds[i]);
            imageMinuses[i] = findViewById(minusIds[i]);
        }
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalItems == 0 || totalPrice == 0) {
                    Toast.makeText(IroningActivity.this, "Harap pilih jenis barang!", Toast.LENGTH_SHORT).show();
                }else{
                    Checkout(email);
                    Toast.makeText(IroningActivity.this, "Pesanan Anda sedang diproses, cek di menu riwayat", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void setListeners() {
        for (int i = 0; i < 5; i++) {
            final int index = i;
            priceViews[i].setText(FunctionHelper.rupiahFormat(prices[i]));

            imageAdds[i].setOnClickListener(v -> {
                itemCounts[index]++;
                itemPrices[index].setText(String.valueOf(itemCounts[index]));
                countItems[index] = prices[index] * itemCounts[index];
                setTotalPrice();
            });

            imageMinuses[i].setOnClickListener(v -> {
                if (itemCounts[index] > 0) {
                    itemCounts[index]--;
                    itemPrices[index].setText(String.valueOf(itemCounts[index]));
                }
                countItems[index] = prices[index] * itemCounts[index];
                setTotalPrice();
            });
        }
    }

    private void setTotalPrice() {
        totalItems = 0;
        totalPrice = 0;

        for (int i = 0; i < 5; i++) {
            totalItems += itemCounts[i];
            totalPrice += countItems[i];
        }

        tvTotalItems.setText(totalItems + " items");
        tvTotalPrice.setText(FunctionHelper.rupiahFormat(totalPrice));
    }
    private void setItemData() {
        for (int i = 0; i < 5; i++) {
            priceViews[i].setText(FunctionHelper.rupiahFormat(prices[i]));
            itemCounts[i] = 0;
            itemPrices[i].setText("0");
        }
    }

    private void setStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }

    private void setTextViewDefaults() {
        tvTotalItems.setText("0 items");
        tvTotalPrice.setText("Rp 0");
        tvInfo.setText("Menghilangkan lipatan dari pakaian Anda menggunakan setrika listrik dan uap.");
    }
    private void Checkout(String userEmail) {
        dbHelper.insertCheckoutData(strTitle, totalItems, totalPrice, userEmail);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

}
