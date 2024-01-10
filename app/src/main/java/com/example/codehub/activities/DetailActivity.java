package com.example.codehub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codehub.R;
import com.example.codehub.adapters.ImagesAdapter;
import com.example.codehub.database.SqlConnection;
import com.example.codehub.models.Report;
import com.example.codehub.models.Source;
import com.example.codehub.models.User;
import com.example.codehub.utils.Email;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    private SqlConnection connection;
    private int idSource = 0;
    TextView btnBack, btnMore, views, purchases, nameSource, coder, fee, language, type, desc;
    Button btnAddToCart, btnBuyNow;
    WebView webView;
    RecyclerView rvImages;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        Intent intent = getIntent();
        if (intent != null) {
            idSource = intent.getIntExtra("IdSource", 0);
            if (idSource != 0) {
                connection = new SqlConnection(this);
                Source source = connection.getSource(idSource);
                if (source != null) {
                    setupYouTubeVideo(webView, source.getLinkVideo());
                    views.setText(String.valueOf(source.getViews()));
                    purchases.setText(String.valueOf(source.getPurchases()));
                    nameSource.setText(source.getName());
                    coder.setText(source.getCoder());
                    fee.setText(String.valueOf(source.getFee()));
                    language.setText(source.getLanguage());
                    type.setText(source.getType());
                    desc.setText(source.getDescription());
                    rvImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                    rvImages.setAdapter(new ImagesAdapter(connection.listImage(idSource)));

                    btnAddToCart.setOnClickListener(v -> {
                        String message = connection.addToCart(idSource) ? "Đã thêm vào giỏ hàng" : "Thêm vào giỏ hàng thất bại";
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    });

                    btnBuyNow.setOnClickListener(v -> {
                        int status = connection.checkPayment(idSource);
                        if (status == 0) {
                            openDialog("Nạp xu", "Bạn không đủ xu để thanh toán", "Nạp", (dialog, which) -> {
                                startActivity(new Intent(this, TopUpActivity.class));
                            });
                        } else if (status == 1) {
                            showDialogPayment();
//                            openDialog("Xác nhận thanh toán", "Bạn có chắc muốn mua " + source.getName(), "Xác nhận", (dialog, which) -> {
//
//                            });
                        } else if (status == -1) {
                            Toast.makeText(this, "Lỗi, hãy thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else onBackPressed();
        }
        btnMore.setOnClickListener(v -> {
            //Button report
            PopupMenu popupMenu = new PopupMenu(this, btnMore);

            for (String name : getReportMap().values()) {
                popupMenu.getMenu().add(name);
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                //String selectedName = item.getTitle().toString();
                for (Map.Entry<Integer, String> entry : getReportMap().entrySet()) {
                    if (entry.getValue().equals(item.getTitle().toString())) {
                        String message = connection.postReport(entry.getKey(), idSource) ? "Đã gửi báo cáo của bạn" : "Có lỗi, hãy thử lại!";
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            });

            popupMenu.show();
        });


        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void showDialogPayment() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payment, null);
        dialogBuilder.setView(dialogView);
        TextView fullname, email, namecode, total;
        Button btnCancel, btnConfirm;
        btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        btnCancel = dialogView.findViewById(R.id.btnCancel);
        fullname = dialogView.findViewById(R.id.tv_FullName);
        email = dialogView.findViewById(R.id.tv_Email);
        namecode = dialogView.findViewById(R.id.tv_NameCode);
        total = dialogView.findViewById(R.id.tv_Fee);
        User u = connection.getUser();
        fullname.setText(u.getFullName());
        email.setText(u.getEmail());
        namecode.setText(nameSource.getText().toString());
        total.setText(fee.getText().toString());
        AlertDialog alertDialog = dialogBuilder.create();
        btnConfirm.setOnClickListener(view -> {
            int totalFee = Integer.parseInt(total.getText().toString());
            boolean success = connection.progressPayment(idSource, totalFee);
            if (success) {
                String link = connection.getLinkDownload(idSource);
                if (!link.isEmpty()) {
                    new Thread(() -> Email.sendOTP(u.getEmail(),
                            namecode.getText().toString(),
                            "Link download của bạn: " + link)).start();
                    alertDialog.dismiss();
                    Toast.makeText(this, "Hãy kiểm tra email của bạn", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
        });
        btnCancel.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupYouTubeVideo(WebView webView, String videoUrl) {
        String videoId = extractVideoIdFromUrl(videoUrl);
        if (videoId != null) {
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = width * 9 / 16;
            webView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            webView.loadUrl("https://www.youtube.com/embed/" + videoId);
            webView.getSettings().setJavaScriptEnabled(true);
        }
    }

    private String extractVideoIdFromUrl(String videoUrl) {
        String videoId = null;
        if (videoUrl != null && videoUrl.contains("youtu.be")) {
            int index = videoUrl.lastIndexOf("/") + 1;
            videoId = videoUrl.substring(index);
        } else if (videoUrl != null && videoUrl.contains("watch")) {
            String[] splitUrl = videoUrl.split("=");
            if (splitUrl.length > 1) {
                videoId = splitUrl[1];
            }
        }
        return videoId;
    }

    private void openDialog(String title, String message, String positiveBtn, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMessage(message);

        // Thiết lập OnClickListener cho PositiveButton
        builder.setPositiveButton(positiveBtn, listener);

        builder.setNegativeButton("Thoát", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public Map<Integer, String> getReportMap() {
        Map<Integer, String> reportMap = new HashMap<>();
        List<Report> reportList = connection.listReport();

        if (reportList != null) {
            for (Report report : reportList) {
                reportMap.put(report.getId(), report.getName());
            }
        }

        return reportMap;
    }

    private void init() {
        webView = findViewById(R.id.wv_youtube);
        rvImages = findViewById(R.id.listImage);
        btnBack = findViewById(R.id.btnBack);
        btnMore = findViewById(R.id.btnMore);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        views = findViewById(R.id.tv_Views);
        purchases = findViewById(R.id.tv_Purchases);
        nameSource = findViewById(R.id.tv_NameSource);
        coder = findViewById(R.id.tv_Coder);
        fee = findViewById(R.id.tv_Fee);
        language = findViewById(R.id.tv_Language);
        type = findViewById(R.id.tv_Type);
        desc = findViewById(R.id.tv_Desc);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}