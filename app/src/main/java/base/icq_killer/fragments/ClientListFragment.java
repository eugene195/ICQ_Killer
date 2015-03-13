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
import android.widget.TextView;

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
        clientAdapter = new ArrayAdapter<String>(getActivity(),
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

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
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

}
