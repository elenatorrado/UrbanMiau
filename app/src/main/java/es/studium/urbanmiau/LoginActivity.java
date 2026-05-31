package es.studium.urbanmiau;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    String status="success";
    String tipoUsuario="admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establecer el layout de la actividad de inicio de sesión
        setContentView(R.layout.activity_login);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            // Aquí pones lo que quieres que pase al hacer clic
            Toast.makeText(LoginActivity.this, "¡Intentando iniciar sesión!", Toast.LENGTH_SHORT).show();
            if (status.equals("success")) {
               // String tipoUsuario = jsonObject.getString("tipo_usuario");
                Toast.makeText(LoginActivity.this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();

                Intent intent;

               /* // Evaluamos el tipo de usuario para decidir qué pantalla abrir
                switch (tipoUsuario) {
                    case "admin":
                        // Si es administrador, lo mandamos a la actividad de administración
                        intent = new Intent(LoginActivity.this, AdminActivity.class);
                        break;

                    case "cuidador":
                    case "voluntario":

                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        break;

                    default:

                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        break;
                }
*/
                // Iniciamos la nueva actividad
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                // Cerramos la actividad de Login para que si el usuario pulsa el botón "Atrás"
                // en su móvil, no vuelva a la pantalla de iniciar sesión.
                finish();
            }
        });
    }
}