package com.mrns.map.fragment;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mrns.invest.record.CodeRecordList;
import com.mrns.main.R;
import com.mrns.map.MapSearchActivity;
import com.mrns.map.PointResultActivity_;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PointSearchFragment extends Fragment {
    Spinner spinnerPointClass;
    Spinner spinnerPointKind;
    EditText editboxEmdName;
    EditText editboxStartDate;
    EditText editboxEndDate;
    EditText editboxPointName;
    EditText editboxStartPointNo;
    EditText editboxEndPointNo;
    Button btnSearch;

    CodeRecordList mPointClassList;
    CodeRecordList mPointKindList;

    boolean bOnline;

    GregorianCalendar calendar = new GregorianCalendar();
    int mYear = calendar.get(Calendar.YEAR);
    int mMonth= calendar.get(Calendar.MONTH);
    int mDay  = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //UI set
        spinnerPointClass = (Spinner)view.findViewById(R.id.spinner_point_class);
        spinnerPointKind = (Spinner)view.findViewById(R.id.spinner_point_kind);
        editboxEmdName = (EditText)view.findViewById(R.id.editbox_emd_name);
        editboxStartDate = (EditText)view.findViewById(R.id.editbox_start_date);
        editboxEndDate = (EditText)view.findViewById(R.id.editbox_end_date);
        editboxPointName = (EditText)view.findViewById(R.id.editbox_point_name);
        editboxStartPointNo = (EditText)view.findViewById(R.id.editbox_start_point_no);
        editboxEndPointNo = (EditText)view.findViewById(R.id.editbox_end_point_no);
        btnSearch = (Button)view.findViewById(R.id.btn_search);

        //점 구분 리스트 get & 스피너 set
        getPointClassList();

        //선택 이벤트 등록
        spinnerPointClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPointKindList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //클릭 리스너 set
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final int viewId = v.getId();

                switch (viewId) {
                    case R.id.editbox_start_date :
                    case R.id.editbox_end_date :
                        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                setDate(viewId, String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                            }
                        }, mYear, mMonth, mDay).show();
                        break;
                    case R.id.btn_search :
                        onSearch();
                        break;
                }
            }
        };

        //리스너 set
        editboxStartDate.setOnClickListener(listener);
        editboxEndDate.setOnClickListener(listener);
        btnSearch.setOnClickListener(listener);
    }

    private void setDate(int viewId, String date) {
        if (viewId == R.id.editbox_start_date) {
            editboxStartDate.setText(date);
        } else if (viewId == R.id.editbox_end_date) {
            editboxEndDate.setText(date);
        }
    }

    private void onSearch() {
        String sigCode = ((MapSearchActivity)getActivity()).getSelectedSigCode();
        int selPointClassIndex = spinnerPointClass.getSelectedItemPosition();
        int selPointKindIndex = spinnerPointKind.getSelectedItemPosition();

        if (sigCode == null || selPointClassIndex == -1 || selPointKindIndex == -1) {
            Toast.makeText(getContext(), getString(R.string.message_ban_map_search), Toast.LENGTH_SHORT).show();
        } else {
            String pointClassCode = mPointClassList.getCodeList().get(selPointClassIndex);
            String pointKindCode = mPointKindList.getCodeList().get(selPointKindIndex);
            String emdName = editboxEmdName.getText().toString();
            String startDate = editboxStartDate.getText().toString();
            String endDate = editboxEndDate.getText().toString();
            String pointName = editboxPointName.getText().toString();
            String startPointNo = editboxStartPointNo.getText().toString();
            String endPointNo = editboxEndPointNo.getText().toString();

            Intent intent = new Intent(getContext(), PointResultActivity_.class);
            intent.putExtra(getString(R.string.key_bonline), bOnline);
            intent.putExtra(getString(R.string.key_adm_code), sigCode);
            intent.putExtra(getString(R.string.key_emd_name), emdName);
            intent.putExtra(getString(R.string.key_point_class_code), pointClassCode);
            intent.putExtra(getString(R.string.key_point_kind_code), pointKindCode);
            intent.putExtra(getString(R.string.key_start_date), startDate);
            intent.putExtra(getString(R.string.key_end_date), endDate);
            intent.putExtra(getString(R.string.key_point_name), pointName);
            intent.putExtra(getString(R.string.key_start_point_no), startPointNo);
            intent.putExtra(getString(R.string.key_end_point_no), endPointNo);
            startActivity(intent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_point_search, container, false);
    }

    private void getPointClassList() {
        String codeArray[] = getContext().getResources().getStringArray(R.array.point_class_code_array_all);
        String codeValueArray[] = getContext().getResources().getStringArray(R.array.point_class_array_all);

        mPointClassList = new CodeRecordList(
                new ArrayList<>(Arrays.asList(codeArray)),
                new ArrayList<>(Arrays.asList(codeValueArray))
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mPointClassList.getCodeValueList());
        spinnerPointClass.setAdapter(adapter);

        setPointKindList();
    }

    private void setPointKindList() {
        int selPointClassIndex = spinnerPointClass.getSelectedItemPosition();

        int codeResourceId;
        int codeValueResourceId;

        switch (selPointClassIndex) {
            case 0:
                codeResourceId = R.array.pKind_array_cd;
                codeValueResourceId = R.array.pKind_array;
                break;
            case 1:
                codeResourceId = R.array.national_point_kind_array_code;
                codeValueResourceId = R.array.national_point_kind_array;
                break;
            case 2:
                codeResourceId = R.array.common_point_kind_array_code;
                codeValueResourceId = R.array.common_point_kind_array;
                break;
            default:
                codeResourceId = R.array.pKind_array_cd;
                codeValueResourceId = R.array.pKind_array;
        }

        String codeArray[] = getContext().getResources().getStringArray(codeResourceId);
        String codeValueArray[] = getContext().getResources().getStringArray(codeValueResourceId);

        mPointKindList = new CodeRecordList(
                new ArrayList<>(Arrays.asList(codeArray)),
                new ArrayList<>(Arrays.asList(codeValueArray))
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mPointKindList.getCodeValueList());
        spinnerPointKind.setAdapter(adapter);
    }


}
