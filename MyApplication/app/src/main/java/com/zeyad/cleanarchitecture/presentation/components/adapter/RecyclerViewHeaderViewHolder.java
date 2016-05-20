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
public class RecyclerViewHeaderViewHolder extends GenericRecyclerViewAdapter.ViewHolder {
    @Bind(R.id.tvHeader)
    TextView tvHeader;

    public RecyclerViewHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        super(layoutInflater.inflate(R.layout.list_header_layout, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(Object data, SparseBooleanArray selectedItems, int position) {
        tvHeader.setText((String) data);
    }
}