package com.mrns.map.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mrns.main.R;
import com.mrns.map.BuildResultActivity_;
import com.mrns.map.MapSearchActivity;

public class BuildSearchFragment extends Fragment  {
    EditText editboxBuildingName;
    Button btnSearch;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        editboxBuildingName = (EditText)view.findViewById(R.id.editbox_building_name);
        btnSearch = (Button)view.findViewById(R.id.btn_search);

        //클릭 리스너 설정
        btnSearch.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sigCode = ((MapSearchActivity)getActivity()).getSelectedSigCode();

                    if (sigCode == null) {
                        Toast.makeText(getContext(), getString(R.string.message_ban_map_search), Toast.LENGTH_SHORT).show();
                    } else {
                        String buildingName = editboxBuildingName.getText().toString();

                        Intent moveIntent = new Intent(getContext(), BuildResultActivity_.class);

                        moveIntent.putExtra(getString(R.string.key_mode), getString(R.string.key_building_name));
                        moveIntent.putExtra(getString(R.string.key_adm_code), sigCode);
                        moveIntent.putExtra(getString(R.string.key_building_name), buildingName);

                        startActivity(moveIntent);
                    }
                }
            }
        );
    }

    public BuildSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_build_search, container, false);
    }
}
