package com.ustc.app.studyabroad.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.rpc.Help;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.homeActivities.SecondActivity;

public class SearchablePostItems extends BottomSheetDialogFragment implements SearchView.OnQueryTextListener{

    Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o;
    ImageView cancel;
    SearchView search;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.searchable_items_post, container, false);
        setHasOptionsMenu(true);

        setView(v);
        //search.setOnQueryTextListener((AppCompatActivity)getActivity().getApplicationContext());

        setOnClick(a);
        setOnClick(b);
        setOnClick(c);
        setOnClick(d);
        setOnClick(e);
        setOnClick(f);
        setOnClick(g);
        setOnClick(h);
        setOnClick(i);
        setOnClick(j);
        setOnClick(k);
        setOnClick(l);
        setOnClick(m);
        setOnClick(n);
        setOnClick(o);

        setOnClicks();
        searchView();
        return v;
    }

    private void searchView() {


    }

    private void setOnClicks() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setView(View v) {
        a = v.findViewById(R.id.a);
        b = v.findViewById(R.id.b);
        c = v.findViewById(R.id.c);
        d = v.findViewById(R.id.d);
        e = v.findViewById(R.id.e);
        f = v.findViewById(R.id.f);
        g = v.findViewById(R.id.g);
        h = v.findViewById(R.id.h);
        i = v.findViewById(R.id.i);
        j = v.findViewById(R.id.j);
        k = v.findViewById(R.id.k);
        l = v.findViewById(R.id.l);
        m = v.findViewById(R.id.m);
        n = v.findViewById(R.id.n);
        o = v.findViewById(R.id.o);
        cancel = v.findViewById(R.id.cancel);
        search = v.findViewById(R.id.search);

        Helper.print(">>>>>>>>>>>>>>>>>"+b.getText().toString());
    }

    private void setOnClick(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                btn.setBackground(getResources().getDrawable(R.drawable.gradient_profile));
                ((SecondActivity)getActivity()).setSearchableTopic(btn.getText().toString(), "");
                dismiss();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        ((SecondActivity)getActivity()).setSearchableTopic(query, "search");
        dismiss();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>"+newText);
        return false;
    }
}
