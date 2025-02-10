package com.example.swipeviewex01;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private final Context mContext;
    private List<Todo> items = new ArrayList<>();
    private final AppDatabase db;
    private final String Myid;

    protected String theme;
    private ThemeAdapter ta;

    private AlertDialogAdapter ada;





    //    View.OnClickListener,View.OnLongClickListener
//    View.OnCreateContextMenuListener
    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        protected TextView devicename;
        protected TextView waterdata;
        protected ImageView onoff;
        protected ImageView error;
        protected LinearLayout main;


        protected CustomViewHolder(View view) {
            super(view);
            this.devicename = view.findViewById(R.id.customlist_Title);
            this.waterdata = view.findViewById(R.id.customlist_water);
            this.onoff = view.findViewById(R.id.customlist_onoff);
            this.error = view.findViewById(R.id.customlist_error);
            this.main = view.findViewById(R.id.customlist_main);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
//            view.setOnCreateContextMenuListener(this);



            ta = new ThemeAdapter();
            ada = new AlertDialogAdapter(mContext,theme);

        }

        protected void onBind(Todo todo){
            devicename.setText(todo.getRedevice());
            waterdata.setText(todo.getWater());
            try{
                if(todo.getOnoff().equals("1")){
                    onoff.setImageResource(R.drawable.onbtn);
                }else{
                    onoff.setImageResource(R.drawable.offbtn);
                }

                if(todo.getError().equals("1")){
                    error.setImageResource(R.drawable.onbtn);
                }else{
                    error.setImageResource(R.drawable.offbtn);
                }

            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }


        @Override
        public void onClick(View view) {

            int position = getBindingAdapterPosition();

            Intent intent = new Intent(mContext,ControlCustom.class);
            intent.putExtra("devicename",items.get(position).getDevice());

            mContext.startActivity(intent);
        }


        @Override
        public boolean onLongClick(View view) {
            ada.CustomAdapterLongClick(devicename,items,Myid,db);
            return true;
        }
    }


    protected CustomAdapter(Context context,AppDatabase db,String Myid,String theme){
        mContext = context;
        this.db = db;
        this.Myid = Myid;
        this.theme = theme;
    }

    @NonNull
    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_customlist,viewGroup,false);
        return  new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        
//        //테마
        ta.CustomAdapterThemeSelector(mContext,theme,viewholder);

        viewholder.onBind(items.get(position));

    }


    @Override
    public int getItemCount() {
        return (null!= items? items.size() : 0);
    }


    protected void selectItem(List<Todo> todo){
        items = todo;
        notifyDataSetChanged();
    }


    protected Todo getItem(int position){
        return items.get(position);
    }

    @Override
    public int getItemViewType(int postion){
        return postion;
    }

}
