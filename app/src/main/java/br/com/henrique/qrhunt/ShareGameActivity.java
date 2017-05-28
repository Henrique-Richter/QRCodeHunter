package br.com.henrique.qrhunt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import static android.R.attr.width;


public class ShareGameActivity extends AppCompatActivity {

    private static final int WIDTH = 500;
    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_game);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        try {
            Bitmap bitmap = encodeAsBitmap("Test");
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Button shareButton = (Button)findViewById(R.id.sharePdf);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });

    }

    private void createPdf(){
        try {
            Document document = new Document();
            String file = Environment.getExternalStorageDirectory().getPath() + "/Hello.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            Paragraph p = new Paragraph("Hello PDF");
            document.add(p);
            document.close();
        }catch(Exception e){
            Log.e("PDFCreator", "Exception:" + e);

        }
    }
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? black : white;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    private void shareIt() {
        /*
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("application/pdf");
        sharingIntent.setType(ACTION_OPEN_DOCUMENT);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+"mnt/sdcard/PDFfiles/"+"MyPDFFILE.pdf"));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
        */
        try {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("image/jpeg");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, encodeAsBitmap("Test").compress(Bitmap.CompressFormat.JPEG, 100, stream));
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
