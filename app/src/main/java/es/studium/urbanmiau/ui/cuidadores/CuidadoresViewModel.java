package es.studium.urbanmiau.ui.cuidadores;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CuidadoresViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CuidadoresViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}