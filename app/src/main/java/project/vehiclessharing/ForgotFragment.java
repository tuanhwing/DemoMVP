package project.vehiclessharing;

import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Tuan on 24/07/2017.
 */

public class ForgotFragment extends Fragment implements View.OnClickListener{
    private static View view;

    private EditText edPhone;
    private TextView tvBack;
    private TextView tvForgot;

    public ForgotFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgot, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addControls();
        addEvents();
        setTextSelector();
    }

    private void setTextSelector() {
        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            tvBack.setTextColor(csl);
            tvForgot.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    private void addControls() {
        edPhone = (EditText) view.findViewById(R.id.ed_phone);
        tvBack = (TextView) view.findViewById(R.id.tv_back);
        tvForgot = (TextView) view.findViewById(R.id.tv_forgot);
    }

    private void addEvents() {
        tvBack.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back: {
                // Replace login fragment
                new SignInActivity().replaceSigninFragment();
                break;
            }
            case R.id.tv_forgot: {
                break;
            }
        }
    }
}
