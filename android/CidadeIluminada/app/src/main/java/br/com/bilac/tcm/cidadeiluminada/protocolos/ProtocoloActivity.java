package br.com.bilac.tcm.cidadeiluminada.protocolos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.bilac.tcm.cidadeiluminada.Constants;
import br.com.bilac.tcm.cidadeiluminada.R;
import br.com.bilac.tcm.cidadeiluminada.protocolos.validators.EmptyValidator;
import br.com.bilac.tcm.cidadeiluminada.protocolos.validators.ValidationState;

public class ProtocoloActivity extends ActionBarActivity {

    private Uri fileUri;

    private ValidationState descricaoValidationState;
    private ValidationState cepValidationState;
    private ValidationState numeroValidationState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        descricaoValidationState = new ValidationState();
        cepValidationState = new ValidationState();
        numeroValidationState = new ValidationState();

        setContentView(R.layout.activity_protocolo);
        EditText descricaoEditText = (EditText) findViewById(R.id.protocoloDescricaoEditText);
        descricaoEditText.addTextChangedListener(new EmptyValidator(descricaoEditText, descricaoValidationState));

        EditText cepEditText = (EditText) findViewById(R.id.cepEditText);
        cepEditText.addTextChangedListener(new EmptyValidator(cepEditText, cepValidationState));

        EditText numeroEditText = (EditText) findViewById(R.id.numeroEditText);
        numeroEditText.addTextChangedListener(new EmptyValidator(numeroEditText, numeroValidationState));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_protocolo, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_novo_protocolo:
                enviarNovoProtocolo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enviarNovoProtocolo() {
        if (descricaoValidationState.isValid() && numeroValidationState.isValid()
                && cepValidationState.isValid()) {
            Toast.makeText(this, "Enviando protocolo", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Existem erros no formulário", Toast.LENGTH_SHORT).show();
        }
    }

    public void openProtocoloCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri();
        if (fileUri == null) {
            Log.e("ProtocoloActivity", "Falha ao criar arquivo da foto.");
            return;
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ImageButton img = (ImageButton) findViewById(R.id.openCameraButton);
                Bitmap bmp = decodeSampledBitmapFromFile(fileUri.getPath(), img.getWidth(),
                        img.getHeight());
                img.setImageBitmap(bmp);
            }
        }
    }

    private Bitmap decodeSampledBitmapFromFile(String path, int requiredWidth, int requiredHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;
        if (height > requiredHeight) {
            inSampleSize = Math.round((float)height / (float)requiredHeight);
        }
        int expectedWidth = width / inSampleSize;
        if (expectedWidth > requiredWidth) {
            inSampleSize = Math.round((float)width / (float)requiredWidth);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private Uri getOutputMediaFileUri(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        File mediaStorageDir =
                new File(Environment.getExternalStoragePublicDirectory(
                         Environment.DIRECTORY_PICTURES), Constants.APPLICATION_NAME);
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return Uri.fromFile(new File(mediaStorageDir.getPath() +
                            File.separator + "IMG_"+ timeStamp + ".jpg"));
    }
}