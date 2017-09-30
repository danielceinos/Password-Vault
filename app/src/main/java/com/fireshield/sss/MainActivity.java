package com.fireshield.sss;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import com.bumptech.glide.Glide;
import com.fireshield.sss.databinding.ActivityMainBinding;
import timber.log.Timber;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements TextWatcher {

  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    Timber.plant(new Timber.DebugTree());

    binding.etSecret.addTextChangedListener(this);
    binding.etService.addTextChangedListener(this);
    Glide.with(this).load("https://robohash.org/" + (Math.random())).into(binding.ivRobo);
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
  }

  @Override
  public void onTextChanged(CharSequence charS, int i, int i1, int i2) {
    String hash = hash(binding.etSecret.getText().toString());
    String miniHash = hash.substring(0, 8);
    Timber.d(miniHash);
    Glide.with(this).load("https://robohash.org/" + miniHash).into(binding.ivRobo);
    binding.tvHashMin.setText(miniHash);
    String yourHashPassword = hash(binding.etSecret.getText().toString() + binding.etSecret.getText().toString()).substring(0, 16);
    Timber.d(yourHashPassword);
    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText("hash", yourHashPassword);
    clipboard.setPrimaryClip(clip);
  }

  @Override
  public void afterTextChanged(Editable editable) {
  }

  private String hash(String toHash) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-512");
      byte[] bytes = toHash.getBytes("UTF-8");
      digest.update(bytes, 0, bytes.length);
      bytes = digest.digest();

      String hash = bytesToHex(bytes).toLowerCase();
      return hash;
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }
}
