package main.java.ru.clevertec.check;
import main.java.ru.clevertec.check.ElementList;
import java.io.*;
import java.util.Calendar;

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

        int[]idArray=new int[argsLength-2];
        int[]quantityArray=new int[argsLength-2];

        for (int i =0; i <argsLength-2;i++)
        {
            idArray[i] = Integer.parseInt((args[i].split("-"))[0]);
            quantityArray[i]=Integer.parseInt((args[i].split("-"))[1]);

        }

        try{
            BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/products.csv"));
            String line = reader.readLine();
            while (line != null) {
                String[] values = line.split(";");
                for (int i = 0; i < 4;i++)
                {
                //    System.out.println(values[i]);
                }
                line = reader.readLine();

            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        CheckElement ch = new CheckElement(1, 6);
        ElementList elementList = new ElementList();
        elementList.add(ch);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/result.csv"));
            //current date and time
            Calendar c = Calendar.getInstance();
            String currentTime = (String.format("%02d",  c.get(Calendar.HOUR_OF_DAY)))  + ":" +
                    String.format("%02d",  c.get(Calendar.MINUTE)) + ":" +
                    String.format("%02d",  c.get(Calendar.SECOND));
            String currentDate = (String.format("%02d",  c.get(Calendar.DAY_OF_MONTH)))  + "." +
                    String.format("%02d",  c.get(Calendar.MONTH)) + "." +
                    String.format("%02d",  c.get(Calendar.YEAR));
/*
            writer.write("Date;Time\n");
            writer.write(currentDate + ";" + currentTime + "\n");
            writer.write("\nQTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");
            //TODO
            writer.write("\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n");
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
