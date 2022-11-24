package com.example.finalprojectdjcgrocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifyUserInfo extends AppCompatActivity {

    EditText username, oldPass, password, confpass;
    Button changeName, changePass, backBtn;

    String uPass, uName, uRole;

    DatabaseReference ref;
    User user;

    MediaPlayer mediaPlayer;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);

        Intent getSong = getIntent();
        int resume = getSong.getIntExtra("song", 0);

        if(mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(this,R.raw.song);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
        mediaPlayer.seekTo(resume);
        mediaPlayer.start();

        username = findViewById(R.id.newUsername);
        oldPass = findViewById(R.id.oldPassword);
        password = findViewById(R.id.newPassword);
        confpass = findViewById(R.id.confirmPassword);

        changeName = findViewById(R.id.confirmUsernameBtn);
        changePass = findViewById(R.id.confirmPasswordBtn);
        backBtn = findViewById(R.id.button6);

        user = new User();

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("User").child("1");
                updateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        uPass = snapshot.child("password").getValue().toString();
                        uRole = snapshot.child("role").getValue().toString();
                        //if (snapshot.hasChild("1")) {
                            try {
                                user.setUsername(username.getText().toString().trim());
                                user.setPassword(uPass);
                                user.setRole(uRole);

                                ref = FirebaseDatabase.getInstance().getReference().child("User").child("1");
                                ref.setValue(user);

                                Toast.makeText(ModifyUserInfo.this, "Data Updated", Toast.LENGTH_SHORT).show();
                            } catch (NumberFormatException n) {
                                Toast.makeText(ModifyUserInfo.this, "Data not Updated", Toast.LENGTH_SHORT).show();
                            }
//                        } else {
//                            Toast.makeText(ModifyUserInfo.this, "Unable to Update", Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference().child("User").child("1");
                updateRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.hasChild("1")) {
                            uName = snapshot.child("username").getValue().toString().trim();
                            uPass = snapshot.child("password").getValue().toString().trim();
                            uRole = snapshot.child("role").getValue().toString().trim();
                            if (oldPass.getText().toString().equals(uPass)){
                                if (password.getText().toString().equals(confpass.getText().toString())) {
                                    try {
                                        user.setUsername(uName);
                                        user.setPassword(password.getText().toString().trim());
                                        user.setRole(uRole);

                                        ref = FirebaseDatabase.getInstance().getReference().child("User").child("1");
                                        ref.setValue(user);

                                        Toast.makeText(ModifyUserInfo.this, "Data Updated", Toast.LENGTH_SHORT).show();
                                    } catch (NumberFormatException n) {
                                        Toast.makeText(ModifyUserInfo.this, "Data not Updated", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ModifyUserInfo.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                                }
                        }else {
                                Toast.makeText(ModifyUserInfo.this, "Incorrect old password", Toast.LENGTH_SHORT).show();
                        }
//                        } else {
//                            Toast.makeText(ModifyUserInfo.this, "Unable to Update", Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                position = mediaPlayer.getCurrentPosition();
                Intent i = new Intent(getApplicationContext(), MyAccount.class);
                i.putExtra("song", position);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mediaPlayer.pause();
        position = mediaPlayer.getCurrentPosition();

        switch(item.getItemId()){
            case R.id.account:
                Toast.makeText(this, "Account is selected", Toast.LENGTH_SHORT).show();
                Intent a = new Intent(getApplicationContext(), MyAccount.class);
                a.putExtra("song", position);
                startActivity(a);
                return true;
            case R.id.music:
                Toast.makeText(this, "Background Music is selected", Toast.LENGTH_SHORT).show();
                Intent m = new Intent(getApplicationContext(), BackgroundMusic.class);
                m.putExtra("song", position);
                startActivity(m);
                return true;
            case R.id.categories:
                Toast.makeText(this, "Categories is selected", Toast.LENGTH_SHORT).show();
                Intent c = new Intent(getApplicationContext(), Categories.class);
                c.putExtra("song", position);
                startActivity(c);
                return true;
            case R.id.logout:
                Toast.makeText(this, "Categories is selected", Toast.LENGTH_SHORT).show();
                Intent l = new Intent(getApplicationContext(), MainActivity.class);
                l.putExtra("song", position);
                startActivity(l);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}