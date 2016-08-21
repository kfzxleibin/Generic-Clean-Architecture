package com.zeyad.cleanarchitecture.presentation.components.adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeyad.cleanarchitecture.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author by zeyad on 17/05/16.
 */
public class HeadFootViewHolder extends GenericRecyclerViewAdapter.ViewHolder {
    @Bind(R.id.tvHeader)
    TextView tvHeader;

    public HeadFootViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        super(layoutInflater.inflate(R.layout.list_head_foot_layout, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(Object data, SparseBooleanArray selectedItems, int position, boolean isEnabled) {
        itemView.setEnabled(isEnabled);
        if (data instanceof String)
            tvHeader.setText((String) data);
    }
}