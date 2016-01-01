package vimaru.chaunguyen.tracuudiemvimaru;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chau on 12/17/2015.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Item> dataSet;
    public Adapter(ArrayList<Item> items)
    {
        this.dataSet=items;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=new LinearLayout(parent.getContext());
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = dataSet.get(position);
        View v;
        if (item.getClass().getName().equals("vimaru.chaunguyen.tracuudiemvimaru.Kyhoc"))
        {
            TextView tv;
            if((v=holder.view.findViewById(R.id.kihoc_item_layout))==null)
            {
                v = View.inflate(holder.view.getContext(), R.layout.kyhoc_item, null);
                tv = (TextView) v.findViewById(R.id.textViewKihoc);
                tv.setText(item.getName());
                ((LinearLayout) holder.view).removeAllViews();
                ((LinearLayout) holder.view).addView(v);
            }
            else
            {
                tv = (TextView) v.findViewById(R.id.textViewKihoc);
                tv.setText(item.getName());
            }
        }
        else
        {
            TextView tv;
            if((v=holder.view.findViewById(R.id.monhoc_item_layout))==null)
            {
                v = View.inflate(holder.view.getContext(), R.layout.monhoc_item, null);
                tv = (TextView) v.findViewById(R.id.textViewMH);
                tv.setText(item.getName());
                tv = (TextView) v.findViewById(R.id.textViewTCHT);
                tv.setText("TCHT: " + ((Monhoc) item).getTCHT());
                tv = (TextView) v.findViewById(R.id.textViewDX);
                tv.setText("X: " + ((Monhoc) item).getDiemX());
                tv = (TextView) v.findViewById(R.id.textViewDY);
                tv.setText("Y: " + ((Monhoc) item).getDiemY());
                tv = (TextView) v.findViewById(R.id.textViewDZ);
                tv.setText("Z: " + ((Monhoc) item).getDiemZ());
                tv = (TextView) v.findViewById(R.id.textViewDS);
                tv.setText("Điểm số: " + ((Monhoc) item).getDiemso());
                ImageView imageView = (ImageView) v.findViewById(R.id.imageViewDC);
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(((Monhoc) item).getDiemchu(), Color.rgb(204,57,16));
                imageView.setImageDrawable(drawable);
                ((LinearLayout) holder.view).removeAllViews();
                ((LinearLayout) holder.view).addView(v);
            }
            else
            {
                tv = (TextView) v.findViewById(R.id.textViewMH);
                tv.setText(item.getName());
                tv = (TextView) v.findViewById(R.id.textViewTCHT);
                tv.setText("TCHT: " + ((Monhoc) item).getTCHT());
                tv = (TextView) v.findViewById(R.id.textViewDX);
                tv.setText("X: " + ((Monhoc) item).getDiemX());
                tv = (TextView) v.findViewById(R.id.textViewDY);
                tv.setText("Y: " + ((Monhoc) item).getDiemY());
                tv = (TextView) v.findViewById(R.id.textViewDZ);
                tv.setText("Z: " + ((Monhoc) item).getDiemZ());
                tv = (TextView) v.findViewById(R.id.textViewDS);
                tv.setText("Điểm số: " + ((Monhoc) item).getDiemso());
                ImageView imageView = (ImageView) holder.view.findViewById(R.id.imageViewDC);
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(((Monhoc) item).getDiemchu(), Color.rgb(204,57,16));
                imageView.setImageDrawable(drawable);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public View view;

       public ViewHolder(View itemView) {
           super(itemView);
           view=itemView;
       }
   }
}
