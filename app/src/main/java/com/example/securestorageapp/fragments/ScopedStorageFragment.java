package com.example.securestorageapp.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.securestorageapp.R;
import com.example.securestorageapp.storage.MediaStoreManager;

import java.util.List;

public class ScopedStorageFragment extends Fragment {

    private MediaStoreManager mediaStoreManager;
    private TextView tvImageContent;
    private ImageView ivLastImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scoped_storage, container, false);

        mediaStoreManager = new MediaStoreManager(requireContext());

        tvImageContent = view.findViewById(R.id.tvImageContent);
        ivLastImage = view.findViewById(R.id.ivLastImage);
        Button btnSaveImage = view.findViewById(R.id.btnSaveImage);
        Button btnLoadImages = view.findViewById(R.id.btnLoadImages);

        btnSaveImage.setOnClickListener(v -> saveTestImage());
        btnLoadImages.setOnClickListener(v -> loadImages());

        return view;
    }

    private void saveTestImage() {
        // Créer un bitmap simple pour le test
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.BLUE);

        String fileName = "test_image_" + System.currentTimeMillis() + ".jpg";
        Uri uri = mediaStoreManager.saveImageToGallery(bitmap, fileName);

        if (uri != null) {
            Toast.makeText(requireContext(), "Image enregistrée: " + fileName, Toast.LENGTH_SHORT).show();
            ivLastImage.setImageBitmap(bitmap);
        } else {
            Toast.makeText(requireContext(), "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImages() {
        List<Uri> images = mediaStoreManager.getAllImages();
        StringBuilder sb = new StringBuilder("Images trouvées:\n");

        if (images.isEmpty()) {
            sb.append("Aucune image trouvée");
        } else {
            for (Uri uri : images) {
                sb.append("- ").append(uri.toString()).append("\n");
            }
            // Charger la première image trouvée
            Bitmap bitmap = mediaStoreManager.loadImageFromUri(images.get(0));
            if (bitmap != null) {
                ivLastImage.setImageBitmap(bitmap);
            }
        }

        tvImageContent.setText(sb.toString());
    }
}
