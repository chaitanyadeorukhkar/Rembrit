package com.chaitanyad.rembrit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_FirstRun10.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_FirstRun10#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_FirstRun10 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public TextView confWakeup,confLeaveForWork,confReachWork,confLunch,confLeaveFromWork,confReachHome,confDinner,confSleep;
    private OnFragmentInteractionListener mListener;

    public Fragment_FirstRun10() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_FirstRun10.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_FirstRun10 newInstance(String param1, String param2) {
        Fragment_FirstRun10 fragment = new Fragment_FirstRun10();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__first_run10, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confWakeup= (TextView) view.findViewById(R.id.confWakeup);
        confLeaveForWork= (TextView) view.findViewById(R.id.confLeaveForWork);
        confReachWork= (TextView) view.findViewById(R.id.confReachWork);
        confLunch= (TextView) view.findViewById(R.id.confLunch);
        confLeaveFromWork= (TextView) view.findViewById(R.id.confLeaveFromWork);
        confReachHome= (TextView) view.findViewById(R.id.confReachHome);
        confDinner= (TextView) view.findViewById(R.id.confDinner);
        confSleep= (TextView) view.findViewById(R.id.confSleep);


        confWakeup.setText("Wake up at "+MainActivity.converttime(FirstRunActivity.globWakeupHour,FirstRunActivity.globWakeupMin    ));
        confLeaveForWork.setText("Leave for work at "+MainActivity.converttime(FirstRunActivity.globLeaveForWorkHour,FirstRunActivity.globLeaveForWorkMin));
        confReachWork.setText("Reach work at "+MainActivity.converttime(FirstRunActivity.globReachWorkHour,FirstRunActivity.globReachWorkMin));
        confLunch.setText("Lunch at "+MainActivity.converttime(FirstRunActivity.globLunchHour,FirstRunActivity.globLunchMin));
        confLeaveFromWork.setText("Leave from work at "+MainActivity.converttime(FirstRunActivity.globLeaveFromWorkHour,FirstRunActivity.globLeaveFromWorkMin));
        confReachHome.setText("Reach home at "+MainActivity.converttime(FirstRunActivity.globReachHomeHour,FirstRunActivity.globReachHomeMin));
        confDinner.setText("Dinner at "+MainActivity.converttime(FirstRunActivity.globDinnerHour,FirstRunActivity.globDinnerMin));
        confSleep.setText("Sleep at "+MainActivity.converttime(FirstRunActivity.globSleepHour,FirstRunActivity.globSleepMin));
    }

        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
