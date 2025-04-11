package com.example.absensi;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // Pastikan file ini sesuai

        TextView welcomeText = findViewById(R.id.welcome_text);  // 👈 deklarasi di sini
        Button logoutButton = findViewById(R.id.logout_button);

        String name = getIntent().getStringExtra("name");
        if (name != null) {
            welcomeText.setText("Welcome, " + name + "!");
        }

        logoutButton.setOnClickListener(v -> {
            // Logout dari Firebase
            FirebaseAuth.getInstance().signOut();

            // Logout dari Google
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(HomeActivity.this, gso);
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                // Setelah logout, kembali ke LoginActivity
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        });

    }
}
