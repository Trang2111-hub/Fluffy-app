package com.fluffy.app.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fluffy.app.R;

public abstract class BaseHeaderFragment extends Fragment {
    public enum HeaderType {
        DEFAULT, CUSTOM
    }

    private HeaderType headerType = HeaderType.DEFAULT;
    private String customTitle = "";

    protected void setHeader(HeaderType type, @Nullable String title) {
        this.headerType = type;
        this.customTitle = title;
    }

    @LayoutRes
    protected abstract int getFragmentLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.base_fragment_with_header, container, false);
        ViewGroup headerContainer = root.findViewById(R.id.headerContainer);
        if (headerType == HeaderType.DEFAULT) {
            inflater.inflate(R.layout.header_default, headerContainer, true);
        } else {
            View customHeader = inflater.inflate(R.layout.header_custom, headerContainer, true);
            // Set title nếu là custom
            if (customTitle != null) {
                android.widget.TextView txtTitle = customHeader.findViewById(R.id.txtTitle);
                if (txtTitle != null) txtTitle.setText(customTitle);
            }
        }
        // Inflate fragment content
        ViewGroup contentContainer = root.findViewById(R.id.contentContainer);
        inflater.inflate(getFragmentLayout(), contentContainer, true);
        return root;
    }
}
