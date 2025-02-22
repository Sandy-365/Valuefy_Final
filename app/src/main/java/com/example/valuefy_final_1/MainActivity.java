package com.example.valuefy_final_1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.files.types.UploadedFile;
import com.assemblyai.api.resources.transcripts.requests.TranscriptParams;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import com.assemblyai.api.resources.transcripts.types.TranscriptStatus;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DrawerLayout dl;
    ImageButton menu;
    NavigationView navigate;
    private static final String API_KEY = "e3288e102c124087944a4e2f7cc8880e";
    private TextView textView;
    private Button pickFileButton;
    private ImageButton recordButton;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private ActivityResultLauncher<Intent> speechRecognizerLauncher;
    public static String transtext = " ";

    ScrollView s1;
    Button extract;
    ImageView im1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = findViewById(R.id.dl);
        menu = findViewById(R.id.menu_dl);
        navigate = findViewById(R.id.navigation);
        textView = findViewById(R.id.demotext);
        extract = findViewById(R.id.extract);
        pickFileButton = findViewById(R.id.pickFileButton);
        recordButton = findViewById(R.id.record);
        s1 = findViewById(R.id.scrollView4);
        im1 = findViewById(R.id.loder);
        Glide.with(this).asGif().load(R.drawable.loading).into(im1);


        menu.setOnClickListener(view -> dl.open());
        navigate.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ItemId = item.getItemId();
                if (ItemId == R.id.navHome) {
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                }
                if (ItemId == R.id.navToDo) {
                    Toast.makeText(MainActivity.this, "ToDo", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ToDo.class);
                    startActivity(intent);
                    finish();
                }
                if (ItemId == R.id.navHistory) {
                    Toast.makeText(MainActivity.this, "History", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, History.class);
                    startActivity(intent);
                    finish();

                }
                if (ItemId == R.id.navContact) {
                    Toast.makeText(MainActivity.this, "Contact", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, contact.class);
                    startActivity(intent);
                    finish();
                }
                if (ItemId == R.id.navFeedback) {
                    Toast.makeText(MainActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, feedback.class);
                    startActivity(intent);
                    finish();
                }
                if (ItemId == R.id.navShare) {
                    Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
                }
                if (ItemId == R.id.navLogout) {
                    Toast.makeText(MainActivity.this, "Loging Out", Toast.LENGTH_SHORT).show();
                }
                dl.close();
                return false;
            }
        });
        requestPermissions();

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        if (fileUri != null) {
                            im1.setVisibility(View.VISIBLE);
                            String filePath = getFilePathFromUri(fileUri);
                            audioTrans(filePath);
                        }
                    }
                }
        );

        speechRecognizerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<String> speechResult = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (speechResult != null && !speechResult.isEmpty()) {
                            s1.setVisibility(View.VISIBLE);
                            extract.setVisibility(View.VISIBLE);
                            im1.setVisibility(View.GONE);
                            textView.setText("Transcription:\n" + speechResult.get(0));
                        }
                    }
                }
        );

        pickFileButton.setOnClickListener(v -> openFileChooser());
        extract.setOnClickListener(v -> openGeminiAPI());
        recordButton.setOnClickListener(v -> startSpeechToText());
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 100);
        }
    }

    private void openFileChooser() {
        s1.setVisibility(View.GONE);
        extract.setVisibility(View.GONE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        filePickerLauncher.launch(Intent.createChooser(intent, "Select Audio File"));
    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        speechRecognizerLauncher.launch(intent);
    }

    private void openGeminiAPI() {
        Toast.makeText(this, "Opening Gemini API", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        String str1 = textView.getText().toString();
        intent.putExtra(transtext,str1);
        startActivity(intent);
    }

    private String getFilePathFromUri(Uri uri) {
        String fileName = "selected_audio.mp3";

        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                }
            }
        }

        File file = new File(getCacheDir(), fileName);

        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

        } catch (Exception e) {
            runOnUiThread(() -> {
                s1.setVisibility(View.VISIBLE);
                extract.setVisibility(View.VISIBLE);
                im1.setVisibility(View.GONE);
                textView.setText("Error reading file: " + e.getMessage());
            });
        }

        return file.getAbsolutePath();
    }

    public void audioTrans(String localFilePath) {
        new Thread(() -> {
            try {
                File file = new File(localFilePath);
                if (!file.exists()) {
                    runOnUiThread(() -> {
                        s1.setVisibility(View.VISIBLE);
                        extract.setVisibility(View.VISIBLE);
                        im1.setVisibility(View.GONE);
                        textView.setText("File not found: " + localFilePath);
                    });
                    return;
                }

                AssemblyAI client = AssemblyAI.builder()
                        .apiKey(API_KEY)
                        .build();

                UploadedFile uploadedFile = client.files().upload(file);
                String uploadedFileUrl = uploadedFile.getUploadUrl();

                TranscriptParams transcriptParams = TranscriptParams.builder()
                        .audioUrl(uploadedFileUrl)
                        .build();

                Transcript transcript = client.transcripts().transcribe(transcriptParams);

                if (transcript.getStatus() == TranscriptStatus.ERROR) {
                    throw new Exception("Transcript failed: " + transcript.getError().orElse("Unknown error"));
                }

                runOnUiThread(() -> {
                    s1.setVisibility(View.VISIBLE);
                    extract.setVisibility(View.VISIBLE);
                    im1.setVisibility(View.GONE);
                    textView.setText("Transcription:\n" + transcript.getText());
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    s1.setVisibility(View.VISIBLE);
                    extract.setVisibility(View.VISIBLE);
                    im1.setVisibility(View.GONE);
                    textView.setText("Error: " + e.getMessage());
                });
            }
        }).start();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
