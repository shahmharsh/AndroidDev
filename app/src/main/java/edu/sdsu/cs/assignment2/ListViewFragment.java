package edu.sdsu.cs.assignment2;

/**
 * Created by Horsie on 2/13/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListViewFragment extends ListFragment {

    public interface OnDataPass {
        public void onDataPass(int position);
    }

    String[] android_versions = new String[] { "Cupcake", "Donut", "Gingerbread", "Ice Cream", "Jelly Bean"};
    OnDataPass dataPasser;

    public ListViewFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataPasser = (OnDataPass) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        registerForContextMenu(getListView());
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int selection = bundle.getInt(MainActivity.LIST_VIEW_SELECTION);
            if (selection != -1) {
                getListView().setItemChecked(selection, true);
                dataPasser.onDataPass(selection);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                inflater.getContext(), android.R.layout.simple_list_item_activated_1, android_versions);
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        l.setItemChecked(position, true);
        dataPasser.onDataPass(position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Clear Selection");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        getListView().setItemChecked(-1, true);
        dataPasser.onDataPass(-1);
        return super.onContextItemSelected(item);
    }
}