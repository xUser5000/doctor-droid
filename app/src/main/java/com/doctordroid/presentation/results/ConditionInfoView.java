package com.doctordroid.presentation.results;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doctordroid.R;
import com.doctordroid.common.util.ChatUtil;
import com.doctordroid.entity.local.LocalConditionInfo;

public class ConditionInfoView extends LinearLayout {

    private TextView genderText, categoriesText, prevalenceText, acutenessText, severityText, hintsText;
    private LinearLayout prevalenceContainer, acutenessContainer, severityContainer, hintsContainer;

    public ConditionInfoView(Context context) {
        super(context);
        init();
    }

    public ConditionInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConditionInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ConditionInfoView (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init () {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_condition_info, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        prevalenceContainer = findViewById(R.id.info_prevalence_container);
        acutenessContainer = findViewById(R.id.info_acuteness_container);
        severityContainer = findViewById(R.id.info_severity_container);
        hintsContainer = findViewById(R.id.info_hints_container);

        genderText = findViewById(R.id.info_gender);
        categoriesText = findViewById(R.id.info_categories);
        prevalenceText = findViewById(R.id.info_prevalence);
        acutenessText = findViewById(R.id.info_acuteness);
        severityText = findViewById(R.id.info_severity);
        hintsText = findViewById(R.id.info_hints);
    }

    public void setConditionInfo (LocalConditionInfo conditionInfo) {
        genderText.setText(conditionInfo.getGenderFilter());
        categoriesText.setText(ChatUtil.fromStringListToString(conditionInfo.getCategories()));

        if (conditionInfo.getPrevalence() != null) {
            prevalenceText.setText(conditionInfo.getPrevalence());
            prevalenceContainer.setVisibility(VISIBLE);
        }

        if (conditionInfo.getAcuteness() != null) {
            acutenessText.setText(conditionInfo.getAcuteness());
            acutenessContainer.setVisibility(VISIBLE);
        }

        if (conditionInfo.getSeverity() != null) {
            severityText.setText(conditionInfo.getSeverity());
            severityContainer.setVisibility(VISIBLE);
        }

        if (conditionInfo.getHint() != null) {
            hintsText.setText(conditionInfo.getHint());
            hintsContainer.setVisibility(VISIBLE);
        }
    }
}
