package com.rapha.vendafavorita.informacoes.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rapha.vendafavorita.R;
import com.rapha.vendafavorita.objects.ObjectRevenda;
import com.rapha.vendafavorita.objects.TopRevendedores;
import com.rapha.vendafavorita.objects.TopProdutosRevenda;

import java.util.ArrayList;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    private ArrayList<TopProdutosRevenda> topProdutosRevendas30;
    private ArrayList<TopRevendedores> topRevendedores30;
    private ArrayList<ObjectRevenda> revendas30;

    private ArrayList<TopProdutosRevenda> topProdutosRevendas7;
    private ArrayList<TopRevendedores> topRevendedores7;
    private ArrayList<ObjectRevenda> revendas7;

    private ArrayList<TopProdutosRevenda> topProdutosRevendas1;
    private ArrayList<TopRevendedores> topRevendedores1;
    private ArrayList<ObjectRevenda> revendas1;

    private ArrayList<TopProdutosRevenda> topProdutosRevendasHoje;
    private ArrayList<TopRevendedores> topRevendedoresHoje;
    private ArrayList<ObjectRevenda> revendasHoje;

    public SectionsPagerAdapter(Context context, FragmentManager fm, ArrayList<TopProdutosRevenda> topProdutosRevendas30, ArrayList<TopRevendedores> topRevendedores, ArrayList<ObjectRevenda> revendas30, ArrayList<TopProdutosRevenda> topProdutosRevendas7, ArrayList<TopRevendedores> topRevendedores7, ArrayList<ObjectRevenda> revendas7, ArrayList<TopProdutosRevenda> topProdutosRevendas1, ArrayList<TopRevendedores> topRevendedores1, ArrayList<ObjectRevenda> revendas1, ArrayList<TopProdutosRevenda> topProdutosRevendasHoje, ArrayList<TopRevendedores> topRevendedoresHoje, ArrayList<ObjectRevenda> revendasHoje) {
        super(fm);
        mContext = context;
        this.topRevendedores30 = topRevendedores;
        this.topProdutosRevendas30 = topProdutosRevendas30;
        this.revendas30 = revendas30;

        this.topRevendedores7 = topRevendedores7;
        this.topProdutosRevendas7 = topProdutosRevendas7;
        this.revendas7 = revendas7;

        this.topRevendedores1 = topRevendedores1;
        this.topProdutosRevendas1 = topProdutosRevendas1;
        this.revendas1 = revendas1;

        this.topRevendedoresHoje = topRevendedoresHoje;
        this.topProdutosRevendasHoje = topProdutosRevendasHoje;
        this.revendasHoje = revendasHoje;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch (position) {
            case 0: return PlaceholderFragment.newInstance(topProdutosRevendasHoje, topRevendedoresHoje, revendasHoje);
            case 1: return PlaceholderFragment.newInstance(topProdutosRevendas1, topRevendedores1, revendas1);
            case 2: return PlaceholderFragment.newInstance(topProdutosRevendas7, topRevendedores7, revendas7);
            case 3: return PlaceholderFragment.newInstance(topProdutosRevendas30, topRevendedores30, revendas30);
            default: return null;
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Hoje";
            case 1:
                return "24 horas";
            case 2:
                return "1 Semana";
            case 3:
                return "30 dias";
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }
}