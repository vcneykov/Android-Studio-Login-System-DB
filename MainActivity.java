package com.example.loginsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.example.loginsystem.DataBaseHelper.CUSTOMER_TABLE;

public class MainActivity extends AppCompatActivity {

    private static final  String TAG = "MainActivity";

    private EditText edtTxtName, edtTxtEmail, edtTxtPassword, edtTxtRePassword;
    private Button btnImage, btnRegister, continueToLogIn;
    private TextView txtWarnName, txtWarnEmail, txtWarnPassword, txtWarnRePassword;
    private CheckBox agreementCheck;
    private ConstraintLayout parent, afterReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Select an image...", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRegister();
            }
        });

        continueToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRegister() {
        Log.d(TAG, "initRegister: Started");

        CustomerModel customerModel = new CustomerModel();

        try {
            if (validateData() && agreementCheck.isChecked()) {
                customerModel = new CustomerModel(-1, edtTxtName.getText().toString(), edtTxtPassword.getText().toString(), edtTxtEmail.getText().toString());
                clearSnackBar();
            }
            else if (!validateData()){
                customerModel = new CustomerModel(-1, "error", "error", "error");
                Toast.makeText(MainActivity.this, "Error creating customer", Toast.LENGTH_SHORT).show();
            }
            else if (!agreementCheck.isChecked()) {
                Toast.makeText(MainActivity.this, "You must agree the license!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error creating customer", Toast.LENGTH_SHORT).show();
            customerModel = new CustomerModel(-1, "error", "error", "error");
        }

        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

        dataBaseHelper.addOne(customerModel);
    }

    private void clearSnackBar() {
        Log.d(TAG, "showSnackBar: Started");
        txtWarnName.setVisibility(View.GONE);
        txtWarnEmail.setVisibility(View.GONE);
        txtWarnPassword.setVisibility(View.GONE);
        txtWarnRePassword.setVisibility(View.GONE);

        Snackbar registration_successful = Snackbar.make(parent, "Registration Successful", 5000);
        registration_successful.setAction("Log In", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration_successful.dismiss();
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
        registration_successful.show();
    }

    private boolean validateData() {
        Log.d(TAG, "validateData: Started");

        boolean flag = true;

        if (edtTxtName.getText().toString().equals("")) {
            txtWarnName.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtWarnName.setVisibility(View.GONE);
                }
            }, 2000);

            flag = false;
        }

        if (edtTxtEmail.getText().toString().equals("")) {
            txtWarnEmail.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtWarnEmail.setVisibility(View.GONE);
                }
            }, 2000);

            flag = false;
        }

        if (!isValidEmail(edtTxtEmail.getText().toString())) {
            txtWarnEmail.setText("Invalid Email!");
            txtWarnEmail.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtWarnEmail.setVisibility(View.GONE);
                }
            }, 2000);

            flag = false;
        }

        if (edtTxtPassword.getText().toString().equals("")) {
            txtWarnPassword.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtWarnPassword.setVisibility(View.GONE);
                }
            }, 2000);

            flag = false;
        }

        if(edtTxtRePassword.getText().toString().equals("") || !edtTxtPassword.getText().toString().equals(edtTxtRePassword.getText().toString())) {
            txtWarnRePassword.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtWarnRePassword.setVisibility(View.GONE);
                }
            }, 2000);

            flag = false;
        }

        return flag;
    }

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void initViews() {
        Log.d(TAG, "initViews: Started");
        edtTxtName = findViewById(R.id.txtName);
        edtTxtEmail = findViewById(R.id.txtEmail);
        edtTxtPassword = findViewById(R.id.txtPassword);
        edtTxtRePassword = findViewById(R.id.txtRePassword);

        btnImage = findViewById(R.id.btnImage);
        btnRegister = findViewById(R.id.btnRegister);
        continueToLogIn = findViewById(R.id.continueToLogIn);

        txtWarnName = findViewById(R.id.txtWarnName);
        txtWarnEmail = findViewById(R.id.txtWarnEmail);
        txtWarnPassword = findViewById(R.id.txtWarnPass);
        txtWarnRePassword = findViewById(R.id.txtWarnRePass);

        agreementCheck = findViewById(R.id.checkAgree);
        parent = findViewById(R.id.parent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu:
                Toast.makeText(this, "Settings...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.alarm_menu:
                Toast.makeText(this, "Alarm...", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}