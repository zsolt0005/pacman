package pacman;

import java.io.*;

public class SaveHandler {

    public static int load(){
        int score = 0;
        try{
            File save = new File("saves/high.txt");
            if(!save.exists())
                save.createNewFile();

            BufferedReader br = new BufferedReader(new FileReader("saves/high.txt"));
            String loaded = br.readLine();
            if(loaded != null)
                score = Integer.parseInt(loaded);
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return score;
    }

    public static void save(){
        if(Settings.hiScore < Settings.score)
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter("saves/high.txt"));
                bw.write(Settings.score + "");
                bw.close();
            }catch(Exception e){
                e.printStackTrace();
            }
    }

}
