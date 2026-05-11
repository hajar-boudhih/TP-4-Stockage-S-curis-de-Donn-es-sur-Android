package com.example.securestorageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.securestorageapp.R;
import com.example.securestorageapp.security.SecurityAnalyzer;

import java.util.List;

public class SecurityRisksFragment extends Fragment {

    private TextView tvRisksResults;
    private SecurityAnalyzer securityAnalyzer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_security_risks, container, false);

        securityAnalyzer = new SecurityAnalyzer(requireContext());
        tvRisksResults = view.findViewById(R.id.tvRisksResults);
        Button btnAnalyze = view.findViewById(R.id.btnAnalyze);

        btnAnalyze.setOnClickListener(v -> runAnalysis());

        return view;
    }

    private void runAnalysis() {
        List<SecurityAnalyzer.SecurityRisk> risks = securityAnalyzer.analyzeSecurityRisks();
        StringBuilder sb = new StringBuilder();

        if (risks.isEmpty()) {
            sb.append("Aucun risque majeur détecté !");
        } else {
            sb.append("Résultats de l'analyse :\n\n");
            for (SecurityAnalyzer.SecurityRisk risk : risks) {
                sb.append("----------------------------\n");
                sb.append(risk.toString()).append("\n\n");
            }
        }

        tvRisksResults.setText(sb.toString());
    }
}
