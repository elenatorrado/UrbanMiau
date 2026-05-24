package es.studium.urbanmiau.ui.colonias;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ColoniasViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ColoniasViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}