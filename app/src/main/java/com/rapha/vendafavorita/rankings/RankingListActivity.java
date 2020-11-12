package com.rapha.vendafavorita.rankings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.rapha.vendafavorita.R;

public class RankingListActivity extends AppCompatActivity {

    private LinearLayout bt_criar_ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        bt_criar_ranking = (LinearLayout) findViewById(R.id.bt_criar_ranking);

        bt_criar_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}