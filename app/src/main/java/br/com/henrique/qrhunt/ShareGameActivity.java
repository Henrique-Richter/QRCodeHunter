package br.com.henrique.qrhunt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.R.attr.width;


public class ShareGameActivity extends AppCompatActivity {

    private static final int WIDTH = 500;
    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;
    private String code;
    File mediaStorageDir;
    int quantity;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_game);

        mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){

            }
        }
        Bundle bundle = getIntent().getExtras();

        name = bundle.getString("name");
        code = String.valueOf(UUID.randomUUID());
        quantity = bundle.getInt("quantity");

       // name = "testName";;
       // code = String.valueOf(UUID.randomUUID());
       // quantity = 5;

        Button shareButton = (Button)findViewById(R.id.sharePdf);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });

    }

    private String createPdf(){
        try {
            Document document = new Document();
            // Create a media file name
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

            String file = mediaStorageDir.getPath() + File.separator + timeStamp +".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            Paragraph p = new Paragraph("This is the Main tag, others players will use it to start playing the game");
            document.add(p);
            Image mainImage = Image.getInstance(createGameTag());
            document.add(mainImage);
            Paragraph paragraph = new Paragraph("Hide these tags from the players:");
            document.add(paragraph);

            String[] imagens = generateAllTags(quantity);
            for(int i = 0; i < imagens.length;i++){
                Image image = Image.getInstance(imagens[i]);
                document.add(image);
            }
            document.close();
            return timeStamp;
        }catch(Exception e){
            Log.e("PDFCreator", "Exception:" + e);

        }
        return "";
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
       String pdfile = createPdf();

        File file = new File(mediaStorageDir.getPath(),pdfile + ".pdf");
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("application/pdf");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));


    }

    private void storeImage(Bitmap image,String imageName) {
        File pictureFile = getOutputMediaFile(imageName);
        if (pictureFile == null) {
            Log.d("test",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("test", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("test", "Error accessing file: " + e.getMessage());
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(String imageName){
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        File mediaFile;
        mediaFile = new File(imageName);
        return mediaFile;
    }

    private String[] generateAllTags(int quantity){
        String[] imagePaths = new String[quantity];
        String imageName;
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

        for(int i = 0;i<quantity;i++){
            imageName = mediaStorageDir.getPath() + File.separator +
                    timeStamp + String.valueOf(i)  + ".jpg";
            imagePaths[i] = imageName;


            String JsonToBeEncoded = createBitMap(i+1);

            Bitmap bitmap = null;
            try {
                bitmap = encodeAsBitmap(JsonToBeEncoded);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            storeImage(bitmap,imageName);
        }

        return imagePaths;
    }

    private String createBitMap(int quantity){
        String q = "\"";
        String json = "{\"code\":" + q + code + q + ",\"step\":"+  String.valueOf(quantity)  +"}" ;
        return json;
    }

    private String createGameTag(){
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String imageName = mediaStorageDir.getPath() + File.separator +
                timeStamp + "main"  + ".jpg";

        String q = "\"";

        String json = "{\"name\":" + q + name + q + ",\"quantity\":"
                + String.valueOf(quantity)
                + ",\"code\":"+ q + code + q +" }";
        //{"name":"game","quantity":5,"code":"2c574b29-0031-44f9-8df8-457ee39a4ad5"}

        Bitmap bitmap = null;
        try {
            bitmap = encodeAsBitmap(json);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        storeImage(bitmap,imageName);
        return imageName;
    }
}
