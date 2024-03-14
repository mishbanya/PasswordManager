package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Password> data = new ArrayList<>();
    RecyclerAdapter recyclerAdapter;
    PrefManager prefManager = new PrefManager(this);
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.buttonAddPassword);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PasswordAddActivity.class);
            startActivity(intent);
        });
        data = prefManager.getPasswords();
        RecyclerView Recycler = findViewById(R.id.recycler);
        recyclerAdapter = new RecyclerAdapter(this, data);
        Recycler.setAdapter(recyclerAdapter);
        Recycler.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter.setOnEditButtonClickListener(password -> {

        });
        recyclerAdapter.setOnViewButtonClickListener(password -> {

        });
    }
}