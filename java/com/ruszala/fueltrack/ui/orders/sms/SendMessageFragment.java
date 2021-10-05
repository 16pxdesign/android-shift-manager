package com.ruszala.fueltrack.ui.orders.sms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.global.CurrentShift;

import java.lang.ref.WeakReference;

public class SendMessageFragment extends Fragment implements OnTaskCallback {

    View view;
    Button smsBtn;
    private final int REQUEST_PERMISSIONS_SMS = 1001;
    private final int REQUEST_PHONE_STATE = 1002;

    public SendMessageFragment() {
    }

    public static SendMessageFragment newInstance() {
        SendMessageFragment fragment = new SendMessageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_message, container, false);
        smsBtn = view.findViewById(R.id.buttonSendSMS);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //check is any message is sending
        try {
            boolean running = SendAsyncTask.getInstance(this).isRunning();
        } catch (Exception e) {
            e.printStackTrace();
        }

        smsBtn.setEnabled(false);
        //check for permissions
        checkSMSPerimissions();
    }

    private void checkSMSPerimissions() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ) {
            Log.d("mylog", "SendMessageFragment checkSMSPerimitions: Need permit SEND_SMS");
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_PERMISSIONS_SMS);
        }else if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            Log.d("mylog", "SendMessageFragment checkSMSPerimitions: Need permit READ_PHONE_STATE");
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        } else {
            Log.d("mylog", "SendMessageFragment checkSMSPerimitions: Permit ok, call setListener");
            //set listener if permissions are granted
            setListener();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("mylog", "SendMessageFragment onRequestPermissionsResult: ");
        switch (requestCode) {
            case REQUEST_PERMISSIONS_SMS:
                Log.d("mylog", "SendMessageFragment onRequestPermissionsResult: case REQUEST_PERMISSIONS_SMS:");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("mylog", "SendMessageFragment onRequestPermissionsResult: REQUEST_PERMISSIONS_SMS Call again check permit");
                    checkSMSPerimissions();
                }
                break;
            case REQUEST_PHONE_STATE:
                Log.d("mylog", "SendMessageFragment onRequestPermissionsResult: case REQUEST_PHONE_STATE:");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("mylog", "SendMessageFragment onRequestPermissionsResult: REQUEST_PHONE_STATE Call again check permit");
                    checkSMSPerimissions();
                }
                break;

        }
    }

    private void setListener() {
        Log.d("mylog", "SendMessageFragment setListener: ");
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
        smsBtn.setEnabled(true);

    }

    private void sendSms() {
        //send sms using task
        try {
            SendAsyncTask task = SendAsyncTask.getInstance(this);
            task.run(CurrentShift.getInstance().getOrder().getValue().getPhone());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartTask() {
        smsBtn.setEnabled(false);
        smsBtn.setText("Sending");

    }

    @Override
    public void onFinishTask() {
        smsBtn.setEnabled(true);
        smsBtn.setText(getResources().getString(R.string.send_sms));
    }


    private static class SendAsyncTask extends AsyncTask<String, Void, Void> {

        private static WeakReference<OnTaskCallback> wrf;
        private static SendAsyncTask mInstance;
        private static boolean isRunning;
        private static FragmentActivity activity;


        public static SendAsyncTask getInstance(Fragment fragment) throws Exception {
            if (mInstance == null)
                mInstance = new SendAsyncTask();
            if (fragment instanceof OnTaskCallback) {
                wrf = new WeakReference<>((OnTaskCallback) fragment);
                if (isRunning) {
                    wrf.get().onStartTask();
                }
                activity = ((Fragment) wrf.get()).getActivity();
            } else {
                throw new Exception("Must implement OnTaskCallback");
            }
            return mInstance;
        }

        public void destroy() {
            mInstance = null;
        }


        public AsyncTask<String, Void, Void> run(String s) {
            if (isRunning)
                return this;
            return execute(s);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isRunning = true;
            if (wrf.get() != null) {
                wrf.get().onStartTask();
            }
        }


        @Override
        protected Void doInBackground(String... strings) {

            sendMessage(strings[0]);

            return null;
        }

        private void sendMessage(String phone) {
            Fragment fragment = (Fragment) wrf.get();
            final FragmentActivity activity = fragment.getActivity();
            String message = "Hello. Your food is on my way :)";
            try {
                SmsManager smsMgrVar = SmsManager.getDefault();
                smsMgrVar.sendTextMessage(phone, null, message, null, null);
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Message sent", Toast.LENGTH_LONG).show();
                        }

                    });
                }
            } catch (final Exception ErrVar) {
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, ErrVar.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }

                    });
                }
                ErrVar.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isRunning = false;
            if (wrf.get() != null) {
                wrf.get().onFinishTask();
            }

            destroy();

        }

        public boolean isRunning() {
            return isRunning;
        }
    }
}
