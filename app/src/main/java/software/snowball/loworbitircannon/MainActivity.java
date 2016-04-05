package software.snowball.loworbitircannon;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.Dialog;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    //another good site: irdb.tk
    //NOTE: NEC is the only brand that's supported as of now
    //NOTE: isFirstRun() is always true right now, for testing purposes.  it can be changed in the checkFirstRun() method.

    ConsumerIrManager ir;
    IRUtil irUtil;

    Toolbar toolbar;
    private final String[] brands = {"NEC", "SAMSUNG", "EPSON"};
    ButtonRectangle power;
    ButtonRectangle powerOn;
    ButtonRectangle powerOff;
    ButtonRectangle video;
    ButtonRectangle focusp; //focus in
    ButtonRectangle focusm; //focus out
    ButtonRectangle brightnessp; //raise brightness
    ButtonRectangle brightnessm; //lower brightness
    ButtonRectangle zoomp;
    ButtonRectangle zoomm;
    ButtonRectangle rapid;
    ButtonRectangle spring;
    TextView brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set up toolbar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //initializing objects
        power = (ButtonRectangle) findViewById(R.id.btnPower);
        powerOn = (ButtonRectangle) findViewById(R.id.btnPowerOn);
        powerOff = (ButtonRectangle) findViewById(R.id.btnPowerOff);
        video = (ButtonRectangle) findViewById(R.id.btnInput);
        focusp = (ButtonRectangle) findViewById(R.id.btnFocusP);
        focusm = (ButtonRectangle) findViewById(R.id.btnFocusM);
        brightnessp = (ButtonRectangle) findViewById(R.id.btnBrightnessp);
        brightnessm = (ButtonRectangle) findViewById(R.id.btnBrightnessm);
        zoomp = (ButtonRectangle) findViewById(R.id.btnZoomp);
        zoomm = (ButtonRectangle) findViewById(R.id.btnZoomm);
        rapid = (ButtonRectangle) findViewById(R.id.btnRapid);
        spring = (ButtonRectangle) findViewById(R.id.btnSpring);
        brand = (TextView) findViewById(R.id.tvBrand);

        irUtil = new IRUtil(getApplicationContext());
        checkFirstRun();

        //setting listeners to handle clicking
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.power();
            }
        });
        powerOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.powerOn();
            }
        });
        powerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.powerOff();
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.input();
            }
        });
        focusp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.focusP();
            }
        });
        focusm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.focusM();
            }
        });
        brightnessp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.brightnessP();
            }
        });
        brightnessm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.brightnessM();
            }
        });
        zoomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.zoomP();
            }
        });
        zoomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.zoomM();
            }
        });
        rapid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.rapidMode();
            }
        });
        spring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irUtil.springMode();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //about and settings options on toolbar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent about = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(about);
                return true;
            case R.id.action_settings:
                Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        isFirstRun = true; //for testing purposes only
        if (isFirstRun) {
            //prompt user for brand
            showInputDialog();
            //modify isFirstRun to be false
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!editText.getText().toString().matches("")) {
                            boolean isValid = false;
                            for (int i = 0; i < brands.length; i++) {
                                if (editText.getText().toString().toUpperCase().trim() == brands[i]) {
                                    isValid = true;
                                    break;
                                }
                            }
                            if (isValid) {
                                irUtil.setCurBrand(editText.getText().toString().toUpperCase());
                                brand.setText("Current Brand: " + editText.getText().toString().toUpperCase());
                            } else {
                                Toast.makeText(getApplicationContext(), "Brand not found! Defaulting to SAMSUNG", Toast.LENGTH_LONG).show();
                                irUtil.setCurBrand("SAMSUNG");
                                brand.setText("Current Brand: " + "SAMSUNG");
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Brand not found! Defaulting to NEC", Toast.LENGTH_LONG).show();
                            irUtil.setCurBrand("NEC");
                            brand.setText("Current Brand: " + "NEC");
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //default brand to NEC
                                irUtil.setCurBrand("NEC");
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
