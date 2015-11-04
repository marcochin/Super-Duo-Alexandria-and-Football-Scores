package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by Marco on 9/20/2015.
 */
public class ScannerActivity extends Activity implements ZBarScannerView.ResultHandler {
    private static final String TAG = ScannerActivity.class.getSimpleName();

    public static final int REQUEST_CODE = 1;
    public static final String BARCODE_KEY = "barcode";

    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        // Set the scanner view as the content view
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(BARCODE_KEY, rawResult.getContents());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
