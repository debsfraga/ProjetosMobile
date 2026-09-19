package br.edu.unisenai.rangonaregua.model;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.edu.unisenai.rangonaregua.R;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtSenha;
    Button btnEntrar, btnCriarConta, btnRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        btnRecuperar = findViewById(R.id.btnRecuperar);

        btnEntrar.setOnClickListener(v -> entrar());
        btnCriarConta.setOnClickListener(v -> criaConta());
        btnRecuperar.setOnClickListener(v -> recuperar());

        //Acessar já logado
        FirebaseAuth atenticar = FirebaseAuth.getInstance();
        if (atenticar.getCurrentUser() != null) {
            Intent rota = new Intent(this, MainActivity.class);
            startActivity(rota);
            finish();
        }
    }

    private void recuperar() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = edtEmail.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email enviado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void entrar() {
        FirebaseAuth autenticar = FirebaseAuth.getInstance();

        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.setError("Obrigatório");
            return;
        } else if (edtSenha.getText().toString().isEmpty()) {
            edtSenha.setError("Obrigatório");
            return;
        }
        // criar a conta do usuario
        autenticar.signInWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString())
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthWeakPasswordException) {
                        Toast.makeText(this, "Senha fraca", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(authResult -> {
                    Intent rota = new Intent(this, MainActivity.class);
                    startActivity(rota);
                    finish();
                });

    }


    private void criaConta() {
        FirebaseAuth autenticar = FirebaseAuth.getInstance();

        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.setError("Obrigatório");
            return;
        } else if (edtSenha.getText().toString().isEmpty()) {
            edtSenha.setError("Obrigatório");
            return;
        }
        // criar a conta do usuario
        autenticar.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString())
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthWeakPasswordException) {
                        Toast.makeText(this, "Senha fraca", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(authResult -> {
                    Intent rota = new Intent(this, MainActivity.class);
                    startActivity(rota);
                    finish();
                });
    }

}
