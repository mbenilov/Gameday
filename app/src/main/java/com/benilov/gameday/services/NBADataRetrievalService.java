package com.benilov.gameday.services;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.benilov.gameday.R;
import com.benilov.gameday.interfaces.VolleyCallback;
import com.benilov.gameday.models.Game;
import com.benilov.gameday.models.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by mark on 3/31/18.
 */

public class NBADataRetrievalService {
    private Context context;
    private RequestQueue queue;
    String teamDataEndpoint;
    String gameDataEndpoint;

    public NBADataRetrievalService(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
        this.teamDataEndpoint = context.getString(R.string.teamDataEndpoint);
        this.gameDataEndpoint = context.getString(R.string.gameDataEndpoint);
    }

    public void getTeams(final VolleyCallback<List<Team>> callback) {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, teamDataEndpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<Team> teams = new ArrayList<Team>();
                    JSONArray teamsArray = response.getJSONObject("league").getJSONArray("standard");
                    for (int i = 0; i < teamsArray.length(); i++) {
                        if (teamsArray.getJSONObject(i).getString("isNBAFranchise") == "true") {
                            Team team = mapTeamFromJSON(teamsArray.getJSONObject(i));
                            teams.add(team);
                        }
                    }
                    callback.onSuccessResponse(teams);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(context, "Error retrieving team data.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        queue.add(request);
    }

    // TODO: Implement getGames method with optional team parameter
    public List<Game> getGames(Optional<Team> team, final VolleyCallback<List<Game>> callback) {
        return null;
    }

    private Team mapTeamFromJSON(JSONObject teamJSONObject) {
        Team team = new Team();
        try {
            team.setTeamId(teamJSONObject.getString("teamId"));
            team.setFullName(teamJSONObject.getString("fullName"));
            team.setShortName(teamJSONObject.getString("nickname"));
            team.setCity(teamJSONObject.getString("city"));
            team.setTricode(teamJSONObject.getString("tricode"));
            team.setConference(teamJSONObject.getString("confName"));
            team.setDivision(teamJSONObject.getString("divName"));
        } catch (Exception e) {
            Toast toast = Toast.makeText(context, "Error mapping team data.", Toast.LENGTH_SHORT);
            toast.show();
        }
        return team;
    }
}
