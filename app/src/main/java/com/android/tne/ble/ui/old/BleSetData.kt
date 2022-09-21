package com.android.tne.ble.ui.old

import android.widget.AdapterView
import android.widget.RadioGroup
import com.android.tne.ble.BLECommManager
import com.android.tne.ble.BTScanEvent
import com.android.tne.ble.BTStateEvent
import com.android.tne.ble.ui.DeviceListAdapter
import com.android.tne.ble.ui.Simple1ChannelGraph
import com.android.tne.ble.ui.Simple3ChannelGraph
import com.solmi.uxprotocol.UxParserEvent
import java.util.*

/**
 * BlueTooth Setting View DataClass
 * 2022.05.11
 */
data class BleSetData(var mBTScanEventHandler: BTScanEvent? = null,
                      var mBTStateEventHandler: BTStateEvent? = null,
                      var mUxParserEventHandler: UxParserEvent? = null,
                      var mItemClickListener: AdapterView.OnItemClickListener? = null,
                      var mBLEManager: BLECommManager? = null,
                      var mDeviceListAdapter: DeviceListAdapter? = null,
                      var mEMGBuffer: Queue<IntArray>? = null,
                      var mAccBuffer: Queue<IntArray>? = null,
                      var mGyroBuffer: Queue<IntArray>? = null,
                      var mMagnetoBuffer: Queue<IntArray>? = null,
                      var mDataUpdateTimer: Timer? = null,
                      var mStartTime: Long = 0,
                      var mEMGCount: Int = 0,
                      var mAccCount: Int = 0,
                      var mGyroCount: Int = 0,
                      var mMagnetoCount: Int =0,
                      var mSGEMGGraph: Simple1ChannelGraph? = null,
                      var mSGAccGraph: Simple3ChannelGraph? = null,
                      var mSGGyroGraph: Simple3ChannelGraph? = null,
                      var mSGMagnetoGraph: Simple3ChannelGraph? = null,
                      var mRGSamplingRate: RadioGroup? = null)
