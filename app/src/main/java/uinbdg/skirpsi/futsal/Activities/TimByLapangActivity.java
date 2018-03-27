package uinbdg.skirpsi.futsal.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uinbdg.skirpsi.futsal.Adapter.AdapterTim;
import uinbdg.skirpsi.futsal.Model.DataItem;
import uinbdg.skirpsi.futsal.Model.DataItemTeam;
import uinbdg.skirpsi.futsal.Model.TeamDetailResponse;
import uinbdg.skirpsi.futsal.Model.TeamResponse;
import uinbdg.skirpsi.futsal.R;
import uinbdg.skirpsi.futsal.Service.ApiClient;
import uinbdg.skirpsi.futsal.Service.AppConstans;
import uinbdg.skirpsi.futsal.Service.FutsalApi;
import uinbdg.skirpsi.futsal.Util.Session;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TimByLapangActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.action_search)
    ImageButton actionSearch;
    @BindView(R.id.search_bar)
    CardView searchBar;
    @BindView(R.id.recycler_view_tim)
    RecyclerView recyclerViewTim;

    List<DataItemTeam> dataItemTeamList;

    AdapterTim adapterPeserta;

    Retrofit retrofit;
    FutsalApi futsalApi;

    int id_lapang, id_team;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new Session(this);

        id_lapang = getIntent().getIntExtra("id_lapang", 0);
        initView();
        getMytim();
        getTim();
    }

    private void initView() {
        retrofit = ApiClient.newInstance();
        futsalApi = retrofit.create(FutsalApi.class);
        dataItemTeamList = new ArrayList<>();

        recyclerViewTim.setLayoutManager(new LinearLayoutManager(this));

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
        futsalApi.getTeamByLapang(id_lapang).enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.code() == AppConstans.HTTP_OK) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        dataItemTeamList.add(response.body().getData().get(i));
                    }
                    adapterPeserta = new AdapterTim(TimByLapangActivity.this, dataItemTeamList);
                    recyclerViewTim.setAdapter(adapterPeserta);
                    recyclerViewTim.setHasFixedSize(true);
                    adapterPeserta.setOnItemClickListener(new AdapterTim.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            ajakTanding(dataItemTeamList.get(position));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {

            }
        });
    }


    void getMytim() {
        futsalApi.getMyTim(session.getID()).enqueue(new Callback<TeamDetailResponse>() {
            @Override
            public void onResponse(Call<TeamDetailResponse> call, Response<TeamDetailResponse> response) {
                if (response.code() == AppConstans.HTTP_OK) {
                    id_team = response.body().getData().getId();
                } else if (response.code() == 404) {

                }
            }

            @Override
            public void onFailure(Call<TeamDetailResponse> call, Throwable t) {

            }
        });
    }

    void ajakTanding(final DataItemTeam team) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajak tanding ?");
        String message = team.getNama() + "\n" + team.getLapang().getNama();
        builder.setMessage(message);
        builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postTanding(team);
                finish();
            }
        }).setNegativeButton("KEMBALI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void postTanding(DataItemTeam team) {
        DataItem pertandingan = new DataItem();
        pertandingan.setIdTeamHome(id_team);
        pertandingan.setIdTeamAway(team.getId());
        pertandingan.setStatus("Pending");
        futsalApi.postPertandingan(pertandingan).enqueue(new Callback<DataItem>() {
            @Override
            public void onResponse(Call<DataItem> call, Response<DataItem> response) {
                if (response.code() == 200) {
                    Toast.makeText(TimByLapangActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataItem> call, Throwable t) {

            }
        });
    }
}
