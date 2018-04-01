package com.benilov.gameday.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.benilov.gameday.R;
import com.benilov.gameday.interfaces.VolleyCallback;
import com.benilov.gameday.models.Team;
import com.benilov.gameday.services.NBADataRetrievalService;

import java.util.List;

/**
 * The Main Activity for the application
 */
public class MainActivity extends AppCompatActivity {
    Context context;
    NBADataRetrievalService nbaDataRetrievalService;

    //UI Elements
    ListView teamsListView;
    ArrayAdapter teamAdapter;
    Button getTeamDetailsButton;
    Button getTrackedTeamsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        nbaDataRetrievalService = new NBADataRetrievalService(context);

        //Bind UI elements
        getTeamDetailsButton = findViewById(R.id.getTeamDetailsButton);
        getTeamDetailsButton.setOnClickListener(new View.OnClickListener(){
            //On click, retrieve and populate ListView with active NBA teams
            public void onClick(View v){
                nbaDataRetrievalService.getTeams(new VolleyCallback<List<Team>>() {
                    @Override
                    public void onSuccessResponse(List<Team> response) {
                        teamAdapter.clear();
                        teamAdapter.addAll(response);
                        teamAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        getTrackedTeamsButton = findViewById(R.id.getTrackedTeamsButton);
        getTrackedTeamsButton.setOnClickListener(new View.OnClickListener(){
            //On click, open ViewTrackedTeamsActivity
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), ViewTrackedTeamsActivity.class);
                startActivity(intent);
            }
        });

        teamsListView = (ListView) findViewById(R.id.teamsListView);
        teamAdapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item);
        teamsListView.setAdapter(teamAdapter);
        teamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //On user selection, pass selected team to TrackTeamActivity
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                Intent intent = new Intent(view.getContext(), TrackTeamActivity.class);
                intent.putExtra("Team",(new Gson()).toJson(teams.get(i)));
                startActivity(intent);
                */
            }
        });
    }

}
