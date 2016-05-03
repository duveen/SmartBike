package kr.o3selab.smartbike.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import kr.o3selab.smartbike.DB;
import kr.o3selab.smartbike.R;

/**
 * SpeedMeterFragment 화면
 */
public class SpeedMeterFragment extends Fragment implements View.OnClickListener {

    private BitmapDrawable startImage;
    private BitmapDrawable stopImage;
    private ImageView ridingStatusImageView;

    /**
     * Fragment가 생성되었을때 실행되는 콜백 메소드
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_content_speed_meter, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("속도계");

        ridingStatusImageView = (ImageView) v.findViewById(R.id.start_stop_button);

        startImage = (BitmapDrawable) getResources().getDrawable(R.drawable.start_button);
        stopImage = (BitmapDrawable) getResources().getDrawable(R.drawable.stop_button);

        imageSetter();

        ridingStatusImageView.setOnClickListener(this);

        return v;
    }

    /**
     * Fragment가 BackStack에서 돌아왔을때 실행되는 콜백 메소드
     */
    @Override
    public void onResume() {

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("속도계");

        super.onResume();
    }

    /**
     * Riding 버튼을 클릭했을때 실행되는 메소드
     * @param v 실행중인 View의 정보
     */
    @Override
    public void onClick(View v) {
        if(!DB.ridingStatus) {
            // 라이딩 시작전
            DB.ridingStatus = true;
            Toast.makeText(getActivity(), "라이딩을 시작합니다!", Toast.LENGTH_SHORT).show();

        } else {
            // 라이딩 시작후
            DB.ridingStatus = false;
            Toast.makeText(getActivity(), "라이딩을 종료합니다!", Toast.LENGTH_SHORT).show();

        }

        imageSetter();
    }

    /**
     * 현재 라이딩 상태를 확인하여 실행중일 경우 실행이미지 적용
     */
    public void imageSetter() {
        if(DB.ridingStatus)    ridingStatusImageView.setImageDrawable(stopImage);
        else                   ridingStatusImageView.setImageDrawable(startImage);

    }
}