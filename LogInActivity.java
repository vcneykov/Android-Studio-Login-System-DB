package com.example.loginsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class LogInActivity extends AppCompatActivity {

    private EditText edtTxtName, edtTxtPassword;
    private Button btnLogIn, btnShowDatabase;
    private ListView customerModelList;

    DataBaseHelper dataBaseHelper;
    List<CustomerModel> everyone;
    ArrayAdapter<CustomerModel> customerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initViews();
        ShowCustomerOnListView(dataBaseHelper);

        btnShowDatabase.setOnClickListener(new View.OnClickListener() {
            private View v;

            @Override
            public void onClick(View v) {
                this.v = v;
                dataBaseHelper = new DataBaseHelper(LogInActivity.this);
                ShowCustomerOnListView(dataBaseHelper);
                if (customerModelList.getVisibility() == View.GONE) {
                    customerModelList.setVisibility(View.VISIBLE);
                }
                else if (customerModelList.getVisibility() == View.VISIBLE) {
                    customerModelList.setVisibility(View.GONE);
                }
            }
        });

        customerModelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedCustomer);
                ShowCustomerOnListView(dataBaseHelper);
                Toast.makeText(LogInActivity.this, "Delete" + clickedCustomer.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtTxtName.getText().toString();
                String password = edtTxtPassword.getText().toString();

                try {
                    if (username.length() > 0 && password.length() > 0) {
                        dataBaseHelper.getReadableDatabase();

                        if (dataBaseHelper.Login(username, password)) {
                            Toast.makeText(LogInActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogInActivity.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                        }
                        dataBaseHelper.close();
                    }
                } catch (Exception e) {
                    Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dataBaseHelper.close();
            }
        });

    }

    private void ShowCustomerOnListView(DataBaseHelper dataBaseHelper) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(LogInActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone());
        customerModelList.setAdapter(customerArrayAdapter);
    }

    private void initViews() {
        edtTxtName = findViewById(R.id.edtTxtName);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnShowDatabase = findViewById(R.id.btnShowDatabase);
        customerModelList = findViewById(R.id.customerModelList);
        dataBaseHelper = new DataBaseHelper(LogInActivity.this);
    }
}