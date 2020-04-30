package Util;

import java.io.*;

public class FileController {
    private String directory;
    private String filename;
    private StringBuilder content;

    public FileController(String directory, String filename) throws IOException {
        this.directory = directory;
        this.filename = directory + File.separator + filename;
        this.content = new StringBuilder();
        read();
    }

    public void setContent(String content){
        this.content = new StringBuilder(content);
    }

    public String getContent() {
        return content.toString();
    }

    public void append(String content){
        this.content.append(content);
    }

    public void appendWithNewLine(String content){
        append(content);
        append(System.lineSeparator());
    }

    public void read() throws IOException {
        File dir = new File(directory);
        File file = new File(filename);
        if (!dir.exists()) dir.mkdirs();
        if (!file.exists()) file.createNewFile();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null){
            content.append(line);
            content.append(System.lineSeparator());
        }
        reader.close();
    }

    public void save() throws IOException {
        File file = new File(directory);
        if (!file.exists()) file.mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(getContent());
        writer.close();
    }

    public void delete(String target){
        String[] lines = getContent().split(System.lineSeparator());
        StringBuilder newContent = new StringBuilder();
        for(String line: lines){
            if (!target.equalsIgnoreCase(line)){
                newContent.append(line);
                newContent.append(System.lineSeparator());
            }
        }
        setContent(newContent.toString());
    }

    public void update(String oldStr, String newStr){
        delete(oldStr);
        appendWithNewLine(newStr);
    }

    public boolean isExistData(){
        return content.length() > 0;
    }

}
