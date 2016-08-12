package com.grability.rappitendero.presentation.components.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.grability.rappitendero.R;

import butterknife.ButterKnife;

/**
 * @author by zeyad on 20/05/16.
 */
public class LoadingViewHolder extends GenericRecyclerViewAdapter.ViewHolder {

    public LoadingViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        super(layoutInflater.inflate(R.layout.list_loading_layout, parent, false));
        ButterKnife.bind(this, itemView);
    }
}