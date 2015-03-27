package base.icq_killer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import base.icq_killer.ClientActivity;
import base.icq_killer.R;

public class ClientListFragment extends Fragment implements AbsListView.OnItemClickListener {

    public static final String MY_NAME = "my_name";
    public static final String CLIENT_LIST = "client_list";

    private static String nickname;
    private static String [] clientArray;
    private OnItemSelectedListener listener;

    private AbsListView mListView;
    private ListAdapter clientAdapter;

    public static ClientListFragment newInstance(String name, String [] clients) {
        ClientListFragment fragment = new ClientListFragment();
        nickname = name;
        clientArray = clients;
        return fragment;
    }

    public ClientListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            nickname = savedInstanceState.getString(MY_NAME);
            clientArray = (String []) savedInstanceState.getSerializable(CLIENT_LIST);
        }
        if (clientArray.length == 0)
            setEmptyText("Client list is empty");
        else
            clientAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, clientArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(clientAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != listener) {
            listener.onClientSelected(mListView.getItemAtPosition(position).toString());
        }
    }

    public void setEmptyText(CharSequence emptyText) {
        ((ClientActivity)getActivity()).showMessage((String) emptyText);
    }

    public interface OnItemSelectedListener {
        public void onClientSelected(String nickname);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    private void recreateAdapter () {
        clientAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, clientArray);
        mListView.setAdapter(clientAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CLIENT_LIST, clientArray);
        outState.putString(MY_NAME, nickname);
    }

    public void setClients (String [] newClientArray) {
        clientArray = newClientArray;
        if (clientArray.length == 0)
            setEmptyText("Client list is empty");
        else {
            View curView = getView();
            if (curView != null)
                if (curView.findViewById(android.R.id.list) != null)
                    recreateAdapter();
        }
    }
}
