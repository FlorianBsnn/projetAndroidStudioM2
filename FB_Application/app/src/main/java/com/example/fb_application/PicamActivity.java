package com.example.fb_application;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PicamActivity extends AppCompatActivity {


    public static String SERVER_IP ="192.168.0.44";
    public static final int SERVER_PORT = 5000;

    private FileOutputStream fos = null;
    private DataInputStream din = null;
    ImageView mImageView;
    Button stop;
    Button start;
    private boolean videoOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MySingletonClass.getInstance().isDarkMode()){
            getTheme().applyStyle(R.style.AppThemeDark, true);
        }
        else{
            getTheme().applyStyle(R.style.AppThemeLight, true);
        }

        setContentView(R.layout.activity_picam);

        SERVER_IP = MySingletonClass.getInstance().getIpAddress();
        start = (Button) findViewById(R.id.StartButton);
        stop = (Button) findViewById(R.id.StopButton);
        stop.setEnabled(false);
        start.setEnabled(true);//non necessaire car un bouton est de base activé

        mImageView = (ImageView) findViewById(R.id.picamView);

    }

    public void Stop(View view) {
        videoOn = false;
        stop.setEnabled(false);
        start.setEnabled(true);
    }

    public void Start(View view) {
        videoOn = true;
        Thread t = new Thread(){

            @Override
            public void run() {
                try {

                    while (videoOn){
                        Socket s = new Socket(SERVER_IP, SERVER_PORT);
                        din = new DataInputStream(s.getInputStream());

                        //SI ON VEUT ENVOYER UN MESSAGE AVEC TCP
                        //DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                        //EditText et = (EditText) findViewById(R.id.EditText01);
                        //String str = et.getText().toString();
                        //dos.writeUTF(str);

                        File file = new File(getCacheDir()+"/image.png");
                        // creation du fichier si il n'existe pas
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        fos = new FileOutputStream(file);
                        byte[] buffer = new byte[8192];
                        for(int counter=0; (counter = din.read(buffer, 0, buffer.length)) >= 0;)
                        {
                            fos.write(buffer, 0, counter);
                        }
                        fos.flush();
                        fos.close();

                        runOnUiThread(new Runnable() //run on ui thread
                        {
                            public void run()
                            {
                                File file = new File(getCacheDir()+"/image.png");
                                if(file.exists()) {
                                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                                    mImageView.setImageBitmap(myBitmap);
                                }
                            }
                        });

                        //read input stream
                        //DataInputStream dis2 = new DataInputStream(s.getInputStream());
                        //String msg = dis2.readUTF();
                        //Log.e("MainActivity",msg );
                        //Log.e("MainActivity",br.toString() );
                        //dis2.close();

                        s.close();
                        SystemClock.sleep(300); // attente de 0.3 secondes pour se reconnecter et recuperer une nouvelle image
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        stop.setEnabled(true);
        start.setEnabled(false);
    }
}