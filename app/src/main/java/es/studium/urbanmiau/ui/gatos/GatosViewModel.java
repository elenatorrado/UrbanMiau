package es.studium.urbanmiau.ui.gatos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GatosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GatosViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}