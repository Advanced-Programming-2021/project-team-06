package controller;

import view.Output;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CSVReader {
    public CSVReader(String pathToReadFrom , String pathToWriteTo) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToReadFrom));
            String line;
            String[] headers = bufferedReader.readLine().split(",");
            CsvToJsonTransformator csvToJsonTransformator = new CsvToJsonTransformator(headers);
            while ((line = bufferedReader.readLine()) != null)
                csvToJsonTransformator.makeJsonFileOutOfCsvLine(line.split(",") , pathToWriteTo);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            Output.getInstance().showMessage(" FATAL ERROR : file not found please check the path you entered");
        } catch (IOException ioException) {
            ioException.printStackTrace();
            Output.getInstance().showMessage("FATAL ERROR : IO Error occurred while reading the csv file");
        }
    }
}
