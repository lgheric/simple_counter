package xyz.wardsoft.simple_counter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MotionEventCompat;

import java.lang.reflect.Method;

import xyz.wardsoft.simple_counter.common.SPContents;
import xyz.wardsoft.simple_counter.utils.SPUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    TextView tvShow;
    Button btnPlus;
    Button btnReset;
    Button btnLed;
    int countNow = 0;
    Boolean isTvShowSetBg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        bindView();
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData(){
        context = this;
        int initCount = (Integer) SPUtils.get(this,SPContents.SP_COUNT,0);
        countNow = initCount;
        tvShow.setText(initCount+"");

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/digital_7_mono.ttf");
        tvShow.setTypeface(typeface);
    }

    private void bindView(){
        tvShow = findViewById(R.id.tv_show);
        btnPlus = findViewById(R.id.btn_plus);
        btnReset = findViewById(R.id.btn_reset);
        btnLed = findViewById(R.id.btn_led);

        btnPlus.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnLed.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_plus:
                plus();
                break;
            case R.id.btn_reset:
                reset();
                break;
            case R.id.btn_led:
                ledUp();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);
        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                plus();
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    @SuppressLint("SetTextI18n")
    private void plus(){
        countNow = (countNow < 0 || countNow+1 >= 99999) ? 0 : ++countNow;
        //
        SPUtils.put(this,SPContents.SP_COUNT,countNow);
        tvShow.setText(countNow+"");
    }

    @SuppressLint("SetTextI18n")
    private void reset(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        // Add the buttons
        builder.setPositiveButton(R.string.yes, (dialog, id) -> {
            // User clicked OK button
            SPUtils.put(context,SPContents.SP_COUNT,0);
            countNow = 0;
            tvShow.setText(countNow+"");
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            // User cancelled the dialog
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void ledUp() {
        if(!isTvShowSetBg){
            tvShow.setBackgroundColor(Color.GREEN);
            isTvShowSetBg = true;
        }else{
            tvShow.setBackgroundColor(Color.TRANSPARENT);
            isTvShowSetBg = false;
        }
    }
    //--menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_share:
                shareApp();
                return true;
            case R.id.action_rate:
                rateMe();
                return true;
            case R.id.action_privacy:
                String url = "https://lgheric.github.io/privacy/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.action_contact:
                //showHelp();
                contactUs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    //----------------
    private void shareApp(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Simple Counter:https://play.google.com/store/apps/details?id="+this.getPackageName());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void contactUs(){
        Uri uri = Uri.parse("mailto:robert2021south@gmail.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(intent);
    }

    private void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + this.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }
}