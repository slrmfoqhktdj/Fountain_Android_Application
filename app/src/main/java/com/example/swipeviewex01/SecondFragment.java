package com.example.swipeviewex01;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SecondFragment  extends Fragment {

    private SecondAdapter sAdapter;
    private AppDatabase db;

    private String Myid;

    private String theme;
    protected ThemeAdapter ta;
    protected SharedPreferencesAdapter spa;

    private RecyclerView sRecyclerView;

    private List<Picture> slist;

    private Context mContext;


    private int focus = 0;
    private boolean recyclerviewlastfocus = true;

    public static SecondFragment newInstance(String theme) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString("theme",theme);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("SecondFragment","onCreate");

        mContext = getContext();

        //데이터 베이스 생성 및 연결
        db = AppDatabase.getInstance(mContext);
        //테마
        theme = getArguments().getString("theme");


        spa = SharedPreferencesAdapter.getInstance();

        Myid = spa.getItem("loginid");
        ta = new ThemeAdapter();

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("SecondFragment","onCreateView");
        //테마적용
        View view = ta.FragmentThemeSelector(getActivity(), theme, inflater).inflate(R.layout.fragment_second, container, false);

        sRecyclerView = view.findViewById(R.id.second_recyclerview_list);
        LinearLayout main = view.findViewById(R.id.second_main);
        sRecyclerView.setItemAnimator(null);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        sRecyclerView.setLayoutManager(mLinearLayoutManager);
        sAdapter = new SecondAdapter(getActivity(),db,theme,main);
        sRecyclerView.setAdapter(sAdapter);


        sRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                // focus가 list의 끝이면 true,아니면 false 반환
                recyclerviewlastfocus = lastVisibleItemPosition == itemTotalCount;
            }
        });




        //테마
//        ta.SecondFragmentThemeSelector(getActivity(), theme, showname_ll, waterstate_ll, start_ll, error_ll, vpPager);
        return view;
    }


    @Override
    public void onViewCreated(@Nullable View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db.todoDao().pgetAll(Myid).observe(getViewLifecycleOwner(), pictures -> {
            slist = pictures;
            sAdapter.selectItem(pictures);
            //일정 시간 지난 목록 삭제하기


            removeDateList();


            //view focus가 secondfragment 일때 메시지가 와도 recyclerview를 아래로 내리지 않기
            if(focus == 0){
                //어플을 처음 시작할때 맨 아랫단 보여주기
                sRecyclerView.scrollToPosition(sAdapter.getItemCount()-1);
            }else{
                if(!recyclerviewlastfocus){
                    int lastposition = pictures.size()-1;
                    customsnackbar(view,pictures.get(lastposition).getDevice()+"  "+snackbarmassage(pictures.get(lastposition).getType()));
                }else {
                    sRecyclerView.scrollToPosition(sAdapter.getItemCount()-1);
                }
            }
            Log.e("SecondFragment","pgetAll"+" focus : "+focus + " scrollstate : "+sRecyclerView.getScrollState());
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        //화면상태 -켜짐
        Log.e("SecondFragment","onResume");
        focus = 1;


    }

    @Override
    public void onStart(){
        super.onStart();
        Log.e("SecondFragment","onStart");

    }

    @Override
    public void onPause(){
        super.onPause();
        //화면상태 -꺼짐
        Log.e("SecondFragment","onPause");
        focus = 0;
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
        db = null;

    }

    private String snackbarmassage(String type){
        String result;
        String[] types = type.split("/");
        switch (types[0]){
            case "P":
                result = "이미지 보기";
                break;
            case "I-W":
                result = "소독액양이 "+types[1]+"% 로 변화하였습니다.";
                break;
            case "I-N":
                result = "기기가 켜졌습니다.";
                break;
            case "I-N-E":
                result = "기기가 켜지는데 오류가 발생했습니다.";
                break;
            case "I-F":
                result = "기기가 꺼졌습니다.";
                break;
            case "I-F-E":
                result = "기기가 꺼지는데 오류가 발생했습니다.";
                break;
            case "I-D":
                result = "경고 메시지가 왔습니다.";
                break;
            case "I-U":
                result = "기기가 대기상태입니다.";
                break;
            default:
                result = "네트워크에 오류가 생겼습니다.";
                break;
        }
        return result;
    }

    // 스낵바
    private void customsnackbar(View view,String massage){
        Snackbar snackbar;
        snackbar = Snackbar.make(view,massage,3000);

        View snackView = snackbar.getView();
        TextView snackText = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackText.setTextColor(Color.BLACK);
        snackText.setTextSize(15);
        snackText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackView.setBackgroundColor(Color.WHITE);
        
        // 스낵바 클릭이벤트
        snackView.setOnClickListener(v -> {
            sRecyclerView.scrollToPosition(sAdapter.getItemCount()-1);
            snackbar.dismiss();
            Log.e("secondfragment","click snackbar");
        });
        snackbar.show();
    }


    private void removeDateList(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String time = sdf.format(date);
        try{
            String preDate = spa.getItem("preDate");
            
            if(preDate.equals("")){
                spa.setItem("preDate",time);
                Log.e("SecondFragment","predate is null");
            }else{
                /// 하루에 한번만 리스트를 훑어 삭제하기
                long diffInMillies = Math.abs(sdf.parse(preDate).getTime() - sdf.parse(time).getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                Log.e("SecondFragment","predate is not null");
                if(diff>0){
                    for(int i = 0;i<slist.size();i++){

                        Log.e("SecondFragment","slist"+slist.get(i).getTime());
                        //db에 있는 한달의 시간이 지나버린 목록을 지워야함
                        //캐쉬에 있는 해당 사진들도 지워야함+

                        long diffInMillies02 = Math.abs(sdf.parse(time).getTime() - sdf.parse(slist.get(i).getTime()).getTime());


                        //두 날짜의 날 수 차이
                        long diff02 = TimeUnit.DAYS.convert(diffInMillies02, TimeUnit.MILLISECONDS);
                        Log.e("SecondFragment", String.valueOf(diff02));

                        // 지정된 날짜가 차면 옛날 것 삭제
                        if(diff02>0){
                            db.todoDao().pdelete(slist.get(i).getDevice(),slist.get(i).getTime()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                        }
                    }
                }
            }
            
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            spa.setItem("preDate",time);
        }

    }


}
