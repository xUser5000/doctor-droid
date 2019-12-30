package com.doctordroid.presentation.results;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.doctordroid.R;
import com.doctordroid.entity.local.Chat;
import com.doctordroid.entity.local.LocalCondition;
import com.doctordroid.entity.local.LocalConditionInfo;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class ResultsActivity extends AppCompatActivity {

    // ui
    private TextView conditionNameText;
    private ConditionInfoView conditionInfoView;
    private RelativeLayout offlineContainer;
    private LinearLayout mainContainer;
    private SwipeRefreshLayout refreshLayout;

    // data
    private ResultsViewModel viewModel;

    private String conditionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        conditionNameText = findViewById(R.id.results_condition_name);
        conditionInfoView = findViewById(R.id.results_condition_info);
        offlineContainer = findViewById(R.id.results_offline_container);
        mainContainer = findViewById(R.id.results_main_container);
        refreshLayout = findViewById(R.id.results_refresh);

        viewModel = ViewModelProviders.of(this).get(ResultsViewModel.class);

        Chat chat = viewModel.getChat(getIntent().getStringExtra("chatId"));
        LocalCondition condition = chat.getLocalConditions().last();

        conditionId = condition.getId();
        conditionNameText.setText(condition.getName());

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            fetchInfo();
        });

        refreshLayout.setRefreshing(true);
        fetchInfo();
    }

    private void fetchInfo () {
        // TODO: Get the condition info from the view model
        viewModel.getConditionInfo(conditionId).subscribe(new SingleObserver<LocalConditionInfo>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onSuccess(LocalConditionInfo conditionInfo) {
                refreshLayout.setRefreshing(false);
                mainContainer.setVisibility(View.VISIBLE);
                conditionInfoView.setConditionInfo(conditionInfo);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                refreshLayout.setRefreshing(false);
                offlineContainer.setVisibility(View.VISIBLE);
                Toast.makeText(ResultsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
