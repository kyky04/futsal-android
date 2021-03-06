package uinbdg.skirpsi.futsal.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uinbdg.skirpsi.futsal.Adapter.AdapterTim;
import uinbdg.skirpsi.futsal.Model.DataItemTeam;
import uinbdg.skirpsi.futsal.Model.TeamDetailResponse;
import uinbdg.skirpsi.futsal.Model.TeamResponse;
import uinbdg.skirpsi.futsal.R;
import uinbdg.skirpsi.futsal.Service.ApiClient;
import uinbdg.skirpsi.futsal.Service.AppConstans;
import uinbdg.skirpsi.futsal.Service.FutsalApi;
import uinbdg.skirpsi.futsal.Util.Session;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TimActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.action_search)
    ImageButton actionSearch;
    @BindView(R.id.search_bar)
    CardView searchBar;
    @BindView(R.id.recycler_view_tim)
    RecyclerView recyclerViewPeserta;

    List<DataItemTeam> dataItemTeamList;

    AdapterTim adapterPeserta;

    Retrofit retrofit;
    FutsalApi futsalApi;
    Session session;
    int id_team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new Session(this);
        initView();

        getTim();
    }

    private void initView() {
        retrofit = ApiClient.newInstance();
        futsalApi = retrofit.create(FutsalApi.class);
        dataItemTeamList = new ArrayList<>();


        recyclerViewPeserta.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    void getTim() {
        futsalApi.getTeam().enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.code() == AppConstans.HTTP_OK) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        dataItemTeamList.add(response.body().getData().get(i));
                    }
                    adapterPeserta = new AdapterTim(TimActivity.this, dataItemTeamList);
                    recyclerViewPeserta.setAdapter(adapterPeserta);
                    recyclerViewPeserta.setHasFixedSize(true);
                    adapterPeserta.setOnItemClickListener(new AdapterTim.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent i = new Intent(TimActivity.this, TimDetailActivity.class);
                            i.putExtra(AppConstans.ID_TEAM, dataItemTeamList.get(position).getId());
                            startActivity(i);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {

            }
        });
    }


}
