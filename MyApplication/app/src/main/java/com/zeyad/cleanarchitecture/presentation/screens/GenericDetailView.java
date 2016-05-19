package com.zeyad.cleanarchitecture.presentation.screens;

/**
 * @author by zeyad on 16/05/16.
 */
public interface GenericDetailView<M> extends LoadDataView {
    /**
     * Render an item in the UI.
     *
     * @param item The {@link M} that will be shown.
     */
    void renderItem(M item);
}