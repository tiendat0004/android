package vn.edu.tlu.baithicuoiky.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edtEmailForgot;
    private Button btnResetPassword;
    private TextView tvLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        edtEmailForgot = findViewById(R.id.edt_email_forgot);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        tvLogin = findViewById(R.id.tv_login_link);

        // Khi nhấn vào "Đăng nhập" chuyển về LoginActivity
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý nút đặt lại mật khẩu
        btnResetPassword.setOnClickListener(v -> {
            String email = edtEmailForgot.getText().toString().trim();

            if (email.isEmpty()) {
                edtEmailForgot.setError("Vui lòng nhập email");
                edtEmailForgot.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmailForgot.setError("Email không hợp lệ");
                edtEmailForgot.requestFocus();
                return;
            }

            // Gửi email đặt lại mật khẩu
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();
                            // Chuyển về màn hình đăng nhập, gửi kèm email để có thể điền sẵn (nếu muốn)
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            String errorMessage = "Đã xảy ra lỗi. Vui lòng thử lại sau.";
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                errorMessage = "Email không tồn tại";
                            } else if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            Toast.makeText(ForgotPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
