package kr.o3selab.smartbike.fragment;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import kr.o3selab.smartbike.DB;
import kr.o3selab.smartbike.R;

/**
 * Created by duveen on 2016-05-03.
 */
public class ControlLedFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    private Switch ledConnect;

    private RadioButton ledDefault;
    private RadioButton ledLong;
    private RadioButton ledShort;
    private RadioButton ledUser;

    BluetoothSocket mmSocket;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    /**
     * Fragment가 생성되었을때 실행되는 콜백 메소드
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content_led_connector, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("LED 설정");

        // 객체 로더
        ledConnect = (Switch) v.findViewById(R.id.led_switch);

        ledDefault = (RadioButton) v.findViewById(R.id.led_default);
        ledLong = (RadioButton) v.findViewById(R.id.led_long);
        ledShort = (RadioButton) v.findViewById(R.id.led_short);
        ledUser = (RadioButton) v.findViewById(R.id.led_user);

        // 초기상태 체크
        if(DB.ledStatus) {
            ledConnect.setChecked(true);
            statusChangeChecked();
        } else {
            ledConnect.setChecked(false);
            statusChangeChecked();
        }

        ledConnect.setOnCheckedChangeListener(this);

        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.led_group);
        radioGroup.setOnCheckedChangeListener(this);

        return v;
    }

    /**
     * Switch Control on/off시 실행되는 메소드
     * created by duveen
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Toast.makeText(this, "" + isChecked, Toast.LENGTH_SHORT).show();
        if(isChecked) {
            if(DB.mmDevice == null) {
                Toast.makeText(getActivity(), "센서 연결을 먼저 실시해주세요!", Toast.LENGTH_SHORT);
                return;
            }
            try {
                mmSocket = DB.mmDevice.createRfcommSocketToServiceRecord(uuid);
                mmSocket.connect();
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();
            } catch (IOException e) {
                Toast.makeText(getActivity(), "IO 예외", Toast.LENGTH_SHORT).show();
            }
            DB.ledStatus = true;
            statusChangeChecked();
        } else {
            DB.ledStatus = false;
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getActivity(), "IO 예외", Toast.LENGTH_SHORT).show();
            }
            statusChangeChecked();
        }

    }

    /**
     * 상태가 변경 되었을 때 활성화 비활성화 체크
     */
    public void statusChangeChecked() {
        if(ledConnect.isChecked()) {
            ledDefault.setEnabled(true);
            ledLong.setEnabled(true);
            ledShort.setEnabled(true);
            ledUser.setEnabled(true);
        } else {
            ledDefault.setEnabled(false);
            ledLong.setEnabled(false);
            ledShort.setEnabled(false);
            ledUser.setEnabled(false);
        }

        if(DB.ledDefault) ledDefault.setChecked(true);
        else if(DB.ledLong) ledLong.setChecked(true);
        else if(DB.ledShort) ledShort.setChecked(true);
        else if(DB.ledUser) ledUser.setChecked(true);
    }

    /**
     * 라디오그룹에 있는 라디오 버튼을 클릭시 수행되는 이벤트
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.led_default:
                dbCheckStatus(checkedId);
                // Toast.makeText(getActivity(), "기본" + checkedId, Toast.LENGTH_SHORT).show();
                break;
            case R.id.led_long:
                dbCheckStatus(checkedId);
                // Toast.makeText(getActivity(), "길게" + checkedId, Toast.LENGTH_SHORT).show();
                break;
            case R.id.led_short:
                dbCheckStatus(checkedId);
                // Toast.makeText(getActivity(), "짧게" + checkedId, Toast.LENGTH_SHORT).show();
                break;
            case R.id.led_user:
                dbCheckStatus(checkedId);
                // Toast.makeText(getActivity(), "사용자 패턴" + checkedId, Toast.LENGTH_SHORT).show();
                break;
        }
    }



    public void dbCheckStatus(int checkedId) {
        String line = null;
        switch(checkedId) {
            case R.id.led_default:
                line = "D";
                DB.ledDefault = true;
                DB.ledLong = false;
                DB.ledShort = false;
                DB.ledUser = false;
                break;
            case R.id.led_long:
                line = "L";
                DB.ledDefault = false;
                DB.ledLong = true;
                DB.ledShort = false;
                DB.ledUser = false;
                break;
            case R.id.led_short:
                line = "S";
                DB.ledDefault = false;
                DB.ledLong = false;
                DB.ledShort = true;
                DB.ledUser = false;
                break;
            case R.id.led_user:
                line = "U";
                DB.ledDefault = false;
                DB.ledLong = false;
                DB.ledShort = false;
                DB.ledUser = true;
                break;
        }

        line = line + "\n";
        try {
            mmOutputStream.write(line.getBytes());
        } catch (IOException e) {

        }

    }
    /**
     * Fragment가 BackStack에서 돌아왔을때 실행되는 콜백 메소드
     */
    @Override
    public void onResume() {

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("LED 설정");

        super.onResume();
    }
}
