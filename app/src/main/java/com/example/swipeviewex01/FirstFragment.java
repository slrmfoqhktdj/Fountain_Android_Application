package com.example.swipeviewex01;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class FirstFragment extends Fragment  {

    private CustomAdapter mAdapter;


    private MainService ms;
    protected boolean isService = false;

    private Intent foregroundServiceIntent;


    private AppDatabase db;

    private String name;
    private String Myid;

    protected String[] user00={"n","n","n","n"};
    protected String[] recivedata = {"n","n","n","n","n","n"};


    private ScrollView scrollView;

    private String theme;
    private ThemeAdapter ta;

    protected AES256Chiper aes256Chiper;

//    CustomLoadingDialog customProgressDialog;

    private CustomSnackbar customSnackbar;

    private List<Todo> items = new ArrayList<>();

    private Context mContext;

//    Disposable backgroundTask;
    //    언데드 서비스 컨넥터
private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MainService.MyBinder mb = (MainService.MyBinder) iBinder;
            ms = mb.getService();
            isService = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isService = false;
            String tag = "system";
            Log.e(tag,"서비스 연결 끊김");
        }
    };

    private final BroadcastReceiver rBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            regist_Yes();
        }
    };

    private final BroadcastReceiver rBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            regist_No();
        }
    };

    private final BroadcastReceiver rBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            regist_reply_App_Yes(intent.getStringExtra("recivedata0"),intent.getStringExtra("recivedata1"),intent.getStringExtra("recivedata2"),intent.getStringExtra("recivedata3"),intent.getStringExtra("recivedata4"));
        }
    };

    private final BroadcastReceiver rBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            regist_reply_App_No(intent.getStringExtra("user000"),intent.getStringExtra("user001"),intent.getStringExtra("user002"),intent.getStringExtra("user003"));
        }
    };

    private final BroadcastReceiver rBroadcastReceiver5 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            regist_reply2_App_Yes(intent.getStringExtra("recivedata0"),intent.getStringExtra("recivedata1"),intent.getStringExtra("recivedata2"),intent.getStringExtra("recivedata3"),intent.getStringExtra("recivedata4"));
        }
    };

    private final BroadcastReceiver rBroadcastReceiver6 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            regist_reply2_App_No();
        }
    };



    public static FirstFragment newInstance(String theme) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString("theme",theme);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //서비스 실행여부
        foregroundServiceIntent = new Intent(getContext(), MainService.class);
        getActivity().bindService(foregroundServiceIntent,conn, Context.BIND_AUTO_CREATE);

        mContext = getActivity();

        //데이터 베이스 생성 및 연결
        db = AppDatabase.getInstance(getActivity().getApplicationContext());


        //테마
        theme = getArguments().getString("theme");
        ta = new ThemeAdapter();

        //로딩
//        customProgressDialog = new CustomLoadingDialog(mContext);

//        Myid = helper.findid();
        SharedPreferencesAdapter spa = SharedPreferencesAdapter.getInstance();
        Myid = spa.getItem("loginid");

        //암호화
        aes256Chiper = new AES256Chiper();

        //커스텀 토스트메시지
        customSnackbar = new CustomSnackbar();



        IntentFilter rfilter1 = new IntentFilter();
        rfilter1.addAction("regist_Yes");
        getActivity().registerReceiver(rBroadcastReceiver1,rfilter1);

        IntentFilter rfilter2 = new IntentFilter();
        rfilter2.addAction("regist_No");
        getActivity().registerReceiver(rBroadcastReceiver2,rfilter2);

        IntentFilter rfilter3 = new IntentFilter();
        rfilter3.addAction("regist_reply_App_Yes");
        getActivity().registerReceiver(rBroadcastReceiver3,rfilter3);

        IntentFilter rfilter4 = new IntentFilter();
        rfilter4.addAction("regist_reply_App_No");
        getActivity().registerReceiver(rBroadcastReceiver4,rfilter4);

        IntentFilter rfilter5 = new IntentFilter();
        rfilter5.addAction("regist_reply2_App_Yes");
        getActivity().registerReceiver(rBroadcastReceiver5,rfilter5);

        IntentFilter rfilter6 = new IntentFilter();
        rfilter6.addAction("regist_reply2_App_No");
        getActivity().registerReceiver(rBroadcastReceiver6,rfilter6);



    }

    // Inflate the view for the fragment based on layout XML
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = ta.FragmentThemeSelector(getActivity(), theme, inflater).inflate(R.layout.fragment_first, container, false);
        ImageView addButton = view.findViewById(R.id.add);
        RecyclerView mRecyclerView = view.findViewById(R.id.main_recyclerview_list);
        mRecyclerView.setItemAnimator(null);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new CustomAdapter(getActivity(),db,Myid,theme);
        mRecyclerView.setAdapter(mAdapter);


        //화면이 다 뜨면 로딩 풀어줌
//        backgroundTask = Observable.fromCallable(() -> {
//            //doInBackground(task에서 실행할 코드 여기에 작성)

        //테마
        scrollView = view.findViewById(R.id.firstFragmentScrollview);
        ta.FirstFragmentThemeSelector(getContext(),theme,scrollView);

//            return false;
//
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((result)-> {
//            //onPostExecute(task 끝난 후 실행될 코드 여기에 작성)
//
//            Intent intent = new Intent("Loading");
//            mContext.sendBroadcast(intent);
//
//
//            backgroundTask.dispose();
//        });


        //아이템추가
        addButton.setOnClickListener(v -> {
            //다이얼로그 생성
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
            final AlertDialog ad = builder.create();
            //다이얼로그 이름
            builder.setTitle("기기이름을 기입해주세요");
            //다이얼로그에 텍스트 추가
            final EditText nameText = new EditText(mContext);
            if(theme.equals("1")){
                nameText.setTextColor(Color.WHITE);
            }
            builder.setView(nameText);
            builder.setCancelable(true);
            //다이얼로그에 버튼 추가와 클릭이벤트
            builder.setPositiveButton("추가하기", (dialogInterface, i) -> {
                name = nameText.getText().toString().trim();
                boolean overlap = true;
                if(!(name.equals(""))){
                    for(int j = 0; j<items.size();j++){
                        if(items.get(j).getDevice().equals(name)){
                            overlap = false;
                        }
                    }
                    if(overlap){

                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("id",Myid);
                            obj.put("dv",name);
                            ms.socket.emit("regist",obj );
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
//                        ms.socket.emit("regist", Myid + "##" + name);

//                        customProgressDialog.show();
                    }else {
                        customSnackbar.toast("등록된 기기 중 동일한 기기가 이미 존재합니다",scrollView);
                    }
                }
                else{
                    customSnackbar.toast("기기의 이름을 기입해주세요",scrollView);
                }
                ad.dismiss();
            });
            builder.show();

        });

        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view ,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);


        db.todoDao().getAll(Myid).observe(getViewLifecycleOwner(), todos -> {
//            customProgressDialog.show();
            mAdapter.selectItem(todos);
            items = todos;
//            customProgressDialog.dismiss();
        });

    }


    private void regist_Yes(){
        Log.e("message1","data");
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",Myid);
            obj.put("dv",name);
            ms.socket.emit("regist_reply_App",obj );
        }catch (JSONException e){
            e.printStackTrace();
        }
//        ms.socket.emit("regist_reply_App",Myid+"##"+name);
    }

    private void regist_No(){
        Log.e("message2","data");
        customSnackbar.toast("'존재하지 않는 기기'이거나 '미등록 기기'입니다. 다시 확인해 주세요",scrollView);
//        customProgressDialog.dismiss();
    }

    private void regist_reply_App_Yes(String recivedata0,String recivedata1,String recivedata2,String recivedata3,String recivedata4){

        recivedata[0] = recivedata0;
        recivedata[1] = recivedata1;
        recivedata[2] = recivedata2;
        recivedata[3] = recivedata3;
        recivedata[4] = recivedata4;

        Log.e("message3","data"+recivedata[0]+" "+recivedata[1]+" "+recivedata[2]+" "+recivedata[3]+" "+recivedata[4]);


        db.todoDao().insert(Myid,name,name,recivedata[0],recivedata[1],recivedata[2],recivedata[3],recivedata[4]).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
//        customProgressDialog.dismiss();

//        db.todoDao().insert(new Todo(Myid,name,name,recivedata[0],recivedata[1],recivedata[2],recivedata[3],recivedata[4],recivedata[5])).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();

    }

    private void regist_reply_App_No(String user000,String user001,String user002,String user003){

        user00[0] = user000;
        user00[1] = user001;
        user00[2] = user002;
        user00[3] = user003;


        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
        final AlertDialog ad = builder.create();
        builder.setTitle("알림");
        builder.setMessage("서버 내 기기에 등록된 사용자가 많습니다. 등록된 사용자 한명을 선택 삭제후 기기에 등록하시겠습니까? ");
        builder.setCancelable(false);
        builder.setPositiveButton("확인", (dialogInterface, i) -> {

            final int[] SelectedItem = {0};
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext,(theme.equals("1")?R.style.DarkAlertDialog:R.style.AlertDialog02));
            final AlertDialog ad1 = builder1.create();
            builder1.setTitle("알림");
            builder1.setCancelable(false);
            builder1.setSingleChoiceItems(user00,0, (dialogInterface1, i1) -> SelectedItem[0]= i1);
            builder1.setPositiveButton("확인", (dialogInterface12, i12) -> {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id",Myid);
                    obj.put("dv",name);
                    obj.put("su",user00[SelectedItem[0]]);
                    ms.socket.emit("regist_reply2_App",obj );
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                ms.socket.emit("regist_reply2_App", Myid + "##" + name + "##" + user00[SelectedItem[0]]);
            });
            builder1.setNeutralButton("취소", (dialogInterface13, i13) -> ad1.dismiss());
            builder1.show();
        });
        builder.setNeutralButton("취소", (dialogInterface, i) -> ad.dismiss());
        builder.show();

    }

    private void regist_reply2_App_Yes(String recivedata0,String recivedata1,String recivedata2,String recivedata3,String recivedata4){

        recivedata[0] = recivedata0;
        recivedata[1] = recivedata1;
        recivedata[2] = recivedata2;
        recivedata[3] = recivedata3;
        recivedata[4] = recivedata4;



        db.todoDao().insert(Myid,name,name,recivedata[0],recivedata[1],recivedata[2],recivedata[3],recivedata[4]).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();

//        db.todoDao().insert(new Todo(Myid,name,name,recivedata[0],recivedata[1],recivedata[2],recivedata[3],recivedata[4],recivedata[5])).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private void regist_reply2_App_No(){
        customSnackbar.toast("기기 등록에 실패했습니다. 잠시 후 시도해주세요",scrollView);
//        customProgressDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        Log.e("firstfragment","onDestroyView");
        super.onDestroyView();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("firstfragment","onDestroy");
        try{
            getActivity().unregisterReceiver(rBroadcastReceiver1);
            getActivity().unregisterReceiver(rBroadcastReceiver2);
            getActivity().unregisterReceiver(rBroadcastReceiver3);
            getActivity().unregisterReceiver(rBroadcastReceiver4);
            getActivity().unregisterReceiver(rBroadcastReceiver5);
            getActivity().unregisterReceiver(rBroadcastReceiver6);
            getActivity().unbindService(conn);
            foregroundServiceIntent =null;
            if(mContext != null){
                mContext = null;
            }
            customSnackbar = null;
//            customProgressDialog = null;
            conn =null;
            db = null;
            ms = null;
            mAdapter =null;
//            if(!backgroundTask.isDisposed()){
//                backgroundTask.dispose();
//                backgroundTask = null;
//            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}