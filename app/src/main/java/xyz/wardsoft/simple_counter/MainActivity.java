package xyz.wardsoft.simple_counter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import xyz.wardsoft.simple_counter.common.SPContents;
import xyz.wardsoft.simple_counter.utils.SPUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    TextView tvShow;
    Button btnPlus;
    Button btnReset;
    int countNow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData(){
        context = this;
        int initCount = (Integer) SPUtils.get(this,SPContents.SP_COUNT,0);
        countNow = initCount;
        tvShow.setText(initCount+"");
    }

    private void bindView(){
        tvShow = findViewById(R.id.tv_show);
        btnPlus = findViewById(R.id.btn_plus);
        btnReset = findViewById(R.id.btn_reset);

        btnPlus.setOnClickListener(this);
        btnReset.setOnClickListener(this);
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

}