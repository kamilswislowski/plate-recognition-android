package pl.swislowski.kamil.project.platerecognition.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.swislowski.kamil.project.platerecognition.android.R;


public class DescriptionFragment extends Fragment {


    public static DescriptionFragment newInstance() {
        return new DescriptionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.description_fragment, container, false);
    }

}
