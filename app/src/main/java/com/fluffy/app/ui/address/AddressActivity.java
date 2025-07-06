package com.fluffy.app.ui.address;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
import com.fluffy.app.R;
import com.fluffy.app.adapter.AddressListAdapter;
import com.fluffy.app.model.AddressItem;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ListView listView = findViewById(R.id.listViewAddress);
        List<AddressItem> addressItems = new ArrayList<>();
        addressItems.add(new AddressItem("Nguyễn Quốc Khải", "0362614457", "Số 123 Hai Bà Trưng, TP Bình Dương", true));
        addressItems.add(new AddressItem("Trịnh Tiến Đạt Khoa", "0912345678", "456 Lê Lợi, TP Hồ Chí Minh", false));
        addressItems.add(new AddressItem("Trần Thị Thùy Trang", "0912345678", "456 Lê Lợi, TP Hồ Chí Minh", false));

        AddressListAdapter adapter = new AddressListAdapter(this, addressItems);
        listView.setAdapter(adapter);
    }
}
