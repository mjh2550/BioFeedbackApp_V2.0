package com.android.tne.ble.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tne.R;
import com.android.tne.ble.BLECommManager;
import com.android.tne.ble.BLEDefine;
import com.android.tne.ble.BTScanEvent;
import com.android.tne.ble.BTStateEvent;
import com.solmi.bluetoothlibrary.common.BTDataDefine;
import com.solmi.uxprotocol.BatteryInfo;
import com.solmi.uxprotocol.DeviceInfo;
import com.solmi.uxprotocol.HeaderPacket;
import com.solmi.uxprotocol.UxParserEvent;
import com.solmi.uxprotocol.UxProtocol;

import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class BLEMainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 로그 출력을 위한 태그
     */
    private final String TAG = BLEMainActivity.class.getSimpleName();

    /**
     * 필요한 권한 요청 상수
     */
    private final int PERMISSION_REQUEST_CODE = 100;

    /**
     * Component
     */
    protected TextView mTVLogTextView;
    protected Button mBtnScan;
    protected Button mBtnDisconnect;
    protected ListView mLVDeviceList;
    protected Simple1ChannelGraph mSGEMGGraph;
    protected Simple3ChannelGraph mSGAccGraph;
    protected Simple3ChannelGraph mSGGyroGraph;
    protected Simple3ChannelGraph mSGMagnetoGraph;
    protected RadioGroup mRGSamplingRate;
    protected Button mBtnStart;
    protected Button mBtnStop;

    private Context mContext;

    /**
     * 블루투스 검색 이벤트 핸들러
     */
    private BTScanEvent mBTScanEventHandler = null;
    /**
     * 블루투스 상태 변화 이벤트 핸들러
     */
    private BTStateEvent mBTStateEventHandler = null;
    /**
     * 데이터 파싱 이벤트 핸들러
     */
    private UxParserEvent mUxParserEventHandler = null;
    /**
     * 디바이스 리스트 뷰 아이템 클릭 이벤트 핸들러
     */
    private AdapterView.OnItemClickListener mItemClickListener = null;

    /**
     * 블루투스 통신 클래스
     */
    private BLECommManager mBLEManager = null;
    /**
     * 검색된 디바이스 리스트 뷰 어댑터
     */
    private DeviceListAdapter mDeviceListAdapter = null;
    /**
     * EMG 데이터 버퍼
     */
    private Queue<int[]> mEMGBuffer = null;
    /**
     * Acc 데이터 버퍼
     */
    private Queue<int[]> mAccBuffer = null;
    /**
     * Gyro 데이터 버퍼
     */
    private Queue<int[]> mGyroBuffer = null;
    /**
     * Magneto 데이터 버퍼
     */
    private Queue<int[]> mMagnetoBuffer = null;
    /**
     * 데이터 업데이트 타이머
     */
    private Timer mDataUpdateTimer = null;
    /**
     * 측정 시작 시간
     */
    private long mStartTime = 0;
    /**
     * 수신한 EMG 데이터 수
     */
    private int mEMGCount = 0;
    /**
     * 수신한 Acc 데이터 수
     */
    private int mAccCount = 0;
    /**
     * 수신한 Gyro 데이터 수
     */
    private int mGyroCount = 0;
    /**
     * 수신한 Magneto 데이터 수
     */
    private int mMagnetoCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ble_activity_main);
        initBinding();
        initHandler();
        initComponent();
        boolean isPermissionGranted = checkPermission();
        if (isPermissionGranted == false) {
            requestPermission();
        }
    }

    private void initBinding() {

        mContext = this;
        mTVLogTextView = findViewById(R.id.tv_mainLogTextView);
        mBtnStart = findViewById(R.id.btn_mainStart);
        mBtnStop = findViewById(R.id.btn_mainStop);
        mBtnScan = findViewById(R.id.btn_mainScan);
        mBtnDisconnect = findViewById(R.id.btn_mainDisconnect);
        mLVDeviceList = findViewById(R.id.lv_mainDeviceList);
        mSGEMGGraph = findViewById(R.id.sg_mainEMGGraph);
        mSGAccGraph = findViewById(R.id.sg_mainAccGraph);
        mSGGyroGraph = findViewById(R.id.sg_mainGyroGraph);
        mSGMagnetoGraph = findViewById(R.id.sg_mainMagnetoGraph);
        mRGSamplingRate = findViewById(R.id.rg_mainSamplingRate);
        mBtnDisconnect.setOnClickListener(this);
        mBtnScan.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
    }

    /**
     * 필요한 권한 요청하는 함수
     */
    private void requestPermission() {
        String[] needPermissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ActivityCompat.requestPermissions(this, needPermissions, PERMISSION_REQUEST_CODE);
    }

    /**
     * 권한 설정되었는지 확인하는 함수
     * @return 권한 설정 여부
     */
    private boolean checkPermission() {
        int locationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (locationPermissionCheck == PackageManager.PERMISSION_DENIED) {
            return false;
        }

        return true;
    }

    /**
     * 구성 요소들 초기화하는 함수
     */
    private void initComponent() {
        mBLEManager = new BLECommManager(this);
        mBLEManager.registerBTScanEventHandler(mBTScanEventHandler);
        mBLEManager.registerBTStateEventHandler(mBTStateEventHandler);
        mBLEManager.registerParserEventHandler(mUxParserEventHandler);
        boolean isBLESupport = mBLEManager.checkIsBLESupport();
        if (isBLESupport) {
            if (mBLEManager.startBLEService() == false) {
                Toast.makeText(getApplicationContext(), R.string.error_ble_start_service, Toast.LENGTH_SHORT).show();
                mBLEManager = null;
                return;
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_not_support_ble, Toast.LENGTH_SHORT).show();
        }

        mLVDeviceList.setOnItemClickListener(mItemClickListener);
        mDeviceListAdapter = new DeviceListAdapter(this);
        mLVDeviceList.setAdapter(mDeviceListAdapter);
        mTVLogTextView.setMovementMethod(new ScrollingMovementMethod());
        mEMGBuffer = new LinkedBlockingQueue<>();
        mAccBuffer = new LinkedBlockingQueue<>();
        mGyroBuffer = new LinkedBlockingQueue<>();
        mMagnetoBuffer = new LinkedBlockingQueue<>();
    }

    /**
     * 이벤트 핸들러들 초기화하는 함수
     */
    private void initHandler() {
        initBTScanEventHandler();
        initBTStateEventHandler();
        initUxParserEventHandler();
        initItemClickListener();
    }

    /**
     * 디바이스 리스트 뷰 아이템 클릭 이벤트 핸들러 초기화하는 함수
     */
    private void initItemClickListener() {
        mItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                if (mBLEManager.getBluetoothState() == BLEDefine.BluetoothState.STATE_CONNECTED) {
                    Toast.makeText(getApplicationContext(), R.string.error_device_connected, Toast.LENGTH_LONG).show();
                    return;
                }

                mBLEManager.stopScanDevice();
                BluetoothDevice device = mDeviceListAdapter.getItem(index);
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mTVLogTextView.setText("device name: " + device.getName());
                mBLEManager.setDeviceType(BTDataDefine.DeviceType.SHC_U4);
                mBLEManager.setReconnectCount(3);
                mBLEManager.connect(device);
            }
        };
    }

    /**
     * 데이터 파싱 이벤트 핸들러 초기화하는 함수
     */
    private void initUxParserEventHandler() {
        mUxParserEventHandler = new UxParserEvent() {
            @Override
            public void onParserHeaderPacket(HeaderPacket headerPacket) {
                switch (headerPacket.getPacketType()) {
                    case UxProtocol.RES_DAQ_STOP:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTVLogTextView.append("\nRES_DAQ_STOP");
                                stopDataUpdateTimer();
                            }
                        });
                        break;
                    case UxProtocol.RES_DAQ:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTVLogTextView.append("\nRES_DAQ");
                                startDataUpdateTimer();
                            }
                        });
                        break;
                    case UxProtocol.RES_SCALE_SET:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String log = "";
                                switch (headerPacket.getECGSignalScale()) {
                                    case UxProtocol.SCALE_1X:
                                        log = String.format("\nRES_SCALE_SET: Scale: 1X");
                                        break;
                                    case UxProtocol.SCALE_2X:
                                        log = String.format("\nRES_SCALE_SET: Scale: 2X");
                                        break;
                                    case UxProtocol.SCALE_4X:
                                        log = String.format("\nRES_SCALE_SET: Scale: 4X");
                                        break;
                                }

                                mTVLogTextView.append(log);
                            }
                        });
                        break;
                    case UxProtocol.RES_BATT_INFO:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BatteryInfo batteryInfo = headerPacket.getBatteryInfo();
                                String log = String.format("\nRES_BATT_INFO: Max: %.1f Cur: %.1f", batteryInfo.getMaximumVoltage(), batteryInfo.getCurrentVoltage());
                                mTVLogTextView.append(log);
                            }
                        });
                        break;
                    case UxProtocol.RES_FIRM_INFO:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DeviceInfo deviceInfo = headerPacket.getDeviceInfo();
                                String log = String.format("\nRES_FIRM_INFO: Major: %d Minor: %d Build: %d", deviceInfo.getMajorVersion(), deviceInfo.getMinorVersion(), deviceInfo.getBuildVersion());
                                mTVLogTextView.append(log);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onParserSpecialPacket(byte type, byte value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String log = String.format("SpecialPacket %02X %02X", type, value);
                        mTVLogTextView.append(log);
                    }
                });
            }

            @Override
            public void onParserECG(int[] channels) {
                mEMGCount++;
                mEMGBuffer.offer(channels.clone());
            }

            @Override
            public void onParserACC(int[] channels) {
                mAccCount++;
                mAccBuffer.offer(channels.clone());
            }

            @Override
            public void onParserGYRO(int[] channels) {
                mGyroCount++;
                mGyroBuffer.offer(channels.clone());
            }

            @Override
            public void onParserMAGNETO(int[] channels) {
                mMagnetoCount++;
                mMagnetoBuffer.offer(channels.clone());
            }

            @Override
            public void onParserTEMP(int temperature) {
                //Not support
            }

            @Override
            public void onParserPPG(int ppg) {
                //Not support
            }

            @Override
            public void onParserStep(int step) {
                //Not support
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, error);
            }
        };
    }

    /**
     * 데이터 업데이트 타이머 시작하는 함수
     */
    private void startDataUpdateTimer() {
        if (mDataUpdateTimer == null) {
            mStartTime = System.currentTimeMillis();
            mDataUpdateTimer = new Timer("Data update Timer");
            mDataUpdateTimer.schedule(getDataUpdateTimerTask(), 0, 25);
        }
    }

    /**
     * 데이터 업데이트 타이머 태스크 반환하는 함수
     * @return 타이머 태스크
     */
    private TimerTask getDataUpdateTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int emgSize = mEMGBuffer.size();
                        for (int count = 0; count < emgSize; count++) {
                            int[] channels = mEMGBuffer.poll();
                            if (channels != null) {
                                float value = (channels[0] / 2047f) * 7.4f;
                                mSGEMGGraph.putValue(value);
                            }
                        }

                        int accSize = mAccBuffer.size();
                        for (int count = 0; count < accSize; count++) {
                            int[] channels = mAccBuffer.poll();
                            if (channels != null) {
                                float[] valueArray = new float[3];
                                for (int index = 0; index < 3; index++) {
                                    valueArray[index] = (channels[index] / 1023f) * 3f;
                                }

                                mSGAccGraph.putValueArray(valueArray);
                            }
                        }

                        int gyroSize = mGyroBuffer.size();
                        for (int count = 0; count < gyroSize; count++) {
                            int[] channels = mGyroBuffer.poll();
                            if (channels != null) {
                                float[] valueArray = new float[3];
                                for (int index = 0; index < 3; index++) {
                                    valueArray[index] = (channels[index] / 1023f) * 3f;
                                }

                                mSGGyroGraph.putValueArray(valueArray);
                            }
                        }

                        int magnetoSize = mMagnetoBuffer.size();
                        for (int count = 0; count < magnetoSize; count++) {
                            int[] channels = mMagnetoBuffer.poll();
                            if (channels != null) {
                                float[] valueArray = new float[3];
                                for (int index = 0; index < 3; index++) {
                                    valueArray[index] = (channels[index] / 1023f) * 3f;
                                }

                                mSGMagnetoGraph.putValueArray(valueArray);
                            }
                        }
                    }
                });
            }
        };
    }

    /**
     * 데이터 업데이트 타이머 종료하는 함수
     */
    private void stopDataUpdateTimer() {
        if (mDataUpdateTimer == null) {
            return;
        }

        mTVLogTextView.append(String.format("\nRun time: %.3f(s)", (System.currentTimeMillis() - mStartTime) / 1000f));
        mTVLogTextView.append(String.format("\nEMG: %d Acc: %d Gyro: %d Magneto: %d", mEMGCount, mAccCount, mGyroCount, mMagnetoCount));
        mDataUpdateTimer.cancel();
        mDataUpdateTimer = null;
    }

    /**
     * 블루투스 상태 변화 이벤트 핸들러 초기화하는 함수
     */
    private void initBTStateEventHandler() {
        mBTStateEventHandler = new BTStateEvent() {
            @Override
            public void onStateChanged(BLEDefine.BluetoothState bluetoothState) {
                switch (bluetoothState) {
                    case STATE_DISCONNECTED:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "onStateChanged: STATE_DISCONNECTED", Toast.LENGTH_SHORT).show();
                                mTVLogTextView.append("\nonStateChanged: STATE_DISCONNECTED");
                                mBtnDisconnect.setEnabled(false);
                                stopDataUpdateTimer();
                            }
                        });
                        break;
                    case STATE_CONNECTING:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "onStateChanged: STATE_CONNECTING", Toast.LENGTH_SHORT).show();
                                mTVLogTextView.append("\nonStateChanged: STATE_CONNECTING");
                            }
                        });
                        break;
                    case STATE_CONNECTED:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "onStateChanged: STATE_CONNECTED", Toast.LENGTH_SHORT).show();
                                mTVLogTextView.append("\nonStateChanged: STATE_CONNECTED");
                                mBtnDisconnect.setEnabled(true);
                            }
                        });
                        break;
                    case STATE_CONNECT_FAIL:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "onStateChanged: STATE_CONNECT_FAIL", Toast.LENGTH_SHORT).show();
                                mTVLogTextView.append("\nonStateChanged: STATE_CONNECT_FAIL");
                            }
                        });
                        break;
                    case STATE_CONNECTION_LOST:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "onStateChanged: STATE_CONNECTION_LOST", Toast.LENGTH_SHORT).show();
                                mTVLogTextView.append("\nonStateChanged: STATE_CONNECTION_LOST");
                            }
                        });
                        break;
                }
            }

            @Override
            public void onReconnect(int reconnectCount) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), String.format("onReconnect: %d", reconnectCount), Toast.LENGTH_SHORT).show();
                        mTVLogTextView.append(String.format("\nonReconnect: %d", reconnectCount));
                    }
                });
            }

            @Override
            public void onReconnectStop() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "onReconnectStop: ", Toast.LENGTH_SHORT).show();
                        mTVLogTextView.append("\nonReconnectStop: ");
                        mBtnDisconnect.setEnabled(false);
                        stopDataUpdateTimer();
                    }
                });
            }

            @Override
            public void onUpdateRSSI(int rssi) {

            }
        };
    }

    /**
     * 블루투스 검색 이벤트 핸들러 초기화하는 함수
     */
    private void initBTScanEventHandler() {
        mBTScanEventHandler = new BTScanEvent() {
            @Override
            public void onScanDevice(BluetoothDevice bluetoothDevice) {
                if (bluetoothDevice == null) {
                    return;
                }

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                String name = bluetoothDevice.getName();
                if (name == null) {
                    return;
                }

                if (name.contains("SHC") || name.contains("i8")) {
                    mDeviceListAdapter.addItem(bluetoothDevice);
                }
            }

            @Override
            public void onScanDeviceList(List<BluetoothDevice> bluetoothDeviceList) {
                for (BluetoothDevice bluetoothDevice :
                        bluetoothDeviceList) {
                    if (bluetoothDevice == null) {
                        continue;
                    }

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    String name = bluetoothDevice.getName();
                    if (name == null) {
                        continue;
                    }

                    if (name.contains("SHC")) {
                        mDeviceListAdapter.addItem(bluetoothDevice);
                    }
                }
            }

            @Override
            public void onScanFinished() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnScan.setEnabled(true);
                        mBtnScan.setText(R.string.button_scan);
                        Toast.makeText(getApplicationContext(), R.string.scan_complete, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBLEManager != null) {
            mBLEManager.stopScanDevice();
            mBLEManager.stop();
            mBLEManager.disconnect();
            mBLEManager.stopBLEService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length == 1) {
                    boolean isPermissionGranted = false;
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        isPermissionGranted = true;
                    }

                    if (isPermissionGranted == false) {
                        requestPermission();
                    }
                } else {
                    requestPermission();
                }
                break;
        }
    }

    public void onClickScan() {
//        Toast.makeText(this,"onclickScan",Toast.LENGTH_SHORT).show();
        mDeviceListAdapter.reset();
        mBtnScan.setText(R.string.button_scanning);
        mBtnScan.setEnabled(false);
        mBLEManager.startScanDevice(20 * 1000);
    }

    public void onClickStart() {
        resetComponent();
        if (mRGSamplingRate.getCheckedRadioButtonId() == R.id.rb_mainSamplingRate250) {
            mSGEMGGraph.setSamplingRate(250);
            mSGAccGraph.setSamplingRate(31.25f);
            mSGGyroGraph.setSamplingRate(31.25f);
            mSGMagnetoGraph.setSamplingRate(31.25f);
            mTVLogTextView.append("\nonClickStart: Send start command 250 SPS");
            mBLEManager.start(UxProtocol.DAQ_ECG_ACC_GYRO_MAGNETO_SET, UxProtocol.SAMPLINGRATE_250);
        } else if (mRGSamplingRate.getCheckedRadioButtonId() == R.id.rb_mainSamplingRate500) {
            mSGEMGGraph.setSamplingRate(500);
            mSGAccGraph.setSamplingRate(62.5f);
            mSGGyroGraph.setSamplingRate(62.5f);
            mSGMagnetoGraph.setSamplingRate(62.5f);
            mTVLogTextView.append("\nonClickStart: Send start command 500 SPS");
            mBLEManager.start(UxProtocol.DAQ_ECG_ACC_GYRO_MAGNETO_SET, UxProtocol.SAMPLINGRATE_500);
        }
    }

    /**
     * 구성요소 초기화하는 함수
     */
    private void resetComponent() {
        mEMGBuffer.clear();
        mAccBuffer.clear();
        mGyroBuffer.clear();
        mMagnetoBuffer.clear();
        mStartTime = 0;
        mEMGCount = 0;
        mAccCount = 0;
        mGyroCount = 0;
        mMagnetoCount = 0;
    }

    public void onClickStop() {
        mTVLogTextView.append("\nonClickStop: Send stop command");
        mBLEManager.stop();
    }

    public void onClickDisconnect() {
        if (mBLEManager.getBluetoothState() == BLEDefine.BluetoothState.STATE_CONNECTED) {
            mBLEManager.stop();
            mBLEManager.disconnect();
        } else {
            mBLEManager.stop();
            mBLEManager.disconnect();
            mBtnDisconnect.setEnabled(false);
            stopDataUpdateTimer();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_mainScan :
                onClickScan();
                break;
            case R.id.btn_mainStart:
                onClickStart();
                break;
            case R.id.btn_mainStop :
                onClickStop();
                break;
            case R.id.btn_mainDisconnect:
                onClickDisconnect();
                break;
        }



    }
}
