package es.studium.urbanmiau.activityinfo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import es.studium.urbanmiau.R;
import es.studium.urbanmiau.databinding.ActivityGatosInfoBinding;

public class GatosInfo extends AppCompatActivity
{
    private AppBarConfiguration appBarConfiguration;
    private ActivityGatosInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // ViewBinding
        binding = ActivityGatosInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // NavController
        NavController navController =
                Navigation.findNavController(
                        this,
                        R.id.nav_host_fragment_content_gatos_info
                );

        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();

        NavigationUI.setupActionBarWithNavController(
                this,
                navController,
                appBarConfiguration
        );
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController =
                Navigation.findNavController(
                        this,
                        R.id.nav_host_fragment_content_gatos_info
                );

        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}