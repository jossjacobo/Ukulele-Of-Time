package com.joss.ukuleleoftime.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by: jossayjacobo
 * Date: 2/25/15
 * Time: 2:39 PM
 */
public class BleService extends Service {

    public static final String TAG = BleService.class.getSimpleName();

    public static final String BROADCAST = "BleService.broadcast";
    public static final String ACTION_ID = "action_id";
    public static final int STOP_ADVERTISING = 0;
    public static final int START_ADVERTISING = 1;

    private static final int ADVERTISE_PERIOD = 10000;
    private static final int CONNECTION_TIMEOUT = 10000;

    LocalBroadcastManager broadcast;

    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    BluetoothLeAdvertiser bluetoothLeAdvertiser;

    AdvertiseCallback advertiseCallback;
    AdvertiseSettings advertiseSettings;
    AdvertiseData advertiseData;
    UkuleleHandler ukuleleHandler;

    boolean advertising = false;
    boolean initialized = false;

    @Override
    public IBinder onBind(Intent intent) {
        return new UkuleleBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopAdvertising();
        return super.onUnbind(intent);
    }

    public void initialize(){
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        broadcast = LocalBroadcastManager.getInstance(this);

        advertiseSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTimeout(CONNECTION_TIMEOUT)
                .build();

        //limited to 31 bytes
        advertiseData = new AdvertiseData.Builder()
                .addServiceUuid(GattAttributes.UUID_UKULELE_SERVICE)
                .build();

        advertiseCallback = new UkuleleAdvertiseCallback();

        ukuleleHandler = new UkuleleHandler();
        initialized = true;
    }

    public void setData(byte[] serviceData){
        stopAdvertising();
        //limited to 31 bytes
        advertiseData = new AdvertiseData.Builder()
                .addServiceUuid(GattAttributes.UUID_UKULELE_SERVICE)
                .addServiceData(GattAttributes.UUID_UKULELE_SERVICE, serviceData)
                .build();

        String data = "[";
        for(int i = 0; i < serviceData.length; i++){
            data += Integer.toBinaryString(serviceData[i]);

            if(i != serviceData.length - 1)
                data += ", ";
        }
        data += "]";
        Log.e(TAG, "service data: " + data);
    }

    public void startAdvertising(){
        if (initialized && !advertising) {
            if(bluetoothAdapter.isMultipleAdvertisementSupported()) {
                advertising = true;
                bluetoothLeAdvertiser.startAdvertising(advertiseSettings, advertiseData, advertiseCallback);
                ukuleleHandler.sendMessageDelayed(buildMessage(STOP_ADVERTISING), ADVERTISE_PERIOD);
                sendBroadcast(START_ADVERTISING);
            }else{
                Toast.makeText(this, "Not supported on this device", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void stopAdvertising(){
        if(initialized)
            ukuleleHandler.sendMessage(buildMessage(STOP_ADVERTISING));
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    private class UkuleleAdvertiseCallback extends AdvertiseCallback{
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            sendBroadcast(START_ADVERTISING);
            Log.e(TAG, "Advertising Start Successful");
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            sendBroadcast(STOP_ADVERTISING);

            Log.e(TAG, "Advertising Start Failed: Error Code - " + errorCode);
        }
    }

    public class UkuleleBinder extends Binder {
        public BleService getService(){
            return BleService.this;
        }
    }

    private class UkuleleHandler extends Handler {

        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case STOP_ADVERTISING:
                    advertising = false;
                    sendBroadcast(STOP_ADVERTISING);
                    if(bluetoothLeAdvertiser != null)
                        bluetoothLeAdvertiser.stopAdvertising(advertiseCallback);
                    break;
            }
        }
    }

    private Message buildMessage(int what){
        Message message = new Message();
        message.what = what;
        return message;
    }

    /**
     * Send Broadcast Action Updates to Main Activity
     *
     * @param action Action Integer Value
     */
    private void sendBroadcast(int action){
        Intent intent = new Intent(BROADCAST);
        intent.putExtra(ACTION_ID, action);
        broadcast.sendBroadcast(intent);
    }
}
