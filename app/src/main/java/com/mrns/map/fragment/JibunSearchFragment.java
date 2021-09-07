package com.mrns.map.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mrns.invest.record.CodeRecordList;
import com.mrns.main.R;
import com.mrns.map.JibunResultActivity_;
import com.mrns.map.MapSearchActivity;

public class JibunSearchFragment extends Fragment {
    EditText editboxEmdName;
    CheckBox checkboxReg;
    EditText editboxMainParcelNo;
    EditText editboxSubParcelNo;
    Button btnSearch;

    CodeRecordList mSidoList;
    CodeRecordList mSigList;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        editboxEmdName = (EditText)view.findViewById(R.id.editbox_emd_name);
        editboxMainParcelNo = (EditText)view.findViewById(R.id.editbox_main_parcel_no);
        editboxSubParcelNo = (EditText)view.findViewById(R.id.editbox_sub_parcel_no);
        checkboxReg = (CheckBox)view.findViewById(R.id.checkbox_reg);
        btnSearch = (Button)view.findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sigCode = ((MapSearchActivity)getActivity()).getSelectedSigCode();

                if (sigCode == null) {
                    Toast.makeText(getContext(), getString(R.string.message_ban_map_search), Toast.LENGTH_SHORT).show();
                } else {
                    String emdName = editboxEmdName.getText().toString();
                    String regCode = checkboxReg.isChecked() ? "2" : "1";
                    String mainParcelNo = editboxMainParcelNo.getText().toString();
                    String subParcelNo = editboxSubParcelNo.getText().toString();

                    Intent moveIntent =new Intent(getContext(), JibunResultActivity_.class);

                    moveIntent.putExtra(getString(R.string.key_adm_code), sigCode);
                    moveIntent.putExtra(getString(R.string.key_emd_name), emdName);
                    moveIntent.putExtra(getString(R.string.key_reg_code), regCode);
                    moveIntent.putExtra(getString(R.string.key_main_parcel_no), mainParcelNo);
                    moveIntent.putExtra(getString(R.string.key_sub_parcel_no), subParcelNo);

                    startActivity(moveIntent);
                }
            }
        });
    }

    public JibunSearchFragment() {
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
        return inflater.inflate(R.layout.fragment_jibun_search, container, false);
    }
}
