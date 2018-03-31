package com.benilov.gameday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The Main Activity for the application
 */
public class MainActivity extends AppCompatActivity {
    Context context;
    RequestQueue queue;
    List<Team> teams; //List to store active NBA teams
    String getTeamsEndpoint = "http://data.nba.net/prod/v1/2017/teams.json";

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
        queue = Volley.newRequestQueue(this.context);
        teams = new ArrayList<Team>();

        //Bind UI elements
        getTeamDetailsButton = findViewById(R.id.getTeamDetailsButton);
        getTeamDetailsButton.setOnClickListener(new View.OnClickListener(){

            //On click, retrieve and populate ListView with active NBA teams
            public void onClick(View v){
                getTeams();
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
                Intent intent = new Intent(view.getContext(), TrackTeamActivity.class);
                intent.putExtra("Team",(new Gson()).toJson(teams.get(i)));
                startActivity(intent);
            }
        });
    }

    /**
     * Retrieves available NBA teams from data.nba.net.
     */
    public void getTeams() {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, getTeamsEndpoint, null, new Response.Listener<JSONObject>() {
                    JSONArray teamsArray = null;

                    // Parse JSON response into Team objects
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            teamsArray = response.getJSONObject("league").getJSONArray("standard");
                            for (int i = 0; i < teamsArray.length(); i++) {
                                if (teamsArray.getJSONObject(i).getString("isNBAFranchise") == "true") {
                                    JSONObject teamJSONObject = teamsArray.getJSONObject(i);
                                    Team team = new Team();
                                    team.setTeamId(teamJSONObject.getString("teamId"));
                                    team.setFullName(teamJSONObject.getString("fullName"));
                                    team.setShortName(teamJSONObject.getString("nickname"));
                                    team.setCity(teamJSONObject.getString("city"));
                                    team.setTricode(teamJSONObject.getString("tricode"));
                                    team.setConference(teamJSONObject.getString("confName"));
                                    team.setDivision(teamJSONObject.getString("divName"));
                                    teams.add(team);
                                }
                            }
                            teamAdapter.clear();
                            teamAdapter.addAll(teams);
                            teamAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(context, "ERROR: " + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
        queue.add(request);
    }

}
