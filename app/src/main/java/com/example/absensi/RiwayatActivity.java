package com.example.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.absensi.adapter.RiwayatAdapter;
import com.example.absensi.model.RiwayatModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RiwayatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RiwayatAdapter adapter;
    private ArrayList<RiwayatModel> riwayatList;
    private static final String URL_GET_RIWAYAT = "https://script.google.com/macros/s/AKfycbzBQJ2oiPKR6G-G1WhY5isnvb9D_bAtqpza9rApjSXiW3sRcd0wt8ran46u2WaEEZp4rg/exec"; // Ganti URL-mu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        recyclerView = findViewById(R.id.riwayatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        riwayatList = new ArrayList<>();
        adapter = new RiwayatAdapter(riwayatList);
        recyclerView.setAdapter(adapter);

        fetchRiwayat();
    }

    private void fetchRiwayat() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, URL_GET_RIWAYAT,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject item = dataArray.getJSONObject(i);

                            String nama = item.optString("nama", "Tidak ada nama");
                            String timestamp = item.optString("timestamp", "");
                            String waktuPresensi = item.optString("waktuPresensi", "");

                            String tanggal = formatTanggal(timestamp);

                            riwayatList.add(new RiwayatModel(nama, tanggal, waktuPresensi));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RiwayatActivity.this, "Gagal parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(RiwayatActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    private String formatTanggal(String timestamp) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(timestamp);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
            assert date != null;
            return outputFormat.format(date);
        } catch (Exception e) {
            return "-";
        }
    }
}
