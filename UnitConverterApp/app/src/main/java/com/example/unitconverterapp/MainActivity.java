package com.example.unitconverterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    // UI Components
    private EditText inputValue;
    private Spinner sourceUnit, destinationUnit;
    private TextView resultText;

    private final Map<String, List<String>> unitCategories = new HashMap<>();
    private final Map<String, Double> lengthConversions = new HashMap<>();
    private final Map<String, Double> weightConversions = new HashMap<>();
    private final Map<String, String> unitSymbols = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        inputValue = findViewById(R.id.inputValue);
        sourceUnit = findViewById(R.id.sourceUnit);
        destinationUnit = findViewById(R.id.destinationUnit);
        Button convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        initializeUnitCategories();
        initializeConversionMaps();
        initializeUnitSymbols();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getAllUnits());
        sourceUnit.setAdapter(adapter);
        destinationUnit.setAdapter(adapter);

        sourceUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterDestinationUnits(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        convertButton.setOnClickListener(v -> {
            String input = inputValue.getText().toString().trim();
            String fromUnit = sourceUnit.getSelectedItem().toString();
            String toUnit = destinationUnit.getSelectedItem().toString();
            resultText.setText(convertUnits(input, fromUnit, toUnit));
        });
    }

    private void initializeUnitCategories() {
        unitCategories.put("Length", Arrays.asList("Inch", "Foot", "Yard", "Mile", "Centimeter", "Meter", "Kilometer"));
        unitCategories.put("Weight", Arrays.asList("Pound", "Ounce", "Kilogram", "Gram"));
        unitCategories.put("Temperature", Arrays.asList("Celsius", "Fahrenheit", "Kelvin"));
    }

    private void initializeConversionMaps() {
        lengthConversions.put("Inch", 0.0254);
        lengthConversions.put("Foot", 0.3048);
        lengthConversions.put("Yard", 0.9144);
        lengthConversions.put("Mile", 1609.34);
        lengthConversions.put("Centimeter", 0.01);
        lengthConversions.put("Meter", 1.0);
        lengthConversions.put("Kilometer", 1000.0);

        weightConversions.put("Pound", 0.453592);
        weightConversions.put("Ounce", 0.0283495);
        weightConversions.put("Kilogram", 1.0);
        weightConversions.put("Gram", 0.001);
    }

    private void initializeUnitSymbols() {
        // Length symbols
        unitSymbols.put("Inch", " in");
        unitSymbols.put("Foot", " ft");
        unitSymbols.put("Yard", " yd");
        unitSymbols.put("Mile", " mi");
        unitSymbols.put("Centimeter", " cm");
        unitSymbols.put("Meter", " m");
        unitSymbols.put("Kilometer", " km");

        // Weight symbols
        unitSymbols.put("Pound", " lb");
        unitSymbols.put("Ounce", " oz");
        unitSymbols.put("Kilogram", " kg");
        unitSymbols.put("Gram", " g");

        // Temperature symbols
        unitSymbols.put("Celsius", "°C");
        unitSymbols.put("Fahrenheit", "°F");
        unitSymbols.put("Kelvin", " K");
    }

    private List<String> getAllUnits() {
        List<String> allUnits = new ArrayList<>();
        unitCategories.values().forEach(allUnits::addAll);
        return allUnits;
    }

    private void filterDestinationUnits(String selectedSourceUnit) {
        String category = getCategoryForUnit(selectedSourceUnit);
        if (category != null) {
            ArrayAdapter<String> filteredAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    unitCategories.get(category)
            );
            destinationUnit.setAdapter(filteredAdapter);
        }
    }

    private String getCategoryForUnit(String unit) {
        return unitCategories.entrySet().stream()
                .filter(entry -> entry.getValue().contains(unit))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    private String convertUnits(String value, String fromUnit, String toUnit) {
        if (value.isEmpty()) {
            return "Please enter a value";
        }

        if (fromUnit.equals(toUnit)) {
            return "Source and destination units cannot be the same";
        }

        double input;
        try {
            input = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return "Please enter a valid number";
        }

        String category = getCategoryForUnit(fromUnit);
        if (category == null || !category.equals(getCategoryForUnit(toUnit))) {
            return "Cannot convert between different categories";
        }

        double result;
        try {
            switch (category) {
                case "Length":
                    result = convertWithMap(input, fromUnit, toUnit, lengthConversions);
                    break;
                case "Weight":
                    result = convertWithMap(input, fromUnit, toUnit, weightConversions);
                    break;
                case "Temperature":
                    result = convertTemperature(input, fromUnit, toUnit);
                    break;
                default:
                    return "Conversion not available";
            }
            return formatNumber(result, toUnit);
        } catch (Exception e) {
            return "Error during conversion";
        }
    }

    private double convertWithMap(double value, String fromUnit, String toUnit, Map<String, Double> conversionMap) {
        double baseValue = value * conversionMap.get(fromUnit);
        return baseValue / conversionMap.get(toUnit);
    }

    private double convertTemperature(double value, String fromUnit, String toUnit) {
        switch (fromUnit + "-" + toUnit) {
            case "Celsius-Fahrenheit": return (value * 9/5) + 32;
            case "Fahrenheit-Celsius": return (value - 32) * 5/9;
            case "Celsius-Kelvin": return value + 273.15;
            case "Kelvin-Celsius": return value - 273.15;
            case "Fahrenheit-Kelvin": return (value - 32) * 5/9 + 273.15;
            case "Kelvin-Fahrenheit": return (value - 273.15) * 9/5 + 32;
            default: throw new IllegalArgumentException("Unknown temperature conversion");
        }
    }

    private String formatNumber(double value, String unit) {
        String symbol = unitSymbols.getOrDefault(unit, "");
        return new DecimalFormat("#.##").format(value) + symbol;
    }
}