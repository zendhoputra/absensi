package com.example.absensi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;



import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import android.view.Window;
import android.view.ViewGroup;


public class HomeActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView welcomeText = findViewById(R.id.textView2);
        ImageButton logoutButton = findViewById(R.id.logout_button);
        TextView presensiButton = findViewById(R.id.presensiButton);

        String name = getIntent().getStringExtra("name");
        if (name != null) {
            welcomeText.setText("Welcome, " + name + "!");
        }

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(HomeActivity.this, gso);
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        });

        presensiButton.setOnClickListener(v -> showPresensiDialog());
    }

    private void showPresensiDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_presensi_options, null);

        TextView presensiPKL = dialogView.findViewById(R.id.presensiPKL);
        TextView presensiPKN = dialogView.findViewById(R.id.presensiPKN);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        presensiPKL.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, "Presensi PKL diklik", Toast.LENGTH_SHORT).show();
        });

        presensiPKN.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, "Presensi PKN diklik", Toast.LENGTH_SHORT).show();
        });


        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

}
