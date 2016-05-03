package kr.o3selab.smartbike.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import kr.o3selab.smartbike.DB;
import kr.o3selab.smartbike.R;

/**
 * Created by duveen on 2016-05-03.
 */
public class ConnectSensorFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private ArrayAdapter<String> mArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private ListView listView;
    private Switch bluetoothSwitch;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_connect_sensor, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("센서 연결");

        bluetoothSwitch = (Switch) v.findViewById(R.id.bluetooth_switch);
        mArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        listView = (ListView) v.findViewById(R.id.listView);

        checkBTState();

        return v;
    }

    /**
     * 블루투스 스위치의 상태가 변경되었을때 실행되는 메소드
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            DB.bluetoothStatus = true;
            if(!mBluetoothAdapter.isEnabled()) {
                // 블루투스를 지원하지만 비활성 상태인 경우
                // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요첨
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else {
                // 블루투스를 지원하며 활성 상태인 경우
                // 페어링된 기기 목록을 보여주고 연결할 장치를 선택.
                connectDevice();
            }
        }
        else {
            DB.bluetoothStatus = false;
            listView.setAdapter(null);
            return;
        }
    }

    /**
     * 블루투스 상태 체크
     */
    private void checkBTState() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            // 장치가 블루투스를 지원하지 않는 경우
            Toast.makeText(getActivity(), "이 장치는 블루투스를 지원하지 않습니다!", Toast.LENGTH_SHORT).show();
            bluetoothSwitch.setEnabled(false);
        } else {
            // 장치가 블루투스를 지원하는 경우
            bluetoothSwitch.setOnCheckedChangeListener(this);
        }

        if(DB.bluetoothStatus) {
            bluetoothSwitch.setChecked(true);
        }
    }

    /**
     * 장치 연결
     */
    private void connectDevice() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        for (BluetoothDevice device : pairedDevices) {
            list.add(device.getName() + ", " + device.getAddress());
            if(device.getName().equals("SmartBike")) {
                DB.mmDevice = device;
            }
        }

        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }
}
