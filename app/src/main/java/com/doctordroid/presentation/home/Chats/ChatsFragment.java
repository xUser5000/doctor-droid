package com.doctordroid.presentation.home.Chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doctordroid.R;
import com.doctordroid.entity.local.Chat;
import com.doctordroid.presentation.chat.ChatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.reactivex.disposables.Disposable;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment implements AddChatDialog.OnAddChatDialogListener {

    // ui
    private View parent;
    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private FloatingActionButton fab;

    // data
    private ChatsViewModel viewModel;
    private Disposable subscription;

    public ChatsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_chats, container, false);
        return parent;
    }

    @Override
    public void onViewCreated(@Nonnull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = parent.findViewById(R.id.chats_recycler_view);
        fab = parent.findViewById(R.id.chats_fab);

        viewModel = ViewModelProviders.of(this).get(ChatsViewModel.class);

        initRecyclerView();

        fab.setOnClickListener(v -> showAddChatDialog());
    }

    private void initRecyclerView() {

        // recycler view settings and adapter attachment
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatsAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        // on item click listener for the recycler view
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("chatId", adapter.getChatAt(position).getId());
            startActivity(intent);
        });

        // Swipe-to-delete functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@Nonnull RecyclerView recyclerView, @Nonnull RecyclerView.ViewHolder viewHolder, @Nonnull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@Nonnull RecyclerView.ViewHolder viewHolder, int i) {
                Chat chat = adapter.getChatAt(viewHolder.getAdapterPosition());
                viewModel.deleteChat(chat);
            }
        }).attachToRecyclerView(recyclerView);

        // get chats and listen for changes using RX
        subscription = viewModel.getChats().subscribe(change -> {
            RealmResults chats = change.getCollection();
            if (chats.isEmpty())
                Toast.makeText(getContext(), "Currently, You don't have any conversations", Toast.LENGTH_SHORT).show();
            adapter.setChats(chats);
        });
    }

    private void showAddChatDialog() {
        AddChatDialog dialog = new AddChatDialog();
        dialog.setListener(this);
        dialog.show(getFragmentManager(), "Add Chat Dialog");
    }

    @Override
    public void onPositiveClick(Chat chat) {
        viewModel.addChat(chat);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.dispose();
        subscription = null;
    }
}
