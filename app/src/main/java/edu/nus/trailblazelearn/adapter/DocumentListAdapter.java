package edu.nus.trailblazelearn.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.storage.StreamDownloadTask;

import java.util.ArrayList;

import edu.nus.trailblazelearn.R;

/**
 * Created by Ragu on 18/3/2018.
 */

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> documentListUri;
    private String documentUri;

    public DocumentListAdapter(Context context, ArrayList<String> ddocumentListUri) {
        this.context = context;
        this.documentListUri = ddocumentListUri;
    }

    @Override
    public DocumentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View documentView = layoutInflater.inflate(R.layout.document_list, parent, false);
        return new DocumentListAdapter.ViewHolder(documentView);
    }

    @Override
    public void onBindViewHolder(final DocumentListAdapter.ViewHolder holder, int position) {
        holder.documentName.setText("Document file : " + ++position);
        holder.documentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = holder.getAdapterPosition();
                /*DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(documentListUri.get(itemPosition)));
                downloadRequest.allowScanningByMediaScanner();
                downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);*/
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse(documentListUri.get(itemPosition)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentListUri.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView documentName;
        CardView documentList;

        public ViewHolder(View itemView) {
            super(itemView);
            documentName = itemView.findViewById(R.id.document_name_in_list);
            documentList = itemView.findViewById(R.id.document_list_card_view);

        }
    }
}
