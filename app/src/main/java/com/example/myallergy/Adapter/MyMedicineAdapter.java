package com.example.myallergy.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myallergy.Activities.AlarmActivity;
import com.example.myallergy.DataBase.Medicine;
import com.example.myallergy.DataBase.MedicineDAO;
import com.example.myallergy.DataBase.UserDataBase;
import com.example.myallergy.R;


import java.util.ArrayList;
import java.util.List;


public class MyMedicineAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<Medicine> medicineList;
    private TextView textView;
    private ImageButton btnAlarm, btnDelete;

    public MyMedicineAdapter() {
        medicineList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return medicineList.size();
    }
    @Override
    public Medicine getItem(int position) {
        return medicineList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        //뷰 초기화
        if(convertView == null) {
            mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflater.inflate(R.layout.my_medicine_item, viewGroup, false);
        }
        initializeViews(convertView);
        if(getCount() == 0) {
            createNoListView(context);
        } else {
            createLIstViewItem(position, context);
        }

        return convertView;
    }

    private void initializeViews(View view) {
        textView = view.findViewById(R.id.my_medicine_name);
        btnAlarm = view.findViewById(R.id.btn_my_medicine_setAlarm);
        btnDelete = view.findViewById(R.id.btn_my_medicine_delete);
    }

    private void createNoListView(final Context context) {
        textView.setText("내 복용약이 없습니다");
    }

    private void createLIstViewItem(final int position, final Context context) {
        //약 이름으로 textview 설정
        final String medicineName = medicineList.get(position).getMedicineName();
        textView.setText(medicineName);

        //삭제 버튼
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogHandler(context, position);
                notifyDataSetChanged();
            }
        });

        //알람 설정 버튼
        btnAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), AlarmActivity.class);
                intent.putExtra("medicineName",medicineName);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void addMedicine(Medicine medicine) {
        medicineList.add(medicine);
    }

    //삭제 확인 알림창 팝업
    private void alertDialogHandler(final Context context, final int position) {
        new AlertDialog.Builder(context)
                .setTitle("내 복용약에서 삭제")
                .setMessage("내 복용약에서 삭제하시겠습니까??")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserDataBase db = UserDataBase.getInstance(context);
                        MedicineDAO medicineDAO = db.getMedicineDAO();
                        //db에서 해당 약 삭제
                        deleteThisMedicine(medicineDAO, medicineList.get(position));
                    }
                })
                .setNegativeButton("아니오", null)
                .show();
    }

    private void updateMedicineList(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    private void deleteThisMedicine(final MedicineDAO medicineDAO, final Medicine medicine) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                //db에서 삭제하고 리스트 새로 받아옴
                medicineDAO.deleteMedicine(medicine);
                updateMedicineList(medicineDAO.getMedicineList());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }
}
