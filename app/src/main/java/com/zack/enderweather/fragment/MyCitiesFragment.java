package com.zack.enderweather.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zack.enderweather.R;
import com.zack.enderweather.adapter.CityAdapter;
import com.zack.enderweather.presenter.MyCitiesPresenter;
import com.zack.enderweather.view.MyCitiesView;

public class MyCitiesFragment extends Fragment implements MyCitiesView {

    //private static final String ARG_PARAM1 = "param1";

    private MyCitiesPresenter myCitiesPresenter;
    private RecyclerView recyclerView;
    //private String mParam1;

    public MyCitiesFragment() {
        // Required empty public constructor
    }

    /*public static MyCitiesFragment newInstance(String param1) {
        MyCitiesFragment fragment = new MyCitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }*/

        myCitiesPresenter = new MyCitiesPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_cities, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        myCitiesPresenter.setInitialView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myCitiesPresenter.detachView();
    }

    @Override
    public void showInitialView(CityAdapter cityAdapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cityAdapter);
    }

    @Override
    public void onDetectNetworkNotAvailable() {
        Snackbar snackbar = Snackbar.make(recyclerView, R.string.text_network_not_available, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.action_network_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        snackbar.show();
    }

    @Override
    public void showCityDeletionAlertDialog(String cityName, final int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_dialog_delete_city_confirm)
                .setMessage(getResources().getString(R.string.msg_dialog_delete_city_confirm_head) + cityName + getResources().getString(R.string.msg_dialog_delete_city_confirm_tail))
                .setPositiveButton(R.string.btn_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myCitiesPresenter.notifyCityDeleted(position);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }
}
