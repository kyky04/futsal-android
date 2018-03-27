package uinbdg.skirpsi.futsal.Adapter;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uinbdg.skirpsi.futsal.Model.DataItemLapangan;
import uinbdg.skirpsi.futsal.R;
import uinbdg.skirpsi.futsal.Util.GPSTracker;

/**
 * Created by Comp on 2/11/2018.
 */

public class AdapterLapangan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DataItemLapangan> lapanganList;
    GPSTracker gpsTracker;



    private OnLoadMoreListener onLoadMoreListener;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    @OnClick(R.id.tv_jumlah_team)
    public void onViewClicked() {
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterLapangan(Context ctx) {
        this.ctx = ctx;
    }

    public AdapterLapangan(Context ctx, List<DataItemLapangan> lapanganResponses) {
        this.lapanganList = lapanganResponses;
        this.ctx = ctx;
        gpsTracker = new GPSTracker(ctx);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_nama_lapang)
        TextView tvNamaLapang;
        @BindView(R.id.et_alamat)
        EditText etAlamat;
        @BindView(R.id.et_phone)
        EditText etPhone;
        @BindView(R.id.view_item)
        LinearLayout viewItem;
        @BindView(R.id.et_distance)
        EditText etDistance;
        @BindView(R.id.tv_jumlah_team)
        TextView tvJumlahTeam;

        public OriginalViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lapangan, parent, false);
        vh = new OriginalViewHolder(v);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.d("LAPANGAN", lapanganList.get(position).getNama());
        DataItemLapangan lapang = lapanganList.get(position);
        if (holder instanceof OriginalViewHolder) {
            ((OriginalViewHolder) holder).tvNamaLapang.setText(lapang.getNama());
//            ((OriginalViewHolder) holder).etAlamat.setText(lapang.x());

            String distance = String.valueOf(getDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), lapanganList.get(position).getLatitude(), lapanganList.get(position).getLongitude()) / 1000) + " Km";
//            Integer jarak = Integer.valueOf(distance);
            ((OriginalViewHolder) holder).etDistance.setText(String.valueOf(distance));
            ((OriginalViewHolder) holder).tvJumlahTeam.setText("Jumlah Tim : "+lapanganList.get(position).getDataItemTeamList().size());
        }

        ((OriginalViewHolder) holder).viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lapanganList.size();
    }

    public static float getDistance(double startLati, double startLongi, double goalLati, double goalLongi) {
        float[] resultArray = new float[99];
        Location.distanceBetween(startLati, startLongi, goalLati, goalLongi, resultArray);
        return resultArray[0];
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

}
