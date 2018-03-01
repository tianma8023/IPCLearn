package com.github.tianma8023.ipclearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.github.tianma8023.ipclearn.aidl.BookManagerActivity;
import com.github.tianma8023.ipclearn.bindpool.BindPoolClientActivity;
import com.github.tianma8023.ipclearn.messenger.MessengerClientActivity;
import com.github.tianma8023.ipclearn.provider.BookProviderActivity;
import com.github.tianma8023.ipclearn.socket.TCPClientActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.jump_2_second) Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.jump_2_second)
    public void jump2SecondActivity() {
        startActivity(new Intent(this, SecondActivity.class));
    }

    @OnClick(R.id.jump_2_messenger_client)
    public void jump2MessengerClientActivity() {
        startActivity(new Intent(this, MessengerClientActivity.class));
    }

    @OnClick(R.id.jump_2_book_manager_activity)
    public void jump2BookManagerActivity() {
        startActivity(new Intent(this, BookManagerActivity.class));
    }

    @OnClick(R.id.jump_2_book_provider_activity)
    public void jump2BookProviderActivity() {
        startActivity(new Intent(this, BookProviderActivity.class));
    }

    @OnClick(R.id.jump_2_tcp_client_activity)
    public void jump2TCPClientActivity() {
        startActivity(new Intent(this, TCPClientActivity.class));
    }

    @OnClick(R.id.jump_2_bind_pool_activity)
    public void jump2BindPoolActivity() {
        startActivity(new Intent(this, BindPoolClientActivity.class));
    }
}
