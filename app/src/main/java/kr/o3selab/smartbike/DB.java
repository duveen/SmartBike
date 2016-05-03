package kr.o3selab.smartbike;

import android.bluetooth.BluetoothDevice;

/**
 * Created by duveen on 2016-05-02.
 */
public class DB {
    // 각각의 Fragment Tag Value
    public static final String MAIN_TAG = "MAIN_FRAGMENT";
    public static final String SPEED_TAG = "SPEED_METER_FRAGMENT";
    public static final String LED_TAG = "LED_FRAGMENT";
    public static final String ANA_TAG = "MAIN_FRAGMENT";
    public static final String SENSOR_TAG = "CONNECT_SENSOR_FRAGMENT";

    // Fragment 포지션 설정
    public static final int MAIN_FRAG = 1;
    public static final int SPEED_FRAG = 2;
    public static final int LED_FRAG = 3;
    public static final int ANA_FRAG = 4;
    public static final int SENSOR_FRAG = 5;

    // Riding Start Stop Status
    public static boolean ridingStatus = false;

    // LED Connector Status
    public static boolean ledStatus = false;
    public static boolean ledDefault = false;
    public static boolean ledLong = false;
    public static boolean ledShort = false;
    public static boolean ledUser = false;

    // BlueTooth Status
    public static boolean bluetoothStatus = false;
    public static BluetoothDevice mmDevice = null;


}
