package com.example.login;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

public class SearchActivity_title extends ListFragment {

    private static String[] NUMBERS=new String[] {"ISBN 검색", "과목별 검색","책이름 검색"};

    private ListView mListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, NUMBERS));

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

}