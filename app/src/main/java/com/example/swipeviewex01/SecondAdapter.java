package com.example.swipeviewex01;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.SecondViewHolder> {

    private List<Picture> sitems;
    private final Context mContext;

    private AppDatabase db;


    protected String theme;
    protected ThemeAdapter ta;

    private AlertDialogAdapter ada;


    private Disposable backgroundTask;

//    ControlPicture cp;

    private CustomSnackbar customSnackbar;
    private int count = 0;
    protected LinearLayout mainview;


//    public class SecondViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
    public class SecondViewHolder extends RecyclerView.ViewHolder {
    protected TextView redevice;
    protected ImageView imgview;
    protected TextView igdownload;
    protected TextView time;
    protected LinearLayout main;
    protected TextView imgshow;

    public SecondViewHolder(View view) {
        super(view);
        this.redevice = view.findViewById(R.id.second_redevicedata);
        this.imgview = view.findViewById(R.id.second_imgView);
        this.igdownload = view.findViewById(R.id.second_download);
        this.time = view.findViewById(R.id.second_time);
        this.main = view.findViewById(R.id.secondlist_main);
        this.imgshow = view.findViewById(R.id.second_show);

        //데이터 베이스 생성 및 연결
        db = AppDatabase.getInstance(mContext);

        ta = new ThemeAdapter();



//        cp = new ControlPicture(mContext);

        ada = new AlertDialogAdapter(mContext,theme);

        customSnackbar = new CustomSnackbar();

        igdownload.setOnClickListener(v -> {
            int position = getBindingAdapterPosition();
            if(sitems.get(position).getType().split("/")[0].equals("P")) {
                //구현하고 풀기
                try {
                    saveImageFromUrl(mContext.getString(R.string.app_pictureservice)+"/uploads/" + sitems.get(position).getFile() + ".jpg");
                }catch (Exception e){
                    e.printStackTrace();
                }
                //테스트용
//                saveImageFromUrl("http://192.168.0.4:3006/images/ju.jpg");

                igdownload.setTextColor(Color.parseColor("#A50083"));
            }
        });

        imgshow.setOnClickListener(v -> {
            int position = getBindingAdapterPosition();
            if(sitems.get(position).getType().split("/")[0].equals("P")){
                if(count == 0){
                    try {
                        Glide.with(mContext).load(mContext.getString(R.string.app_pictureservice) + "/uploads/" + sitems.get(position).getFile() + ".jpg").error(R.drawable.pictureerror).placeholder(R.drawable.ic_baseline_settings_24).into(imgview);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //                    Glide.with(mContext).load("http://192.168.0.4:3006/images/ju.jpg").error(R.drawable.pictureerror).into(imgview);
//                    imgshow.setText("이미지 닫기");
                    count = 1;
                }else{
                    imgview.setImageBitmap(null);
//                    imgshow.setText("이미지 보기");
                    count = 0;
                }
                imgshow.setTextColor(Color.parseColor("#A50083"));

            }
        });

        main.setOnLongClickListener(v -> {
            int position = getBindingAdapterPosition();
            ada.SecondAdapterLongClick(db, sitems.get(position).getDevice(), sitems.get(position).getTime(),imgview,imgshow,sitems.get(position).getType());
            return true;
        });

        imgview.setOnClickListener(v -> {
            int position = getBindingAdapterPosition();
            Intent intent = new Intent(mContext, SecondIMGClickPage.class);
            intent.putExtra("url", mContext.getString(R.string.app_pictureservice)+"/uploads/" + sitems.get(position).getFile() + ".jpg");
//            intent.putExtra("url", "http://192.168.0.4:3006/images/ju.jpg");
            mContext.startActivity(intent);

        });

    }

    public void onBind(Picture picture) {
        try{
            redevice.setText(picture.getRedevice());
            time.setText(picture.getTime());
            String[] type = picture.getType().split("/");
            switch (type[0]){
                case "P":
                    imgshow.setText("이미지 보기");

                    
                    igdownload.setText("이미지 다운로드 하기");
                    break;
                case "I-W":
                    imgshow.setText("소독액양이 "+type[1]+"% 로 변화하였습니다.");
                    break;
                case "I-N":
                    imgshow.setText("기기가 켜졌습니다.");
                    break;
                case "I-N-E":
                    imgshow.setText("기기가 켜지는데 오류가 발생했습니다.");
                    break;
                case "I-F":
                    imgshow.setText("기기가 꺼졌습니다.");
                    break;
                case "I-F-E":
                    imgshow.setText("기기가 꺼지는데 오류가 발생했습니다.");
                    break;
                case "I-M":
                    switch (type[1]){
                        case "0":
                            imgshow.setText("모드가 오토 모드로 바뀌었습니다.");
                            break;
                        case "1":
                            imgshow.setText("모드가 커스텀 모드로 바뀌었습니다.");
                            break;
                        default:
                            imgshow.setText("모드가 홈으로 바뀌었습니다.");
                            break;
                    }
                    break;
                case "I-S":
                    imgshow.setText("강도가 "+type[1]+"로 바뀌었습니다.");
                    break;
                case "I-U":
                    imgshow.setText("기기가 대기상태입니다.");
                    break;
                default:
                    imgshow.setText("네트워크에 오류가 생겼습니다.");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }






        //시간으로 제목을 만든뒤 풀어야함!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        Glide.with(mContext).load(cp.getBitmapFromCacheDir(mContext, sitems.get(position).getTime())).into(imgview);




    }
}

    public SecondAdapter(Context context,AppDatabase db,String theme,LinearLayout mainview){
        mContext = context;
        this.db = db;
        this.theme = theme;
        this.mainview = mainview;
    }


    @NonNull
    @Override
    public SecondAdapter.SecondViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_secondlist,viewGroup,false);
//        SecondAdapter.SecondViewHolder viewHolder = new SecondAdapter.SecondViewHolder(view);
        return new SecondAdapter.SecondViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull SecondAdapter.SecondViewHolder viewholder, final int position) {

        viewholder.onBind(sitems.get(position));

    }



    @Override
    public int getItemCount() {
        return (null!= sitems? sitems.size() : 0);
    }

    @Override
    public long getItemId(int position){
        super.getItemId(position);
        return position;
    }

    @Override
    public int getItemViewType(int postion){
        return postion;
    }

    void selectItem(List<Picture> pictures){
        sitems = pictures;
        notifyDataSetChanged();
    }

    public Picture getItem(int position){
        return sitems.get(position);
    }


    /* 백그라운드에서 서버 이미지 Url로 기기에 저장하는 메소드 */
    private void saveImageFromUrl(String imageUrl) {
        backgroundTask = Observable.fromCallable(() -> {
            // Glide를 이용해서 서버의 Url로부터 이미지 파일 반환

            return Glide.with(mContext).asFile().load(imageUrl).error(R.drawable.pictureerror).submit().get();

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(file -> {
            // 이미지 파일 갤러리에 저장
            saveFile(file, getImageMimeType(imageUrl), getImageName(imageUrl));
            backgroundTask.dispose();
        });
    }

    public void saveFile(@NonNull final File file, @NonNull final String mimeType, @NonNull final String displayName) throws IOException {
        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // IS_PENDING을 1로 설정해놓으면, 현재 파일을 업데이트 전까지 외부에서 접근하지 못하도록 할 수 있다.
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        final ContentResolver resolver = mContext.getContentResolver();
        Uri uri = null;

        try {
            final Uri contentUri;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                // 외부저장소-Downloads에 저장하고 싶을 때(Q 이상)
//                contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
//            } else {
                // 외부저장소-Pictures에 저장하고 싶을 때
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

//            }
            uri = resolver.insert(contentUri, values);


            if(uri == null) {
                    throw new IOException("Failed to create new MediaStore record.");
                }else{

                try (final OutputStream stream = resolver.openOutputStream(uri)) {
                    if(stream == null) {
                        throw new IOException("Failed to open output stream.");
                    }
                    // 파일을 바이트배열로 변환 후, 파일에 저장
                    byte[] bArray = convertFileToByteArray(file);
                    stream.write(bArray);
                    stream.flush();
                    stream.close();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        values.clear();
                        // 파일 저장이 완료되었으니, IS_PENDING을 다시 0으로 설정한다.
                        values.put(MediaStore.Images.Media.IS_PENDING, 0);
                        // 파일을 업데이트하면, 파일이 보인다.
                        resolver.update(uri, values, null, null);
                    }
                    customSnackbar.toast("사진이 저장되었습니다.",mainview);
                }catch (Exception e){
                    customSnackbar.toast("사진을 저장할 수 없습니다.",mainview);
                    e.printStackTrace();
                }

            }

        }
        catch (IOException e) {
            customSnackbar.toast("사진을 저장할 수 없습니다.",mainview);
            e.printStackTrace();
            if (uri != null) {
                resolver.delete(uri, null, null);
            }
            if(!backgroundTask.isDisposed()) {
                backgroundTask.dispose();
            }
            throw e;
        }
    }

    /* 파일을 바이트배열로 변환해주는 메소드 */
    private static byte[] convertFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating bytearray of same length as file
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            // Reading file content to byte array
            fis.read(bArray);
            fis.close();
        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bArray;
    }

    /* mimeType 반환 */
    private String getImageMimeType(String imageUrl) {
        String[] type_arr = imageUrl.split("\\.");
        if(type_arr[(type_arr.length-1)].toLowerCase().equals("jpg") ) {
            return "image/jpeg";
        } else {
            return "image/"+type_arr[(type_arr.length-1)].toLowerCase();
        }
    }
    /* 파일 이름 반환하는 메소드 */
    private String getImageName(String imageUrl) {
        String[] name_1 = imageUrl.split("/");
        // 확장자 포함된 이름 반환
        return name_1[(name_1.length-1)];
    }

}
