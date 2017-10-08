package ca.mudar.rotationquicksetting.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.mudar.rotationquicksetting.R;

/**
 * Created by mudar on 16/05/17.
 */

public class AboutBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    public static AboutBottomSheetFragment newInstance() {
        return new AboutBottomSheetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_about, container, false);

        setupListeners(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.about_credits) {
            showWebsite(R.string.url_mudar_ca);
        } else if (id == R.id.about_source_code) {
            showWebsite(R.string.url_github);
        } else if (id == R.id.about_license) {
            startActivity(EulaActivity.newIntent(getActivity()));
        } else if (id == R.id.about_rate_app) {
            showWebsite(R.string.url_playstore);
        }
    }

    private void setupListeners(View view) {
        view.findViewById(R.id.about_credits).setOnClickListener(this);
        view.findViewById(R.id.about_source_code).setOnClickListener(this);
        view.findViewById(R.id.about_license).setOnClickListener(this);
        view.findViewById(R.id.about_rate_app).setOnClickListener(this);
    }

    private void showWebsite(@StringRes int website) {
        final Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.setData(Uri.parse(getResources().getString(website)));
        startActivity(viewIntent);
    }
}
