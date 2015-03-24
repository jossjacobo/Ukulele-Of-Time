# Ukulele of Time

Finally!! An app that allows you to play all the amazing and critically
acclaimed songs from the award winning game 'The Legend of Melda - The Ukulele of Time' and performs BLE Broadcast Bitwise Operations!!

Turning ON Bluetooth Low Energy (BLE) Advertising will broadcast a Custom BLE Service every time a song is *CORRECTLY* played. The song's notes will also be broadcast along with the advertisement inside the Custom Service Data payload. Each note is segmented into 4-bits inside the Service Data payload byte array.

The leftmost 4-bits of each byte will be a note and the rightmost 4-bits will be another
note.

## Legend

  * A - 0001
  * C Down - 0010
  * C Right - 0011
  * C Up - 0100
  * C Left - 0101

## BLE Custom 128-bit UUID

UKULELE_SERVICE = 8F2A9690-FD82-4AA0-953E-79EF126BA95D

## Example - Custom Service Data Payload

```
[1010100, 110101, 1000011]
```
1st byte: 0101 0100 = C Left + C Up


## Challenge

1. Create an app that scans and listens for the above custom ble service.
2. Grab the service's data byte array.
3. Parse out the notes in the played song.
4. Print out on screen the notes in the correct order along with the name of the song.

## Testing

Install the Ukulele of Time on a Lollipop Device that supports BLE Multiple Advertisement (i.e. Nexus 6), play a song and see if your app can pick up the BLE Service Broadcast. If you don't have a device that supports BLE Multiple Advertisements swing by our booth so you can test your app with one of our Demo Devices.

## Judging

We will play several songs in random order with one of our test devices and verify your app results are correct.

## Prizes

First 10 people to successfully complete the challenge will receive an Ocarina!!

## Resources

- [bluetooth-le](https://developer.android.com/guide/topics/connectivity/bluetooth-le.html)