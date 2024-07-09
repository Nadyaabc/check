package main.java.ru.clevertec.check;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class CheckRunner {
    public static void main(String[] args) {

        int argsLength= args.length;
        String balance, discountCardNumber;
        boolean hasDiscountCard=false;

        if (args[argsLength-2].contains("discountCard")){
            hasDiscountCard=true;
            balance=args[argsLength-1];
            discountCardNumber=args[argsLength-2];
        }
        else{
            balance=args[argsLength-1];
            discountCardNumber=null;
        }
//add elements to hashmap
        HashMap<Integer, Integer> idQty=new HashMap<>();
        int id;
        for (int i =0; i<argsLength-2; i++)
        {
            id = Integer.parseInt((args[i].split("-"))[0]);
            int qty=Integer.parseInt((args[i].split("-"))[1]);
            idQty.computeIfPresent(id, (a, b)-> b += qty);
            idQty.computeIfAbsent(id, (a) -> qty);
        }

        double total=0, totalDiscount=0, totalWithDiscount=0;

        //current date and time
        Calendar c = Calendar.getInstance();
        String currentTime = (String.format("%02d",  c.get(Calendar.HOUR_OF_DAY)))  + ":" +
                String.format("%02d",  c.get(Calendar.MINUTE)) + ":" +
                String.format("%02d",  c.get(Calendar.SECOND));
        String currentDate = (String.format("%02d",  c.get(Calendar.DAY_OF_MONTH)))  + "." +
                String.format("%02d",  c.get(Calendar.MONTH)) + "." +
                String.format("%02d",  c.get(Calendar.YEAR));

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("src/result1.csv"))){
           //  BufferedReader reader = new BufferedReader(new FileReader(""));
            writer.write("Date;Time\n");
            writer.write(currentDate + ";" + currentTime + "\n");
            writer.write("\nQTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");
            List<String>lines = Files.readAllLines(Paths.get("./src/main/resources/products.csv"));
            Set<Integer> keys =idQty.keySet();
            for (Integer key : keys) {
                for (String line : lines) {
                    String[] splitLine = line.split(";");
                    if (splitLine[0].equals(key.toString())) {
                        writer.write(idQty.get(Integer.parseInt(splitLine[0])) + ";" + splitLine[1] + ";" + splitLine[2]);
                        writer.write("\n");
                    }
                }
            }
            System.out.println("Data successfully written to the file.");

/*
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                String[] values = line.split(";");
                for (int i = 0; i < 5;i++)
                {
                    idQty.forEach((a,b) -> {

                    });
                }
                line = reader.readLine();

            }
            reader.close();*/
        }
        catch (IOException e)
        {System.out.println("Error writing data to the file: " + e.getMessage());
            e.printStackTrace();
        }













        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/result.csv"));
            //current date and time



            //TODO
    /*        writer.write("\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n");
            //TODO
            writer.write("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
            //TODO
            writer.close();
            */



        } catch (IOException e)    // Ошибка записи в файл
        {
            //e.printStackTrace();
        }

    }
}
