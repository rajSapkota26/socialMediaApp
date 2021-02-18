package com.technoabinash.mig33;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.URL;

public class VideoConference extends AppCompatActivity {
    EditText code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_conference);
        code = findViewById(R.id.codeTxt);


        URL serverUrl = null;
        try {
            serverUrl = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverUrl)
                    .setWelcomePageEnabled(false)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinCall(View view) {
        JitsiMeetConferenceOptions meetConferenceOptions = new JitsiMeetConferenceOptions.Builder()
                .setRoom(code.getText().toString())
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeetActivity.launch(this,meetConferenceOptions);
    }
}