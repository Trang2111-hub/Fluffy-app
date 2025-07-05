package com.fluffy.app.ui.policy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fluffy.app.R;
import com.fluffy.app.ui.common.BaseHeaderFragment;

public class ChinhSachFragment extends BaseHeaderFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sử dụng header custom với title động
        setHeader(HeaderType.CUSTOM, "Chính sách");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_chinh_sach;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
