package es.studium.urbanmiau.ui.cuidadores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.studium.urbanmiau.databinding.FragmentCuidadoresBinding;

public class CuidadoresFragment extends Fragment {

    private FragmentCuidadoresBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        CuidadoresViewModel cuidadoresViewModel =
                new ViewModelProvider(this).get(CuidadoresViewModel.class);

        binding = FragmentCuidadoresBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.etBuscarCuidador;

        cuidadoresViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}