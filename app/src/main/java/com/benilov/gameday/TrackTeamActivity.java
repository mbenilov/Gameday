package com.benilov.gameday;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
 * Displays all scheduled games for selected team and allows user to add team to
 * their list of tracked teams
 */
public class TrackTeamActivity extends AppCompatActivity {

    Context context;
    Team team;
    TeamTracker tracker;
    RequestQueue queue;
    List<Game> games;
    String getGamesEndpoint = "http://data.nba.net/prod/v1/2017/schedule.json";

    //UI Elements
    TextView teamFullName;
    ListView teamGamesListView;
    ArrayAdapter teamGamesAdapter;
    Button followButton;
    Button unfollowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_team);

        context = getApplicationContext();
        tracker = TeamTracker.getInstance();
        games = new ArrayList<Game>();
        queue = Volley.newRequestQueue(this.context);
        teamFullName = (TextView) findViewById(R.id.teamFullName);
        team = (new Gson()).fromJson(getIntent().getStringExtra("Team"), Team.class);
        teamFullName.setText(team.getFullName());

        // Bind UI Elements
        followButton = (Button) findViewById(R.id.followButton);
        followButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                try {
                    tracker.followTeam(team);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        unfollowButton = (Button) findViewById(R.id.unfollowButton);
        unfollowButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                try {
                    tracker.unfollowTeam(team);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        teamGamesListView = (ListView) findViewById(R.id.teamGamesListView);
        teamGamesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        teamGamesListView.setAdapter(teamGamesAdapter);
        getGames();
    }

    /**
     * Retrieves scheduled games from data.nba.net.
     * then filters on selected team
     */
    public void getGames() {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, getGamesEndpoint, null, new Response.Listener<JSONObject>() {
                    JSONArray scheduleArray = null;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            scheduleArray = response.getJSONObject("league").getJSONArray("standard");
                            for (int i = 0; i < scheduleArray.length(); i++) {
                                JSONObject gameJSONObject = scheduleArray.getJSONObject(i);
                                String hTeamId = gameJSONObject.getJSONObject("hTeam").getString("teamId");
                                String vTeamId = gameJSONObject.getJSONObject("vTeam").getString("teamId");
                                if (team.getTeamId().equals(hTeamId) || team.getTeamId().equals(vTeamId)){
                                    Game game = new Game();
                                    game.setGameId(gameJSONObject.getString("gameId"));
                                    game.setStartDateEastern(gameJSONObject.getString("startDateEastern"));
                                    game.setStartTimeEastern(gameJSONObject.getString("startTimeEastern"));
                                    //game.sethTeam(gameJSONObject.getString("hTeam"));
                                    //game.setvTeam(gameJSONObject.getString("vTeam"));
                                    games.add(game);
                                }
                            }
                            teamGamesAdapter.clear();
                            teamGamesAdapter.addAll(games);
                            teamGamesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "ERROR: " + error.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
        queue.add(request);
    }
}
