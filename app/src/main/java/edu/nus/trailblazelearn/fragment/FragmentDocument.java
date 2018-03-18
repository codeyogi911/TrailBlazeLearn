package edu.nus.trailblazelearn.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.adapter.AudioListAdapter;
import edu.nus.trailblazelearn.adapter.DocumentListAdapter;
import edu.nus.trailblazelearn.model.ParticipantItem;

@SuppressLint("ValidFragment")
public class FragmentDocument extends Fragment {
    private ParticipantItem participantItem = new ParticipantItem();
    DocumentListAdapter documentListAdapter;
    //private static String audioUri;
    public FragmentDocument(ParticipantItem participantItem) {
        this.participantItem = participantItem;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentDocumentView = inflater.inflate(R.layout.fragment_document, container, false);
        LinearLayout audioLinearLayout = fragmentDocumentView.findViewById(R.id.fragment_docment_linear_layout);

        RecyclerView recyclerView = fragmentDocumentView.findViewById(R.id.document_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmentDocumentView.getContext()));
        documentListAdapter = new DocumentListAdapter(fragmentDocumentView.getContext(), participantItem.getFileUri());
        recyclerView.setAdapter(documentListAdapter);
        //audioLinearLayout.addView(recyclerView);

        return fragmentDocumentView;
    }

}
