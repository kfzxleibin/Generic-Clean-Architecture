package com.zeyad.cleanarchitecture.presentation.components;

import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.view.View;

public class ColoredSnackbar {

    private static View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }

    private static Snackbar colorSnackBar(Snackbar snackbar, @ColorInt int colorId) {

        View snackBarView = getSnackBarLayout(snackbar);

        if (snackBarView != null) {
            snackBarView.setBackgroundColor(colorId);
        }

        return snackbar;
    }

    public static Snackbar info(Snackbar snackbar, int color) {
        return colorSnackBar(snackbar, color);
    }

    public static Snackbar error(Snackbar snackbar, int color) {
        return colorSnackBar(snackbar, color);
    }

}
