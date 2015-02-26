package com.joss.ukuleleoftime.ble;

import android.os.ParcelUuid;

import java.util.UUID;

/**
 * Created by: jossayjacobo
 * Date: 2/25/15
 * Time: 3:06 PM
 */
public class GattAttributes {

    /**
     * Custom 128-bit UUID
     */
    private static String UKULELE_SERVICE = "8F2A9690-FD82-4AA0-953E-79EF126BA95D";
    private static String GENERIC_SERVICE_DATA = "00009690-0000-1000-8000-00805f9b34fb";

    /**
     * UUIDs
     */
    public static ParcelUuid UUID_UKULELE_SERVICE = new ParcelUuid(UUID.fromString(UKULELE_SERVICE));
    public static ParcelUuid UUID_GENERIC_SERVICE_DATA = new ParcelUuid(UUID.fromString(GENERIC_SERVICE_DATA));

}
