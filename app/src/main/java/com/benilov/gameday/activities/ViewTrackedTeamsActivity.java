package com.benilov.gameday.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.benilov.gameday.R;
import com.benilov.gameday.models.TeamTracker;

public class ViewTrackedTeamsActivity extends AppCompatActivity {
    TeamTracker tracker;
    ListView trackedTeamsListView;
    ArrayAdapter trackedTeamsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tracked_teams);

        tracker = TeamTracker.getInstance();
        trackedTeamsListView = (ListView) findViewById(R.id.trackedTeamsListView);
        trackedTeamsAdapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item, tracker.getTrackedTeams());
        trackedTeamsListView.setAdapter(trackedTeamsAdapter);
        trackedTeamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast toast = Toast.makeText(getApplicationContext(), "Functionality not available yet!", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
