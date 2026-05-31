package es.studium.urbanmiau.ui.gatos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.studium.urbanmiau.databinding.FragmentGatosBinding;

public class GatosFragment extends Fragment {

    private FragmentGatosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        GatosViewModel gatosViewModel =
                new ViewModelProvider(this).get(GatosViewModel.class);

        binding = FragmentGatosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.etBuscarGatos;

        gatosViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}