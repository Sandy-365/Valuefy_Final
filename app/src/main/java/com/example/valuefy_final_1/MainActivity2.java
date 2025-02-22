package com.example.valuefy_final_1;

import static com.example.valuefy_final_1.MainActivity.transtext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

public class MainActivity2 extends AppCompatActivity {
    TextView viewText, dateText;
    Button saveKey, addToDo;
    ImageButton back;
    ScrollView sc1,sc2;
    TextView t1,t2;
    ImageView im1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize Views
        viewText = findViewById(R.id.textView);
        dateText = findViewById(R.id.dateView);
        saveKey = findViewById(R.id.saveKey);
        addToDo = findViewById(R.id.addToDo);
        back = findViewById(R.id.back_to_do);
        sc1 = findViewById(R.id.scrollView2);
        sc2  =findViewById(R.id.scrollView3);
        t1 = findViewById(R.id.textView3);
        t2 = findViewById(R.id.textView4);
        im1 = findViewById(R.id.nativeGifView);

        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(im1);

        // Set Buttons Initially GONE
        saveKey.setVisibility(View.GONE);
        addToDo.setVisibility(View.GONE);

        // Get transcription from Intent
        Intent inten = getIntent();
        String trans = inten.getStringExtra(transtext);
        Toast.makeText(this,trans, Toast.LENGTH_SHORT).show();
        viewText.setText(trans);

        // Refined prompt
        String finestr = "Extract the important key points from this transcript: '" + trans +
                "'. At the end, provide important dates and times in this format:\n\n" +
                "Key Points:\n[point1, point2, ...]\n\nDates and Times:\n[date1, date2, ...]";

        // Gemini API call
        modelCall(finestr);

        saveKey.setOnClickListener(view ->
                Toast.makeText(getApplicationContext(), "Key points have been saved", Toast.LENGTH_SHORT).show());

        addToDo.setOnClickListener(view ->
                Toast.makeText(getApplicationContext(), "ToDo has been created", Toast.LENGTH_SHORT).show());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // ✅ Placed inside MainActivity2 class
    public void modelCall(String finalstr) {
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyAx1CCVTUffRMJsjmW2X7T-qITXHjaGKfs");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder().addText(finalstr).build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                System.out.println(resultText);

                // Separate key points and dates
                String[] parts = resultText.split("Dates and Times:");
                String resultText_key = parts.length > 0 ? parts[0].replace("Key Points:", "").trim() : "No key points found.";
                String resultText_date = parts.length > 1 ? parts[1].trim() : "No dates found.";

                // Update UI on main thread
                runOnUiThread(() -> {
                    im1.setVisibility(View.GONE);
                    viewText.setText(resultText_key);
                    dateText.setText(resultText_date);
                    saveKey.setVisibility(View.VISIBLE);
                    addToDo.setVisibility(View.VISIBLE);
                    sc1.setVisibility(View.VISIBLE);
                    sc2.setVisibility(View.VISIBLE);
                    t1.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                runOnUiThread(() -> viewText.setText("API call failed: " + t.getMessage()));
            }
        }, MoreExecutors.directExecutor());
    }
}
